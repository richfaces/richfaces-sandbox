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

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.el.MethodExpression;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;

/**
 * @author asmirnov@exadel.com (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.3 $ $Date: 2007/02/08 15:02:06 $
 * 
 */
public abstract class UIAjaxRegion extends UIPanel implements AjaxContainer {

	private AjaxRegionBrige brige;

	private static final  ContextCallback decodeCallback= new ContextCallback(){
		public void invokeContextCallback(FacesContext context,
				UIComponent target) {
			target.processDecodes(context);
		}
	};

	private static final  ContextCallback validateCallback= new ContextCallback(){
		public void invokeContextCallback(FacesContext context,
				UIComponent target) {
			target.processValidators(context);
		}
	};

	private static final  ContextCallback updateCallback= new ContextCallback(){
		public void invokeContextCallback(FacesContext context,
				UIComponent target) {
			target.processUpdates(context);
		}
	};

	public static final String COMPONENT_TYPE = "org.ajax4jsf.AjaxRegion";

	/**
	 * 
	 */
	public UIAjaxRegion() {
		brige = new AjaxRegionBrige(this);
	}

	@Override
	public void processDecodes(FacesContext context) {
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		String ajaxSingleClientId = ajaxContext.getAjaxSingleClientId();
		// Process this component itself
		try {
			decode(context);
		} catch (RuntimeException e) {
			context.renderResponse();
			throw e;
		}
		if (ajaxContext.isAjaxRequest() && null != ajaxSingleClientId) {
			invokeOnComponent(context, ajaxSingleClientId, new ContextCallbackWrapper(decodeCallback));
			Set<String> areasToProcess = ajaxContext.getAjaxAreasToProcess();
			if(null != areasToProcess){
				for (String areaId : areasToProcess) {
					invokeOnComponent(context, areaId, new ContextCallbackWrapper(decodeCallback));
				}
			}
		} else {
			// Process all facets and children of this component
			Iterator<UIComponent> kids = getFacetsAndChildren();
			while (kids.hasNext()) {
				UIComponent kid = kids.next();
				kid.processDecodes(context);
			}
		}
	}

	@Override
	public void processValidators(FacesContext context) {
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		String ajaxSingleClientId = ajaxContext.getAjaxSingleClientId();
		if (ajaxContext.isAjaxRequest() && null != ajaxSingleClientId) {
			invokeOnComponent(context, ajaxSingleClientId,  new ContextCallbackWrapper(validateCallback));
			Set<String> areasToProcess = ajaxContext.getAjaxAreasToProcess();
			if(null != areasToProcess){
				for (String areaId : areasToProcess) {
					invokeOnComponent(context, areaId, new ContextCallbackWrapper(validateCallback));
				}
			}
		} else {
			super.processValidators(context);
		}
	}

	@Override
	public void processUpdates(FacesContext context) {
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		String ajaxSingleClientId = ajaxContext.getAjaxSingleClientId();
		if (ajaxContext.isAjaxRequest() && null != ajaxSingleClientId) {
			invokeOnComponent(context, ajaxSingleClientId, new ContextCallbackWrapper(updateCallback));
			Set<String> areasToProcess = ajaxContext.getAjaxAreasToProcess();
			if(null != areasToProcess){
				for (String areaId : areasToProcess) {
					invokeOnComponent(context, areaId, new ContextCallbackWrapper(updateCallback));
				}
			}
		} else {
			super.processUpdates(context);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#broadcast(javax.faces.event.FacesEvent)
	 */
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);
		brige.broadcast(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#getAjaxListener()
	 */
	public MethodExpression getAjaxListener() {
		return brige.getAjaxListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#isImmediate()
	 */
	public boolean isImmediate() {
		return brige.isImmediate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#isSubmitted()
	 */
	public boolean isSubmitted() {
		return brige.isSubmitted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#restoreState(javax.faces.context.FacesContext,
	 *      java.lang.Object)
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] mystate = (Object[]) state;
		super.restoreState(context, mystate[0]);
		brige.restoreState(context, mystate[1]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext context) {
		Object[] state = new Object[2];
		state[0] = super.saveState(context);
		state[1] = brige.saveState(context);
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setAjaxListener(javax.faces.el.MethodBinding)
	 */
	public void setAjaxListener(MethodExpression ajaxListener) {
		brige.setAjaxListener(ajaxListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setImmediate(boolean)
	 */
	public void setImmediate(boolean immediate) {
		brige.setImmediate(immediate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setSubmitted(boolean)
	 */
	public void setSubmitted(boolean submitted) {
		brige.setSubmitted(submitted);
	}

	public void addAjaxListener(AjaxListener listener) {
		addFacesListener(listener);
	}

	public AjaxListener[] getAjaxListeners() {
		return (AjaxListener[]) getFacesListeners(AjaxListener.class);
	}

	public void removeAjaxListener(AjaxListener listener) {
		removeFacesListener(listener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#isSelfRendered()
	 */
	public boolean isSelfRendered() {
		return brige.isSelfRendered();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.AjaxViewBrige#setSelfRendered(boolean)
	 */
	public void setSelfRendered(boolean selfRendered) {
		brige.setSelfRendered(selfRendered);
	}

	public void encodeAjax(FacesContext context) throws IOException {
		String rendererType = getRendererType();
		if (rendererType != null) {
			((AjaxContainerRenderer) getRenderer(context)).encodeAjax(context,
					this);
		}

	}


	/**
	 * @return
	 * @see org.ajax4jsf.component.AjaxRegionBrige#isTransient()
	 */
	public boolean isTransient() {
		return brige.isTransient();
	}


	/**
	 * @param transientFlag
	 * @see org.ajax4jsf.component.AjaxRegionBrige#setTransient(boolean)
	 */
	public void setTransient(boolean transientFlag) {
		brige.setTransient(transientFlag);
	}
	
	

}
