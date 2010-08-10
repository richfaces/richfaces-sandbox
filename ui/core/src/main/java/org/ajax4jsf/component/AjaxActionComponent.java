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

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.AjaxSource;
import org.ajax4jsf.renderkit.RendererUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/06 16:23:21 $
 */
public abstract class AjaxActionComponent extends UICommand implements AjaxComponent, AjaxSource {
    public static final String FOCUS_DATA_ID = "_A4J.AJAX.focus";

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#broadcast(javax.faces.event.FacesEvent)
     */
    public void broadcast(FacesEvent event) throws AbortProcessingException {

        // perform default
        super.broadcast(event);

        if (event instanceof AjaxEvent) {
            FacesContext context = getFacesContext();

            // complete re-Render fields. AjaxEvent deliver before render
            // response.
            setupReRender(context);

            // Put data for send in response
            Object data = getData();
            AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);

            if (null != data) {
                ajaxContext.setResponseData(data);
            }

            String focus = getFocus();

            if (null != focus) {

                // search for component in tree.
                // XXX - use more pourful search, as in h:outputLabel
                // component.
                UIComponent focusComponent = RendererUtils.getInstance().findComponentFor(this, focus);

                if (null != focusComponent) {
                    focus = focusComponent.getClientId(context);
                }

                ajaxContext.getResponseDataMap().put(FOCUS_DATA_ID, focus);
            }

            ajaxContext.setOnbeforedomupdate(getOnbeforedomupdate());
            ajaxContext.setOncomplete(getOncomplete());
        }
    }

    /**
     * Template method with old signature, for backward compability.
     */
    protected void setupReRender() {
    }

    /**
     * Template methods for fill set of resions to render in subclasses.
     *
     * @param facesContext TODO
     */
    protected void setupReRender(FacesContext facesContext) {
        AjaxContext.getCurrentInstance(facesContext).addRegionsFromComponent(this);
        setupReRender();
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.faces.component.UIComponentBase#queueEvent(javax.faces.event.FacesEvent)
     */
    public void queueEvent(FacesEvent event) {
        if (event instanceof ActionEvent) {
            if (event.getComponent() == this) {
                if (isImmediate()) {
                    event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
                } else if (isBypassUpdates()) {
                    event.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
                } else {
                    event.setPhaseId(PhaseId.INVOKE_APPLICATION);
                }
            }

            // UICommand set Phase ID for all ActionEvents - bypass it.
            getParent().queueEvent(event);
        } else {
            super.queueEvent(event);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxSource#addAjaxListener(org.ajax4jsf.framework.ajax.AjaxListener)
     */
    public void addAjaxListener(AjaxListener listener) {
        addFacesListener(listener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxSource#getAjaxListeners()
     */
    public AjaxListener[] getAjaxListeners() {
        AjaxListener[] al = (AjaxListener[]) getFacesListeners(AjaxListener.class);

        return al;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.ajax4jsf.framework.ajax.AjaxSource#removeAjaxListener(org.ajax4jsf.framework.ajax.AjaxListener)
     */
    public void removeAjaxListener(AjaxListener listener) {
        removeFacesListener(listener);
    }

    protected UIComponent getSingleComponent() {
        return this;
    }
}
