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



package org.richfaces.cdk.rd.handler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import java.net.URL;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.logging.Log;

import org.codehaus.plexus.util.SelectorUtils;

import org.richfaces.cdk.rd.Component;
import org.richfaces.cdk.rd.Components;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Anton Belevich
 *
 */
public class ComponentsHandler extends DefaultHandler {
    private static final String DEFAULT_DTD_PATH = "org/richfaces/default.dtd";
    private String[] INCLUDE_ALL_PATTERN = {"**"};
    private Map<String, Set<String>> processedComponents = new HashMap<String, Set<String>>();
    private Set<String> scripts = new LinkedHashSet<String>();
    private Set<String> styles = new LinkedHashSet<String>();
    private String[] componentExcludes;
    private String[] componentIncludes;
    private Map<String, Components> components;
    private String defaultDtdContent;
    protected Log log;
    private String[] namespaceExcludes;
    private String[] namespaceIncludes;
    private String[] scriptExcludes;
    private String[] scriptIncludes;
    private String[] styleExcludes;
    private String[] styleIncludes;

    public ComponentsHandler(Log log) {
        this.log = log;
    }

    private Set<String> getProcessedComponentsSet(String uri) {
        Set<String> result = processedComponents.get(uri);

        if (result == null) {
            result = new HashSet<String>();
            processedComponents.put(uri, result);
        }

        return result;
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if (components != null) {
            Components library = components.get(uri);

            if (library != null) {
                Set<String> processedComponentsSet = getProcessedComponentsSet(uri);

                if (processedComponentsSet.add(localName)) {
                    if (namespaceIncludes == null) {
                        namespaceIncludes = INCLUDE_ALL_PATTERN;

                        if (log.isDebugEnabled()) {
                            log.debug("use default include patterns for namespaces");
                        }
                    }

                    if (doMatch(namespaceIncludes, uri, true)) {
                        if (!doMatch(namespaceExcludes, uri, true)) {
                            List<Component> components = library.getComponents();

                            if (componentIncludes == null) {
                                componentIncludes = INCLUDE_ALL_PATTERN;

                                if (log.isDebugEnabled()) {
                                    log.debug("use default include patterns for components");
                                }
                            }

                            if (doMatch(componentIncludes, localName, false)) {
                                if (!doMatch(componentExcludes, localName, false)) {
                                    for (Component component : components) {
                                        String componentName = component.getComponentName();

                                        if (localName.equals(componentName)) {
                                            log.info("process component: " + componentName);
                                            collectScripts(component);
                                            collectStyles(component);

                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (log.isDebugEnabled()) {
                            log.debug("components from namespace " + uri + " are in excluded list");
                        }
                    } else if (log.isDebugEnabled()) {
                        log.debug("components from namespace " + uri + " aren't in include list");
                    }
                }
            }
        }
    }

    private void collectStyles(Component component) {
        if (styleIncludes == null) {
            styleIncludes = INCLUDE_ALL_PATTERN;
        }

        if (log.isDebugEnabled()) {
            log.debug("start collect styles from component:  " + component.getComponentName());
        }

        for (String style : component.getStyles()) {
            if (doMatch(styleIncludes, style, true)) {
                if (!doMatch(styleExcludes, style, true)) {
                    this.styles.add(style);
                    log.info("style " + style + " is collected");
                } else {
                    log.info("style files are in excluded list");

                    for (String styleExclude : styleExcludes) {
                        log.info(styleExclude);
                    }
                }
            } else {
                log.info("style files are not in included list");

                for (String styleInclude : styleIncludes) {
                    log.info(styleInclude);
                }
            }
        }
    }

    private boolean doMatch(String[] patterns, String str, boolean matchPath) {
        boolean allow = false;

        if (patterns != null) {
            for (String excludePattern : patterns) {
                if (matchPath) {
                    allow = SelectorUtils.matchPath(excludePattern, str);
                } else {
                    allow = SelectorUtils.match(excludePattern, str);
                }

                if (allow) {
                    break;
                }
            }
        }

        return allow;
    }

    private void collectScripts(Component component) {
        if (scriptIncludes == null) {
            scriptIncludes = INCLUDE_ALL_PATTERN;
        }

        if (log.isDebugEnabled()) {
            log.debug("start collect scripts from component:  " + component.getComponentName());
        }

        for (String script : component.getScripts()) {
            if (doMatch(scriptIncludes, script, true)) {
                if (!doMatch(scriptExcludes, script, true)) {
                    this.scripts.add(script);
                    log.info("script " + script + " is collected");
                } else {
                    log.info("script files are in excluded list");

                    for (String styleExclude : scriptExcludes) {
                        log.info(styleExclude);
                    }
                }
            } else {
                log.info("script files are not in included list");

                for (String styleInclude : scriptIncludes) {
                    log.info(styleInclude);
                }
            }
        }
    }

    public Set<String> getStyles() {
        return this.styles;
    }

    public Set<String> getScripts() {
        return this.scripts;
    }

    public Map<String, Components> getComponents() {
        return components;
    }

    public void setComponents(Map<String, Components> components) {
        this.components = components;
    }

    public String[] getNamespaceExcludes() {
        return namespaceExcludes;
    }

    public void setNamespaceExcludes(String[] namespaceExcludes) {
        this.namespaceExcludes = namespaceExcludes;
    }

    public String[] getComponentExcludes() {
        return componentExcludes;
    }

    public void setComponentExcludes(String[] componentExcludes) {
        this.componentExcludes = componentExcludes;
    }

    public String[] getScriptExcludes() {
        return scriptExcludes;
    }

    public void setScriptExcludes(String[] scriptExcludes) {
        this.scriptExcludes = scriptExcludes;
    }

    public String[] getStyleExcludes() {
        return styleExcludes;
    }

    public void setStyleExcludes(String[] styleExcludes) {
        this.styleExcludes = styleExcludes;
    }

    public String[] getScriptIncludes() {
        return scriptIncludes;
    }

    public void setScriptIncludes(String[] scriptIncludes) {
        this.scriptIncludes = scriptIncludes;
    }

    public String[] getStyleIncludes() {
        return styleIncludes;
    }

    public void setStyleIncludes(String[] styleIncludes) {
        this.styleIncludes = styleIncludes;
    }

    public String[] getNamespaceIncludes() {
        return namespaceIncludes;
    }

    public void setNamespaceIncludes(String[] namespaceIncludes) {
        this.namespaceIncludes = namespaceIncludes;
    }

    public String[] getComponentIncludes() {
        return componentIncludes;
    }

    public void setComponentIncludes(String[] componentIncludes) {
        this.componentIncludes = componentIncludes;
    }

    private String readDtdContent(String path) throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);

        if (url == null) {
            url = getClass().getClassLoader().getResource(path);
        }

        Reader reader = new InputStreamReader(url.openStream());
        String dtdContent;

        try {
            StringBuilder builder = new StringBuilder(32);
            char[] cs = new char[512];
            int read;

            while ((read = reader.read(cs)) != -1) {
                builder.append(cs, 0, read);
            }

            dtdContent = builder.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }

        return dtdContent;
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
        if (defaultDtdContent == null) {
            defaultDtdContent = readDtdContent(DEFAULT_DTD_PATH);
        }

        return new InputSource(new StringReader(defaultDtdContent));
    }
}
