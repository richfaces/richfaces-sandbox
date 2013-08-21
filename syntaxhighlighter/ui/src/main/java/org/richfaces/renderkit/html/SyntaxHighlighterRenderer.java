/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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

package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.application.Application;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PreRenderComponentEvent;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractSyntaxHighlighter;
import org.richfaces.javascript.JSObject;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.ui.common.HtmlConstants;

@ListenerFor(systemEventClass = PostAddToViewEvent.class, sourceClass = AbstractSyntaxHighlighter.class)
@JsfRenderer(family = AbstractSyntaxHighlighter.COMPONENT_FAMILY, type = SyntaxHighlighterRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
        @ResourceDependency(name = "richfaces.js", target = "head"), @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
        @ResourceDependency(name = "syntaxhighlighter/js/XRegExp.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "syntaxhighlighter/js/shCore.js", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "syntaxhighlighter/styles/shCore.css", library = "org.richfaces", target = "head"),
        @ResourceDependency(name = "richfaces.syntaxhighlighter.js", target = "head")})
public class SyntaxHighlighterRenderer extends RendererBase implements ComponentSystemEventListener {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.SyntaxhighlighterRenderer";

    private static final Map<String, Object> DEFAULTS;

    private static final String RESOURCE_LIBRARY = "org.richfaces";

    private static final Map<String, String> SUPPORTED_LANGUAGES;

