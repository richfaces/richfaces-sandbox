/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.sandbox.component.behavior;

import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfBehavior;
import org.richfaces.cdk.annotations.JsfBehaviorRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.sandbox.component.AbstractTogglePanel;
import org.richfaces.component.ComponentIterators;
import org.richfaces.component.behavior.ClientBehavior;
import org.richfaces.renderkit.util.RendererUtils;

/**
 * <p> The &lt;rich:toggleControl&gt; behavior can be attached to any interface component, whether inside or outside the
 * controlled panel itself. It works with a &lt;rich:togglePanel&gt; component to switch between different
 * &lt;rich:togglePanelItem&gt; components. </p>
 *
 * @author akolonitsky
 */
@JsfBehavior(id = "org.richfaces.component.behavior.ToggleControl", tag = @Tag(name = "toggleControl", handler = "org.richfaces.view.facelets.html.CustomBehaviorHandler", type = TagType.Facelets), renderer = @JsfBehaviorRenderer(type = "org.richfaces.component.behavior.ToggleControl"))
public class ToggleControl extends ClientBehavior {
    public static final String BEHAVIOR_ID = "org.richfaces.component.behavior.ToggleControl";
    private static final RendererUtils RENDERER_UTILS = RendererUtils.getInstance();

    private enum PropertyKeys {
        event,
        targetItem,
        targetPanel,
        disableDefault
    }

    /**
     * The event on which to toggle the target panel
     */
    @Attribute
    public String getEvent() {
        return (String) getStateHelper().eval(PropertyKeys.event);
    }

    public void setEvent(String eventName) {
        getStateHelper().eval(PropertyKeys.event, eventName);
    }

    /**
     * The next &lt;rich:togglePanelItem&gt; to switch to
     */
    @Attribute
    public String getTargetItem() {
        return (String) getStateHelper().eval(PropertyKeys.targetItem, AbstractTogglePanel.META_NAME_NEXT);
    }

    public void setTargetItem(String target) {
        getStateHelper().put(PropertyKeys.targetItem, target);
    }

    /**
     * The &lt;rich:togglePanel&gt; to switch when this &lt;rich:toggleControl&gt; is not a child of a &lt;rich:togglePanel&gt;
     */
    @Attribute
    public String getTargetPanel() {
        return (String) getStateHelper().eval(PropertyKeys.targetPanel);
    }

    public void setTargetPanel(String selector) {
        getStateHelper().put(PropertyKeys.targetPanel, selector);
    }

    /**
     * If "true", disable the default action of the parent component
     */
    @Attribute
    public void setDisableDefault(Boolean disableDefault) {
        getStateHelper().put(PropertyKeys.disableDefault, disableDefault);
    }

    public Boolean getDisableDefault() {
        return Boolean.valueOf(String.valueOf(getStateHelper().eval(PropertyKeys.disableDefault, true)));
    }

    public String getPanelId(ClientBehaviorContext behaviorContext) throws FacesException {
        return getPanel(behaviorContext.getComponent()).getClientId(behaviorContext.getFacesContext());
    }

    public AbstractTogglePanel getPanel(UIComponent comp) throws FacesException {
        String target = this.getTargetPanel();

        if (target != null) {

            UIComponent targetComponent = RENDERER_UTILS.findComponentFor(comp, target);

            if (null != targetComponent) {
                return (AbstractTogglePanel) targetComponent;
            } else {
                throw new FacesException("Parent panel for control (id=" + comp.getClientId(getFacesContext())
                    + ") has not been found.");
            }
        } else {
            return getEnclosedPanel(comp);
        }
    }

    public static AbstractTogglePanel getEnclosedPanel(UIComponent comp) {
        if (comp == null) {
            return null;
        }

        AbstractTogglePanel panel = ComponentIterators.getParent(comp, AbstractTogglePanel.class);
        if (panel == null) {
            throw new FacesException("Parent panel for control (id=" + comp.getClientId(FacesContext.getCurrentInstance())
                + ") has not been found.");
        }

        return panel;
    }

    @Override
    public String getRendererType() {
        return BEHAVIOR_ID;
    }

    @Override
    public void setLiteralAttribute(String name, Object value) {
        if (compare(PropertyKeys.targetItem, name)) {
            setTargetItem((String) value);
        } else if (compare(PropertyKeys.targetPanel, name)) {
            setTargetPanel((String) value);
        } else if (compare(PropertyKeys.disableDefault, name)) {
            ExpressionFactory expFactory = getFacesContext().getApplication().getExpressionFactory();
            setDisableDefault((Boolean) expFactory.coerceToType(value, Boolean.class));
        }
    }
}
