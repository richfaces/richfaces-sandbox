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

import org.ajax4jsf.javascript.JSObject;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractAccessKeyHelper;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.RendererBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsfRenderer(family = AbstractAccessKeyHelper.COMPONENT_FAMILY, type = AccessKeyHelperRenderer.RENDERER_TYPE)
@ResourceDependencies({@ResourceDependency(library = "javax.faces", name = "jsf.js"), @ResourceDependency(name = "jquery.js", target = "head"),
        @ResourceDependency(name = "richfaces.js", target = "head"),
        @ResourceDependency(name = "richfaces-base-component.js", target = "head"),
        @ResourceDependency(name = "jquery.accesskey.js", target = "head"),
        @ResourceDependency(name = "richfaces.accesskeyhelper.js", target = "head"),
        @ResourceDependency(name = "jquery.accesskey.css", target = "head")})
public class AccessKeyHelperRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.AccessKeyHelperRenderer";

    private static final Map<String, Object> DEFAULTS;

// -------------------------- STATIC METHODS --------------------------

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("shortcutKeyCode", 9);
        defaults.put("hideOnAnyKey", true);
        defaults.put("timeout", 6000);
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractAccessKeyHelper)) {
            return;
        }
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        String clientId = component.getClientId(context);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, clientId, "type");
        writer.startElement(HtmlConstants.SCRIPT_ELEM, null);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, "text/javascript", "type");
        final Map<String, Object> options = getOptions((AbstractAccessKeyHelper) component);
        writer.writeText(new JSObject("RichFaces.ui.AccessKeyHelper", clientId, options), null);
        writer.writeText(";", null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
        writer.endElement(HtmlConstants.DIV_ELEM);
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value) && !value.equals(DEFAULTS.get(
                optionName)) && !(value instanceof Collection && ((Collection) value).size() == 0) && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }


    protected Map<String, Object> getOptions(AbstractAccessKeyHelper accesskeyhelper) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("shortcutKeyCode", accesskeyhelper.getShortcutKeyCode(), options);
        addOptionIfSetAndNotDefault("timeout", accesskeyhelper.getTimeout(), options);
        addOptionIfSetAndNotDefault("hideOnAnyKey", accesskeyhelper.isHideOnAnyKey(), options);
        return options;
    }
}
