/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.cdk.rd.utils;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.vfs.FileObject;

import org.richfaces.cdk.rd.Component;
import org.richfaces.cdk.rd.Components;
import org.richfaces.cdk.rd.JarResourceScanner;

import org.xml.sax.SAXException;

public class PluginUtils {
    public static String[] DEFAULT_CONFIG_PATTERNS = new String[] {"**/*.component-dependencies.xml"};
    public static String[] DEFAULT_PROCESS_INCLUDES = new String[] {"**/*.xhtml"};

    public static FileObject[] resolveConfigsFromJar(FileObject jarFileObject, String[] patterns) throws IOException {
        FileObject[] result = new FileObject[0];
        JarResourceScanner jarScanner = new JarResourceScanner();

        jarScanner.setBaseFile(jarFileObject);
        jarScanner.setPatterns(patterns);
        jarScanner.doScan();
        result = (FileObject[]) jarScanner.getResult().toArray(new FileObject[jarScanner.getResult().size()]);

        return result;
    }

    public static Map<String, Components> processConfigs(FileObject[] configs, Digester digester)
            throws SAXException, IOException {
        Map<String, Components> collector = new HashMap<String, Components>();

        for (FileObject config : configs) {
            InputStream configInputStream = config.getContent().getInputStream();

            try {
                Components components = (Components) digester.parse(configInputStream);

                collector.put(components.getNamespace(), components);
            } finally {
                configInputStream.close();
            }
        }

        return collector;
    }

    public static Digester createDefaultDigester() {
        Digester digester = new Digester();

        digester.addObjectCreate("components", Components.class);
        digester.addCallMethod("components/namespace", "setNamespace", 0);
        digester.addObjectCreate("components/component", Component.class);
        digester.addCallMethod("components/component/name", "setComponentName", 0);
        digester.addCallMethod("components/component/scripts/script", "addScript", 0);
        digester.addCallMethod("components/component/styles/style", "addStyle", 0);
        digester.addSetNext("components/component", "addComponent");

        return digester;
    }
}
