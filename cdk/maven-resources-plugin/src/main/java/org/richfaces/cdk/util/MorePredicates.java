/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.cdk.util;

import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 * 
 */
public final class MorePredicates {

    private MorePredicates() {}

    public static <S, D> Predicate<D> any(Iterable<S> options, Function<S, Predicate<D>> function) {
        if (options == null || Iterables.isEmpty(options)) {
            return Predicates.alwaysTrue();
        }
        
        return Predicates.or(Iterables.transform(options, function));
    }

    public static <S, D> Predicate<D> none(Iterable<S> options, Function<S, Predicate<D>> function) {
        if (options == null || Iterables.isEmpty(options)) {
            return Predicates.alwaysTrue();
        }
        
        Predicate<D> compositePredicate = Predicates.or(Iterables.transform(options, function));
        return Predicates.not(compositePredicate);
    }
    
    public static <S, D> Predicate<D> compose(Iterable<S> includes, Iterable<S> excludes, Function<S, Predicate<D>> function) {
        final Predicate<D> includesPredicate = any(includes, function);
        final Predicate<D> excludesPredicate = none(excludes, function);
        return new Predicate<D>() {
            public boolean apply(D input) {
                return includesPredicate.apply(input) && excludesPredicate.apply(input);
            }
        };
    }

    public static Predicate<String> startsWith(final String prefix) {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.startsWith(prefix);
            }
        };
    }

    public static Predicate<CharSequence> matches(final Pattern pattern) {
        return new Predicate<CharSequence>() {
            @Override
            public boolean apply(CharSequence input) {
                return pattern.matcher(input).matches();
            }
        };
    } 
}