    private static final Map<String, Collection<String>> SUPPORTED_THEMES;

// -------------------------- STATIC METHODS --------------------------

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        DEFAULTS = Collections.unmodifiableMap(defaults);
        Map<String, String> supportedLanguages = new HashMap<String, String>();
        supportedLanguages.put("applescript", "syntaxhighlighter/js/shBrushAppleScript.js");
        supportedLanguages.put("as3", "syntaxhighlighter/js/shBrushAS3.js");
        supportedLanguages.put("bash", "syntaxhighlighter/js/shBrushBash.js");
        supportedLanguages.put("coldfusion", "syntaxhighlighter/js/shBrushColdFusion.js");
        supportedLanguages.put("cpp", "syntaxhighlighter/js/shBrushCpp.js");
        supportedLanguages.put("csharp", "syntaxhighlighter/js/shBrushCSharp.js");
        supportedLanguages.put("css", "syntaxhighlighter/js/shBrushCss.js");
        supportedLanguages.put("delphi", "syntaxhighlighter/js/shBrushDelphi.js");
        supportedLanguages.put("diff", "syntaxhighlighter/js/shBrushDiff.js");
        supportedLanguages.put("erlang", "syntaxhighlighter/js/shBrushErlang.js");
        supportedLanguages.put("groovy", "syntaxhighlighter/js/shBrushGroovy.js");
        supportedLanguages.put("java", "syntaxhighlighter/js/shBrushJava.js");
        supportedLanguages.put("javafx", "syntaxhighlighter/js/shBrushJavaFX.js");
        supportedLanguages.put("jscript", "syntaxhighlighter/js/shBrushJScript.js");
        supportedLanguages.put("perl", "syntaxhighlighter/js/shBrushPerl.js");
        supportedLanguages.put("php", "syntaxhighlighter/js/shBrushPhp.js");
        supportedLanguages.put("plain", "syntaxhighlighter/js/shBrushPlain.js");
        supportedLanguages.put("powershell", "syntaxhighlighter/js/shBrushPowerShell.js");
        supportedLanguages.put("python", "syntaxhighlighter/js/shBrushPython.js");
        supportedLanguages.put("ruby", "syntaxhighlighter/js/shBrushRuby.js");
        supportedLanguages.put("sass", "syntaxhighlighter/js/shBrushSass.js");
        supportedLanguages.put("scala", "syntaxhighlighter/js/shBrushScala.js");
        supportedLanguages.put("sql", "syntaxhighlighter/js/shBrushSql.js");
        supportedLanguages.put("vb", "syntaxhighlighter/js/shBrushVb.js");
        supportedLanguages.put("xml", "syntaxhighlighter/js/shBrushXml.js");
        SUPPORTED_LANGUAGES = Collections.unmodifiableMap(supportedLanguages);
        Map<String, Collection<String>> supportedThemes = new HashMap<String, Collection<String>>();
        supportedThemes.put("default", Arrays.asList("syntaxhighlighter/styles/shCoreDefault.css", "syntaxhighlighter/styles/shThemeDefault.css"));
        supportedThemes.put("django", Arrays.asList("syntaxhighlighter/styles/shCoreDjango.css", "syntaxhighlighter/styles/shThemeDjango.css"));
        supportedThemes.put("eclipse", Arrays.asList("syntaxhighlighter/styles/shCoreEclipse.css", "syntaxhighlighter/styles/shThemeEclipse.css"));
        supportedThemes.put("emacs", Arrays.asList("syntaxhighlighter/styles/shCoreEmacs.css", "syntaxhighlighter/styles/shThemeEmacs.css"));
        supportedThemes.put("fadetogrey", Arrays.asList("syntaxhighlighter/styles/shCoreFadeToGrey.css", "syntaxhighlighter/styles/shThemeFadeToGrey.css"));
        supportedThemes.put("mdultra", Arrays.asList("syntaxhighlighter/styles/shCoreMDUltra.css", "syntaxhighlighter/styles/shThemeMDUltra.css"));
        supportedThemes.put("midnight", Arrays.asList("syntaxhighlighter/styles/shCoreMidnight.css", "syntaxhighlighter/styles/shThemeMidnight.css"));
        supportedThemes.put("rdark", Arrays.asList("syntaxhighlighter/styles/shCoreRDark.css", "syntaxhighlighter/styles/shThemeRDark.css"));
        SUPPORTED_THEMES = Collections.unmodifiableMap(supportedThemes);
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ComponentSystemEventListener ---------------------

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        UIComponent component = event.getComponent();
        AbstractSyntaxHighlighter syntaxhighlighter = (AbstractSyntaxHighlighter) component;
        if (event instanceof PostAddToViewEvent) {
            syntaxhighlighter.subscribeToPreRenderViewEvent();
        } else if (event instanceof PreRenderComponentEvent) {
            FacesContext context = FacesContext.getCurrentInstance();
            String language = syntaxhighlighter.getLanguage();
            if (language != null) {
                language = language.toLowerCase();
            }
            if (SUPPORTED_LANGUAGES.containsKey(language)) {
                String resourceName = SUPPORTED_LANGUAGES.get(language);
                context.getViewRoot().addComponentResource(context, createComponentResource(context, resourceName));
            }
            String theme = syntaxhighlighter.getTheme();
            if (theme != null) {
                theme = theme.toLowerCase();
            }
            if (SUPPORTED_THEMES.containsKey(theme)) {
                Collection<String> resources = SUPPORTED_THEMES.get(theme);
                for (String resourceName : resources) {
                    context.getViewRoot().addComponentResource(context, createComponentResource(context, resourceName));
                }
            }
        }
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractSyntaxHighlighter)) {
            return;
        }
        AbstractSyntaxHighlighter syntaxhighlighter = (AbstractSyntaxHighlighter) component;
        String clientId = component.getClientId(context);
        writer.endElement(HtmlConstants.DIV_ELEM);
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions(syntaxhighlighter);
        writer.writeText(new JSObject("RichFaces.ui.SyntaxHighlighter", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(optionName)) && !(value instanceof Collection && ((Collection) value).size() == 0)
                && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }

    private UIComponent createComponentResource(FacesContext context, String resourceName) {
        Application application = context.getApplication();
        UIComponent resourceComponent = application.createComponent(UIOutput.COMPONENT_TYPE);
        resourceComponent.setRendererType(application.getResourceHandler().getRendererTypeForResourceName(resourceName));
        Map<String, Object> attrs = resourceComponent.getAttributes();
        attrs.put("name", resourceName);
        attrs.put("library", RESOURCE_LIBRARY);
        attrs.put("target", "head");
        return resourceComponent;
    }

    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractSyntaxHighlighter)) {
            return;
        }
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        String clientId = component.getClientId(context);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, HtmlConstants.ID_ATTRIBUTE);

        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, concatClasses("rf-syn", component.getAttributes().get(HtmlConstants.STYLE_CLASS_ATTR)),
                HtmlConstants.STYLE_CLASS_ATTR);
        getUtils().encodeAttributesFromArray(context, component, HtmlConstants.PASS_THRU_STYLES);

        writer.startElement(HtmlConstants.DIV_ELEM, null);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "rf-syn-cd", HtmlConstants.STYLE_CLASS_ATTR);
        writer.write("\n");
    }

    protected Map<String, Object> getOptions(AbstractSyntaxHighlighter syntaxhighlighter) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("language", syntaxhighlighter.getLanguage(), options);
        addOptionIfSetAndNotDefault("autoLinks", syntaxhighlighter.isAutoLinks(), options);
        addOptionIfSetAndNotDefault("bloggerMode", syntaxhighlighter.isBloggerMode(), options);
        addOptionIfSetAndNotDefault("collapsed", syntaxhighlighter.isCollapsed(), options);
        addOptionIfSetAndNotDefault("gutter", syntaxhighlighter.isGutter(), options);
        addOptionIfSetAndNotDefault("htmlScript", syntaxhighlighter.isHtmlScript(), options);
        addOptionIfSetAndNotDefault("smartTabs", syntaxhighlighter.isSmartTabs(), options);
        addOptionIfSetAndNotDefault("stripBrs", syntaxhighlighter.isStripBrs(), options);
        addOptionIfSetAndNotDefault("toolbar", syntaxhighlighter.isToolbar(), options);
        addOptionIfSetAndNotDefault("firstLine", syntaxhighlighter.getFirstLine(), options);
        addOptionIfSetAndNotDefault("tabSize", syntaxhighlighter.getTabSize(), options);
        Map<String, Object> strings = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("copyToClipboardConfirmationLabel", syntaxhighlighter.getCopyToClipboardConfirmationLabel(), strings);
        addOptionIfSetAndNotDefault("copyToClipboardLabel", syntaxhighlighter.getCopyToClipboardLabel(), strings);
        addOptionIfSetAndNotDefault("expandSourceLabel", syntaxhighlighter.getExpandSourceLabel(), strings);
        addOptionIfSetAndNotDefault("helpLabel", syntaxhighlighter.getHelpLabel(), strings);
        addOptionIfSetAndNotDefault("highlight", getRenderableHighlightValue(syntaxhighlighter), strings);
        addOptionIfSetAndNotDefault("printLabel", syntaxhighlighter.getPrintLabel(), strings);
        addOptionIfSetAndNotDefault("viewSourceLabel", syntaxhighlighter.getViewSourceLabel(), strings);
        addOptionIfSetAndNotDefault("strings", strings, options);
        return options;
    }

    private Object getRenderableHighlightValue(AbstractSyntaxHighlighter syntaxhighlighter) {
        final Object highlight = syntaxhighlighter.getHighlight();
        final List<Integer> list = new ArrayList<Integer>();
        if (highlight instanceof Collection) {
            for (Object element : ((Collection) highlight)) {
                list.add(Integer.parseInt(element.toString()));
            }
        } else if (highlight != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(highlight.toString(), ",");
            while (stringTokenizer.hasMoreTokens()) {
                list.add(Integer.parseInt(stringTokenizer.nextToken()));
            }
        }
        return list;
    }
}
