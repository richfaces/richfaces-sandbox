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

package org.ajax4jsf.component;

import org.ajax4jsf.Messages;
import org.ajax4jsf.renderkit.AjaxRendererUtils;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @author Maksim Kaszynski
 */
public class EventValueExpression extends ValueExpression implements StateHolder {
    private static final long serialVersionUID = -6583167387542332290L;

    /**
     * current update component. transient since saved state as component.
     */
    private transient AjaxSupport component = null;
    private String componentId;

    public EventValueExpression() {

        // TODO Auto-generated constructor stub
    }

    public EventValueExpression(AjaxSupport component) {
        super();
        this.component = component;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.Expression#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        // TODO Auto-generated method stub
        return false;
    }

    private AjaxSupport getComponent(FacesContext facesContext) throws ELException {
        if (component == null) {
            UIComponent uiComponent = facesContext.getViewRoot().findComponent(componentId);

            if ((null != uiComponent) && (uiComponent instanceof AjaxSupport)) {
                component = (AjaxSupport) uiComponent;
            } else {
                throw new ELException(Messages.getMessage(Messages.COMPONENT_NOT_FOUND, componentId));
            }
        }

        return component;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ValueExpression#getExpectedType()
     */
    @Override
    public Class<?> getExpectedType() {
        return String.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.Expression#getExpressionString()
     */
    @Override
    public String getExpressionString() {

        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ValueExpression#getType(javax.el.ELContext)
     */
    @Override
    public Class<?> getType(ELContext context) {
        return String.class;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ValueExpression#getValue(javax.el.ELContext)
     */
    @Override
    public Object getValue(ELContext context) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AjaxSupport component = getComponent(facesContext);

        if (((UIComponent) component).isRendered()) {
            return component.getEventString();
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.Expression#hashCode()
     */
    @Override
    public int hashCode() {

        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.Expression#isLiteralText()
     */
    @Override
    public boolean isLiteralText() {

        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ValueExpression#isReadOnly(javax.el.ELContext)
     */
    @Override
    public boolean isReadOnly(ELContext context) {
        return true;
    }

    public boolean isTransient() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.StateHolder#restoreState(javax.faces.context.FacesContext,
     *      java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state) {
        componentId = (String) state;
    }

    public Object saveState(FacesContext context) {
        if (null == component) {
            return componentId;
        } else {
            return AjaxRendererUtils.getAbsoluteId((UIComponent) getComponent(context));
        }
    }

    /**
     * @param component the component to set
     */
    public void setComponent(AjaxSupport component) {
        this.component = component;
    }

    public void setTransient(boolean newTransientValue) {
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.el.ValueExpression#setValue(javax.el.ELContext,
     *      java.lang.Object)
     */
    @Override
    public void setValue(ELContext context, Object value) {
        throw new ELException(Messages.getMessage(Messages.EVENT_IS_READ_ONLY));
    }
}
