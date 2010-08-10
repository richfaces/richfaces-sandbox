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
package org.richfaces.cdk.resource.scan.impl.reflections;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.reflections.util.Utils;

import com.google.common.collect.Multimap;

/**
 * @author Nick Belaevski
 * 
 */
public class ReflectionsExt extends Reflections {

    public ReflectionsExt() {
        super();
    }

    public ReflectionsExt(Configuration configuration) {
        super(configuration);
    }

    public ReflectionsExt(String prefix, Scanner... scanners) {
        super(prefix, scanners);
    }

    public Collection<Class<?>> getMarkedClasses() {
        Map<String, Multimap<String, String>> storeMap = getStore().getStoreMap();
        Multimap<String, String> scannerMMap = storeMap.get(MarkerResourcesScanner.class.getName());
        if (scannerMMap == null) {
            return Collections.emptySet();
        }
        return Utils.forNames(scannerMMap.get(MarkerResourcesScanner.STORE_KEY));
    }

}
