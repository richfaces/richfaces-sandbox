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
package org.richfaces.sandbox.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.ComponentIterators;
import org.richfaces.component.SwitchType;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;

/**
 * <p>The &lt;rich:accordionItem&gt; component is a panel for use with the &lt;rich:accordion&gt; component.
 * &lt;rich:accordionItem&gt; components can be added dynamically using iteration models with the &lt;c:forEach&gt;
 * tag.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), renderer = @JsfRenderer(type = "org.richfaces.sandbox.AccordionItemRenderer"),
        attributes = {"events-mouse-props.xml", "i18n-props.xml", "core-props.xml"})
public abstract class AbstractAccordionItem extends AbstractTogglePanelItem implements AbstractTogglePanelTitledItem,
        ClientBehaviorHolder {
    public static final String COMPONENT_TYPE = "org.richfaces.sandbox.AccordionItem";
    public static final String COMPONENT_FAMILY = "org.richfaces.sandbox.AccordionItem";

    enum Properties {
        header,
        contentClass,
        leftActiveIcon,
        leftInactiveIcon,
        leftDisabledIcon,
        rightActiveIcon,
        rightDisabledIcon,
        rightInactiveIcon,
        headerActiveClass,
        headerDisabledClass,
        headerInactiveClass,
        headerClass,
        switchType
    }

    public AbstractAccordionItem() {
        setRendererType("org.richfaces.sandbox.AccordionItemRenderer");
    }

    @Override
    public AbstractAccordion getParentPanel() {
        return ComponentIterators.getParent(this, AbstractAccordion.class);
    }

    public AbstractAccordion getAccordion() {
        return getParentPanel();
    }

    public UIComponent getHeaderFacet(Enum<?> state) {
        return AbstractTab.getHeaderFacet(this, state);
    }

    // ------------------------------------------------ Component Attributes

    /**
     * <p>
     * Provides the text on the panel header. The panel header is all that is visible when the accordion item is collapsed.
     * </p>
     * <p>
     * Alternatively the header facet could be used in place of the header attribute.
     * This would allow for additional styles and custom content to be applied to the tab.
     * </p>
     */
    @Attribute(generate = false)
    public String getHeader() {
        return (String) getStateHelper().eval(Properties.header, getName());
    }

    public void setHeader(String header) {
        getStateHelper().put(Properties.header, header);
    }

    /**
     * The icon displayed on the left of the panel header when the panel is active
     */
    @Attribute(generate = false)
    public String getLeftActiveIcon() {
        return (String) getStateHelper().eval(Properties.leftActiveIcon, getAccordion().getItemActiveLeftIcon());
    }

    public void setLeftActiveIcon(String leftActiveIcon) {
        getStateHelper().put(Properties.leftActiveIcon, leftActiveIcon);
    }

    /**
     * The icon displayed on the left of the panel header when the panel is not active
     */
    @Attribute(generate = false)
    public String getLeftDisabledIcon() {
        return (String) getStateHelper().eval(Properties.leftDisabledIcon, getAccordion().getItemDisabledLeftIcon());
    }

    public void setLeftDisabledIcon(String leftDisabledIcon) {
        getStateHelper().put(Properties.leftDisabledIcon, leftDisabledIcon);
    }

    /**
     * The icon displayed on the left of the panel header when the panel is disabled
     */
    @Attribute(generate = false)
    public String getLeftInactiveIcon() {
        return (String) getStateHelper().eval(Properties.leftInactiveIcon, getAccordion().getItemInactiveLeftIcon());
    }

    public void setLeftInactiveIcon(String leftInactiveIcon) {
        getStateHelper().put(Properties.leftInactiveIcon, leftInactiveIcon);
    }

    /**
     * The icon displayed on the right of the panel header when the panel is active
     */
    @Attribute(generate = false)
    public String getRightActiveIcon() {
        return (String) getStateHelper().eval(Properties.rightActiveIcon, getAccordion().getItemActiveRightIcon());
    }

    public void setRightActiveIcon(String rightActiveIcon) {
        getStateHelper().put(Properties.rightActiveIcon, rightActiveIcon);
    }

    /**
     * The icon displayed on the right of the panel header when the panel is disabled
     */
    @Attribute(generate = false)
    public String getRightDisabledIcon() {
        return (String) getStateHelper().eval(Properties.rightDisabledIcon, getAccordion().getItemDisabledRightIcon());
    }

    public void setRightDisabledIcon(String rightDisabledIcon) {
        getStateHelper().put(Properties.rightDisabledIcon, rightDisabledIcon);
    }

    /**
     * The icon displayed on the right of the panel header when the panel is not active
     */
    @Attribute(generate = false)
    public String getRightInactiveIcon() {
        return (String) getStateHelper().eval(Properties.rightInactiveIcon, getAccordion().getItemInactiveRightIcon());
    }

    public void setRightInactiveIcon(String inactiveRightIcon) {
        getStateHelper().put(Properties.rightInactiveIcon, inactiveRightIcon);
    }

    /**
     * The CSS class applied to the header when this panel is active
     */
    @Attribute(generate = false)
    public String getHeaderActiveClass() {
        return (String) getStateHelper().eval(Properties.headerActiveClass, getAccordion().getItemActiveHeaderClass());
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }

    /**
     * The CSS class applied to the header when this panel is disabled
     */
    @Attribute(generate = false)
    public String getHeaderDisabledClass() {
        return (String) getStateHelper().eval(Properties.headerDisabledClass, getAccordion().getItemDisabledHeaderClass());
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    /**
     * The CSS class applied to the header when this panel is inactive
     */
    @Attribute(generate = false)
    public String getHeaderInactiveClass() {
        return (String) getStateHelper().eval(Properties.headerInactiveClass, getAccordion().getItemInactiveHeaderClass());
    }

    public void setHeaderInactiveClass(String headerInactiveClass) {
        getStateHelper().put(Properties.headerInactiveClass, headerInactiveClass);
    }

    /**
     * The CSS class applied to the header
     */
    @Attribute(defaultValue = "getAccordion().getItemHeaderClass()")
    public String getHeaderClass() {
        return (String) getStateHelper().eval(Properties.headerClass, getAccordion().getItemHeaderClass());
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(Properties.headerClass, headerClass);
    }

    /**
     * The CSS class applied to the panel content
     */
    @Attribute(defaultValue = "getAccordion().getItemContentClass()")
    public String getContentClass() {
        return (String) getStateHelper().eval(Properties.contentClass, getAccordion().getItemContentClass());
    }

    public void setContentClass(String contentClass) {
        getStateHelper().put(Properties.contentClass, contentClass);
    }

    /**
     * The switch mode when a panel is activated.  One of: "client", "server", "ajax". Default: "ajax"
     */
    @Attribute(generate = false)
    public SwitchType getSwitchType() {
        SwitchType switchType = (SwitchType) getStateHelper().eval(Properties.switchType);
        if (switchType == null) {
            switchType = getParentPanel().getSwitchType();
        }
        if (switchType == null) {
            switchType = SwitchType.DEFAULT;
        }
        return switchType;
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(Properties.switchType, switchType);
    }
}
