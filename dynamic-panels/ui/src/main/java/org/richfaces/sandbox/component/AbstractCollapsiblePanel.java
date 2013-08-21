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

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.ui.toggle.ItemChangeEvent;
import org.richfaces.ui.toggle.PanelToggleEvent;
import org.richfaces.ui.toggle.PanelToggleListener;
import org.richfaces.ui.toggle.PanelToggleSource;

/**
 * <p>
 *     The &lt;rich:collapsiblePanel&gt; component is a collapsible panel that shows or hides content when the header bar is activated.
 *     It is a simplified version of &lt;rich:togglePanel&gt; component.
 * </p>
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.sandbox.view.facelets.html.CollapsiblePanelTagHandler"),
        renderer = @JsfRenderer(type = "org.richfaces.sandbox.CollapsiblePanelRenderer"), attributes = {"core-props.xml", "events-mouse-props.xml", "i18n-props.xml"})
public abstract class AbstractCollapsiblePanel extends AbstractTogglePanel implements PanelToggleSource {
    public static final String COMPONENT_TYPE = "org.richfaces.sandbox.CollapsiblePanel";
    public static final String COMPONENT_FAMILY = "org.richfaces.sandbox.CollapsiblePanel";

    public enum States {
        expanded("exp"),
        collapsed("colps");
        private final String abbreviation;

        States(String abbreviation) {
            this.abbreviation = abbreviation;
        }

        public String abbreviation() {
            return abbreviation;
        }
    }

    protected AbstractCollapsiblePanel() {
        setRendererType("org.richfaces.sandbox.CollapsiblePanelRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getActiveItem() {
        return String.valueOf(isExpanded());
    }

    @Override
    protected boolean isActiveItem(UIComponent kid) {
        return isExpanded();
    }

    @Override
    protected boolean isActiveItem(UIComponent kid, String value) {
        return isExpanded();
    }

    /**
     * When true, the panel is expanded, when false, the panel is collapsed
     */
    @Attribute(defaultValue = "true")
    public abstract boolean isExpanded();

    public abstract void setExpanded(boolean expanded);

    @Attribute(hidden = true)
    public abstract boolean isCycledSwitching();

    @Override
    public void queueEvent(FacesEvent facesEvent) {
        PanelToggleEvent event = null;
        if ((facesEvent instanceof ItemChangeEvent) && (facesEvent.getComponent() == this)) {
            event = new PanelToggleEvent(this, Boolean.valueOf(((ItemChangeEvent) facesEvent).getNewItemName()));
            setEventPhase(event);
        }
        super.queueEvent(event != null ? event : facesEvent);
    }

    // ------------------------------------------------ Component Attributes

    /**
     * <p>
     * Provides the text on the panel header. The panel header is all that is visible when the panel is collapsed.
     * </p>
     * <p>
     * Alternatively the header facet could be used in place of the header attribute.
     * This would allow for additional styles and custom content to be applied to the tab.
     * </p>
     */
    @Attribute
    public abstract String getHeader();

    /**
     * A Server-side MethodExpression to be called when the panel is toggled
     */
    @Attribute
    public abstract MethodExpression getToggleListener();

    @Attribute(hidden = true)
    public abstract MethodExpression getItemChangeListener();

    // ------------------------------------------------ Html Attributes

    /**
     * The icon displayed on the left of the panel header when the panel is collapsed
     */
    @Attribute
    public abstract String getLeftCollapsedIcon();

    /**
     * The icon displayed on the left of the panel header when the panel is expanded
     */
    @Attribute
    public abstract String getLeftExpandedIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is collapsed
     */
    @Attribute
    public abstract String getRightCollapsedIcon();

    /**
     * The icon displayed on the right of the panel header when the panel is expanded
     */
    @Attribute
    public abstract String getRightExpandedIcon();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel content. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getBodyClass();

    /**
     * Space-separated list of CSS style class(es) to be applied to the panel header. This value must be
     * passed through as the "class" attribute on generated markup.
     */
    @Attribute
    public abstract String getHeaderClass();

    /**
     * The client-side script method to be called after the panel state is switched
     */
    @Attribute(events = @EventName("switch"))
    public abstract String getOnswitch();

    /**
     * The client-side script method to be called before the panel state is switched
     */
    @Attribute(events = @EventName("beforeswitch"))
    public abstract String getOnbeforeswitch();

    @Attribute(events = @EventName("beforeitemchange"), hidden = true)
    public abstract String getOnbeforeitemchange();

    @Attribute(events = @EventName("itemchange"), hidden = true)
    public abstract String getOnitemchange();

    // ---------------- i18ln-props.xml

    @Attribute
    public abstract String getLang();

    @Attribute
    public abstract String getDir();

    // ---------------- core-props.xml

    @Attribute
    public abstract String getTitle();

    @Attribute
    public abstract String getStyle();

    @Attribute
    public abstract String getStyleClass();

    // ---------------- events-mouse-props.xml

    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    // ------------------------------------------------ Event Processing Methods

    public void addPanelToggleListener(PanelToggleListener listener) {
        addFacesListener(listener);
    }

    public PanelToggleListener[] getPanelToggleListeners() {
        return (PanelToggleListener[]) getFacesListeners(PanelToggleListener.class);
    }

    public void removePanelToggleListener(PanelToggleListener listener) {
        removeFacesListener(listener);
    }

    @Override
    public Object getValue() {
        return String.valueOf(isExpanded());
    }

    @Override
    public void setValue(Object value) {
        setExpanded(Boolean.parseBoolean((String) value));
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        if (event instanceof PanelToggleEvent) {
            setExpanded(((PanelToggleEvent) event).getExpanded());
            setSubmittedActiveItem(null);
            if (event.getPhaseId() != PhaseId.UPDATE_MODEL_VALUES) {
                FacesContext.getCurrentInstance().renderResponse();
            }
        }
        super.broadcast(event);
    }
}
