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

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.renderkit.AjaxComponentRendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractNotify;
import org.richfaces.component.AbstractNotifyStack;

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

@JsfRenderer(family = AbstractNotify.COMPONENT_FAMILY, type = NotifyRenderer.RENDERER_TYPE)
@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js", target = "head"),
    @ResourceDependency(name = "richfaces.js", target = "head"),
    @ResourceDependency(name = "jquery.pnotify.js", target = "head"),
    @ResourceDependency(name = "richfaces.notify.js", target = "head"),
    @ResourceDependency(name = "notify.ecss", target = "head")})
public class NotifyRenderer extends AjaxComponentRendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.NotifyRenderer";
    private static final Map<String, Object> DEFAULTS;

    static {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("styleClass", "");
        defaults.put("nonblocking", false);
        defaults.put("nonblockingOpacity", AbstractNotify.DEFAULT_NONBLOCKING_OPACITY);
        defaults.put("showHistory", true);
        defaults.put("animationSpeed", "slow");
        defaults.put("opacity", 1);
        defaults.put("showShadow", false);
        defaults.put("showCloseButton", true);
        defaults.put("appearAnimation", "fade");
        defaults.put("hideAnimation", "fade");
        defaults.put("sticky", false);
        defaults.put("stayTime", 8000);
        defaults.put("delay", 0);
        DEFAULTS = Collections.unmodifiableMap(defaults);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractNotify)) {
            return;
        }
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute(HTML.ID_ATTRIBUTE, getUtils().clientId(context, component), "type");
        writer.startElement(HTML.SCRIPT_ELEM, null);
        writer.writeAttribute(HTML.TYPE_ATTR, "text/javascript", "type");
        writer.writeText(new JSFunction("RichFaces.Notify", getOptions(context, (AbstractNotify) component)), null);
        writer.writeText(";", null);
        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }

    protected Map<String, Object> getOptions(FacesContext context, AbstractNotify notify) throws IOException {
        /**
         * Include only attributes that are actually set.
         */
        Map<String, Object> options = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("title", notify.getTitle(), options);
        addOptionIfSetAndNotDefault("text", notify.getText(), options);
        addOptionIfSetAndNotDefault("sticky", notify.isSticky(), options);
        addOptionIfSetAndNotDefault("stayTime", notify.getStayTime(), options);
        Map<String, Object> animationOptions = new HashMap<String, Object>();
        addOptionIfSetAndNotDefault("appearAnimation", notify.getAppearAnimation(), animationOptions);
        addOptionIfSetAndNotDefault("hideAnimation", notify.getHideAnimation(), animationOptions);
        addOptionIfSetAndNotDefault("animation", animationOptions, options);
        addOptionIfSetAndNotDefault("animationSpeed", notify.getAnimationSpeed(), options);
        addOptionIfSetAndNotDefault("nonblocking", notify.isNonblocking(), options);
        addOptionIfSetAndNotDefault("nonblockingOpacity", notify.getNonblockingOpacity(), options);
        addOptionIfSetAndNotDefault("showHistory", notify.isShowHistory(), options);
        addOptionIfSetAndNotDefault("showShadow", notify.isShowShadow(), options);
        addOptionIfSetAndNotDefault("showCloseButton", notify.isShowCloseButton(), options);
        AbstractNotifyStack stack = getStackComponent(context, notify);
        if (stack != null) {
            addOptionIfSetAndNotDefault("stack", getUtils().clientId(context, stack), options);
        }
        String styleClass = notify.getStyleClass();
        if (styleClass == null) {
            styleClass = "";
        }
        addOptionIfSetAndNotDefault("styleClass", getStackStyleClass(context, notify) + " " + styleClass, options);
        addOptionIfSetAndNotDefault("delay", notify.getDelay(), options);
        return options;
    }

    protected String getStackStyleClass(FacesContext context, AbstractNotify notify) {
        AbstractNotifyStack stack = getStackComponent(context, notify);
        return stack == null ? "" : stack.getStyleClass();
    }

    protected void addOptionIfSetAndNotDefault(String optionName, Object value, Map<String, Object> options) {
        if (value != null && !"".equals(value)
            && !value.equals(DEFAULTS.get(optionName))
            && !(value instanceof Collection && ((Collection) value).size() == 0)
            && !(value instanceof Map && ((Map) value).size() == 0)) {
            options.put(optionName, value);
        }
    }

    protected AbstractNotifyStack getStackComponent(FacesContext context, AbstractNotify notify) {
        String stackId = notify.getStack();
        if (stackId == null) {
            UIComponent parent = notify.getParent();
            while (parent != null && !(parent instanceof AbstractNotifyStack)) {
                parent = parent.getParent();
            }
            return (AbstractNotifyStack) parent;
        } else {
            UIComponent componentFor = getUtils().findComponentFor(context.getViewRoot(), stackId);
            if (componentFor instanceof AbstractNotifyStack) {
                return (AbstractNotifyStack) componentFor;
            } else {
                return null;
            }
        }
    }
}
