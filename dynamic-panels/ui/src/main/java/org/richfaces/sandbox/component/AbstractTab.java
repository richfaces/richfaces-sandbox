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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Facet;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.sandbox.renderkit.html.DivPanelRenderer;
import org.richfaces.ui.common.AbstractActionComponent;
import org.richfaces.ui.common.ComponentIterators;
import org.richfaces.ui.common.SwitchType;

/**
 * <p>The &lt;rich:tab&gt; component represents an individual tab inside a &lt;rich:tabPanel&gt; component, including
 * the tab's content. Clicking on the tab header will bring its corresponding content to the front of other tabs.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets), facets = { @Facet(name = "header", generate = false) }, renderer = @JsfRenderer(type = "org.richfaces.sandbox.TabRenderer"), attributes = {
        "core-props.xml", "ajax-props.xml", "bypass-props.xml", "events-mouse-props.xml", "i18n-props.xml" })
public abstract class AbstractTab extends AbstractActionComponent implements AbstractTogglePanelTitledItem, AjaxProps,
        ClientBehaviorHolder {
    public static final String COMPONENT_TYPE = "org.richfaces.sandbox.Tab";
    public static final String COMPONENT_FAMILY = "org.richfaces.sandbox.Tab";

    public AbstractTab() {
        setRendererType("org.richfaces.sandbox.TabRenderer");
    }

    // ------------------------------------------------ Html Attributes
    enum Properties {
        headerDisabledClass, headerInactiveClass, headerClass, contentClass, execute, headerActiveClass, header, switchType
    }

    /**
     * The CSS class applied to the header when this panel is active
     */
    @Attribute(generate = false)
    public String getHeaderActiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerActiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabActiveHeaderClass();
    }

    public void setHeaderActiveClass(String headerActiveClass) {
        getStateHelper().put(Properties.headerActiveClass, headerActiveClass);
    }

    /**
     * The CSS class applied to the header when this panel is disabled
     */
    @Attribute(generate = false)
    public String getHeaderDisabledClass() {
        String value = (String) getStateHelper().eval(Properties.headerDisabledClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabDisabledHeaderClass();
    }

    public void setHeaderDisabledClass(String headerDisabledClass) {
        getStateHelper().put(Properties.headerDisabledClass, headerDisabledClass);
    }

    /**
     * The CSS class applied to the header when this panel is inactive
     */
    @Attribute(generate = false)
    public String getHeaderInactiveClass() {
        String value = (String) getStateHelper().eval(Properties.headerInactiveClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabInactiveHeaderClass();
    }

    public void setHeaderInactiveClass(String headerInactiveClass) {
        getStateHelper().put(Properties.headerInactiveClass, headerInactiveClass);
    }

    /**
     * The CSS class applied to the header
     */
    @Attribute(generate = false)
    public String getHeaderClass() {
        String value = (String) getStateHelper().eval(Properties.headerClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabHeaderClass();
    }

    public void setHeaderClass(String headerClass) {
        getStateHelper().put(Properties.headerClass, headerClass);
    }

    /**
     * The CSS class applied to the panel content
     */
    @Attribute(generate = false)
    public String getContentClass() {
        String value = (String) getStateHelper().eval(Properties.contentClass);
        if (value != null) {
            return value;
        }

        return getTabPanel().getTabContentClass();
    }

    public void setContentClass(String contentClass) {
        getStateHelper().put(Properties.contentClass, contentClass);
    }

    @Attribute(generate = false)
    public Object getExecute() {
        Object execute = getStateHelper().eval(Properties.execute);
        if (execute == null) {
            execute = "";
        }
        return execute + " " + getTabPanel().getId();
    }

    public void setExecute(Object execute) {
        getStateHelper().put(Properties.execute, execute);
    }

    // ///////////////////////////////////////////////////////////////////////

    public UIComponent getHeaderFacet(Enum<?> state) {
        return getHeaderFacet(this, state);
    }

    public static UIComponent getHeaderFacet(UIComponent component, Enum<?> state) {
        UIComponent headerFacet = null;
        if (state != null) {
            headerFacet = component.getFacet("header" + DivPanelRenderer.capitalize(state.toString()));
        }

        if (headerFacet == null) {
            headerFacet = component.getFacet("header");
        }
        return headerFacet;
    }

    // ------------------------------------------------ Component Attributes

    /**
     * The header label of the tab
     */
    @Attribute(generate = false)
    public String getHeader() {
        return (String) getStateHelper().eval(Properties.header, getName());
    }

    public void setHeader(String header) {
        getStateHelper().put(Properties.header, header);
    }

    // ------------------------------------------------ AbstractTogglePanelItemInterface

    public AbstractTabPanel getParentPanel() {
        return ComponentIterators.getParent(this, AbstractTabPanel.class);
    }

    public AbstractTabPanel getTabPanel() {
        return getParentPanel();
    }

    public boolean isActive() {
        AbstractTabPanel tabPanel = getTabPanel();
        if(tabPanel==null) {
            throw new FacesException("Tab can be nested only inside TabPanel");
        }
        return tabPanel.isActiveItem(this);
    }

    public boolean shouldProcess() {
        return isActive() || getSwitchType() == SwitchType.client;
    }

    /**
     * The name of the tab, used for identifying and manipulating the active panel
     */
    @Attribute(generate = false)
    public String getName() {
        return (String) getStateHelper().eval(AbstractTogglePanelItem.NAME, getId());
    }

    public void setName(String name) {
        getStateHelper().put(AbstractTogglePanelItem.NAME, name);
    }

    public String toString() {
        return "TogglePanelItem {name: " + getName() + ", switchType: " + getSwitchType() + '}';
    }

    /**
     * The switch type for this toggle panel: client, ajax (default), server
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

    @Override
    public Renderer getRenderer(FacesContext context) {
        return super.getRenderer(context);
    }
}
