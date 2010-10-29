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
package org.richfaces.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.richfaces.model.SequenceRowKey;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

/**
 * @author Nick Belaevski
 * 
 */
public class SequenceRowKeyConverter implements Converter {

    private static final Joiner DOT_JOINER = Joiner.on('.');

    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    
    private static final Function<String, Integer> INTEGER_PARSER = new Function<String, Integer>() {
        public Integer apply(String from) {
            return Integer.parseInt(from);
        };
    }; 
    
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }

        Iterable<String> split = DOT_SPLITTER.split(value);

        //TODO - handle another types
        return new SequenceRowKey<Integer>(Iterables.toArray(Iterables.transform(split, INTEGER_PARSER), Integer.class));
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        
        SequenceRowKey<?> sequenceRowKey = (SequenceRowKey<?>) value;
        Object[] simpleKeys=sequenceRowKey.getSimpleKeys();

        return DOT_JOINER.join(simpleKeys);
    }

}
