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

import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxEvent;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.AjaxSource;


/**
 * Quite different from default <code>HtmlForm</code> - process child components
 * not only for submitted form, but if submitted parent <code>AjaxContainer</code>
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/06 16:23:22 $
 *
 */
public abstract class UIAjaxForm extends UIForm implements AjaxComponent, AjaxSource, IterationStateHolder 
{
    public static final String COMPONENT_TYPE = "org.ajax4jsf.Form";

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#processDecodes(javax.faces.context.FacesContext)
     */
    public void processDecodes(javax.faces.context.FacesContext context)
    {
        if (context == null) {
			throw new NullPointerException("context");
		}
        decode(context);
        if (mustProcessed(context))
        {
            for (Iterator<UIComponent> it = getFacetsAndChildren(); it.hasNext();)
            {
                UIComponent childOrFacet = (UIComponent) it.next();
                childOrFacet.processDecodes(context);
            }
        }
    }

    
    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#processValidators(javax.faces.context.FacesContext)
     */
    public void processValidators(javax.faces.context.FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (mustProcessed(context))
        {
            for (Iterator<UIComponent> it = getFacetsAndChildren(); it.hasNext();)
            {
                UIComponent childOrFacet = (UIComponent) it.next();
                childOrFacet.processValidators(context);
            }
        }
    }
    
    

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#processUpdates(javax.faces.context.FacesContext)
     */
    public void processUpdates(javax.faces.context.FacesContext context)
    {
        if (context == null) throw new NullPointerException("context");
        if (mustProcessed(context))
        {
            for (Iterator<UIComponent> it = getFacetsAndChildren(); it.hasNext();)
            {
                UIComponent childOrFacet = (UIComponent) it.next();
                childOrFacet.processUpdates(context);
            }
        }
    }

    /**
     * Test for condition processing decoders/validators/updates
     * @param context current <code>FacesContext</code>
     * @return true if submitted parent AjaxContainer or current form.
     */
    private boolean mustProcessed(FacesContext context)
    {
//        if ( !AjaxContext.getCurrentInstance(context).isAjaxRequest(context)) {
            if (!isSubmitted() ) {
                return false;
            }
//        }
        return true;
    }
    

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#broadcast(javax.faces.event.FacesEvent)
	 */
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		// perform default
		super.broadcast(event);
		if (event instanceof AjaxEvent) {
			// complete re-Render fields. AjaxEvent deliver before render response.
			setupReRender();
		}
	}


	/**
	 * Template methods for fill set of resions to render in subclasses.
	 */
	protected void setupReRender() {
		FacesContext context = getFacesContext();
		AjaxContext.getCurrentInstance(context).addRegionsFromComponent(this);
	}




	/* (non-Javadoc)
	 * @see org.ajax4jsf.framework.ajax.AjaxSource#addAjaxListener(org.ajax4jsf.framework.ajax.AjaxListener)
	 */
	public void addAjaxListener(AjaxListener listener) {
        addFacesListener(listener);		
	}


	/* (non-Javadoc)
	 * @see org.ajax4jsf.framework.ajax.AjaxSource#getAjaxListeners()
	 */
	public AjaxListener[] getAjaxListeners() {
        AjaxListener al[] = (AjaxListener [])
	    getFacesListeners(AjaxListener.class);
        return (al);

	}


	/* (non-Javadoc)
	 * @see org.ajax4jsf.framework.ajax.AjaxSource#removeAjaxListener(org.ajax4jsf.framework.ajax.AjaxListener)
	 */
	public void removeAjaxListener(AjaxListener listener) {
		removeFacesListener(listener);
		
	}
    
	public abstract boolean isAjaxSubmit();
	
	public abstract void setAjaxSubmit(boolean ajax);
 
	public Object getIterationState() {
		return isSubmitted() ? Boolean.TRUE : Boolean.FALSE;
	}
	
	public void setIterationState(Object state) {
		setSubmitted(Boolean.TRUE.equals(state));
	}
}
