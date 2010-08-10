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

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.context.ViewIdHolder;

/**
 * @author shura
 * 
 */
public abstract class UIInclude extends UIComponentBase implements
		ViewIdHolder, NamingContainer, AjaxOutput {

	public static final String COMPONENT_TYPE = "org.ajax4jsf.Include";

	public static final String LAYOUT_NONE ="none";
	
	public static final String LAYOUT_BLOCK ="block";
	public static final String LAYOUT_INLINE ="inline";
	
	private boolean _ajaxRendered = false;
	private boolean _ajaxRenderedSet = false;
	
	private boolean wasNavigation = false;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ajax4jsf.framework.ajax.ViewIdHolder#skipNavigation(java.lang.String)
	 */
	public boolean skipNavigation(String ViewId) {
		wasNavigation = true;
		return true;
	}

	private String _viewId = null;

	/*
	 * viewId for included page. Setter for viewId @param viewId - new value
	 */
	public void setViewId(String __viewId) {
		ValueExpression vb = getValueExpression("viewId");
		ELContext elContext = getELContext();
		if (null != vb && (!vb.isReadOnly(elContext))) {
			// Change value of viewId in backed bean, if possible.
			vb.setValue(elContext, __viewId);
		} else {
			this._viewId = __viewId;
		}
	}

	/*
	 * viewId for included page. Getter for viewId @return viewId value from
	 * local variable or value bindings
	 */
	public String getViewId() {
		if (null != this._viewId) {
			return this._viewId;
		}
		ValueExpression vb = getValueExpression("viewId");
		if (null != vb) {
			return (String) vb.getValue(getELContext());
		} else {
			return null;
		}
	}
	
	private String _layout = null;

	/**
	 * @return the layout
	 */
	public String getLayout() {
		if (null != this._layout) {
			return this._layout;
		}
		ValueExpression vb = getValueExpression("layout");
		if (null != vb) {
			return (String) vb.getValue(getELContext());
		} else {
			return null;
		}
	}

	/**
	 * @param layout the layout to set
	 */
	public void setLayout(String layout) {
		_layout = layout;
	}

	
	public boolean isAjaxRendered() {
	    Boolean value = null;
	    
	    if(this._ajaxRenderedSet) {
	    	return this._ajaxRendered;
	    }	
	    	
	    ValueExpression ve = getValueExpression("ajaxRendered");
		if (ve != null) {
		    try {
			value = (Boolean) ve.getValue(getFacesContext().getELContext());
		    } catch (ELException e) {
			throw new FacesException(e);
		    }
		} 
	     
	    
	    if (null == value) {
//			value = this._ajaxRendered;
	    	value = isWasNavigation();
	    } 

	    return (!LAYOUT_NONE.equals(getLayout())) && value;
	}
	
	public void setAjaxRendered(boolean ajaxRendered) {
	    this._ajaxRendered = ajaxRendered;
	    this._ajaxRenderedSet = true;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#broadcast(javax.faces.event.FacesEvent)
	 */
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		if (event instanceof EventWrapper) {
			FacesEvent wrapped = ((EventWrapper) event).getWrapped();
			FacesContext context = getFacesContext();
			ViewIdHolder holder = setupNavigation(context);
			wrapped.getComponent().broadcast(wrapped);
			restoreNavigation(context, holder);
		} else {
			super.broadcast(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#processDecodes(javax.faces.context.FacesContext)
	 */
	public void processDecodes(FacesContext context) {
		wasNavigation = false;
		ViewIdHolder holder = setupNavigation(context);
		super.processDecodes(context);
		restoreNavigation(context, holder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#processUpdates(javax.faces.context.FacesContext)
	 */
	public void processUpdates(FacesContext context) {
		ViewIdHolder holder = setupNavigation(context);
		super.processUpdates(context);
		restoreNavigation(context, holder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#processValidators(javax.faces.context.FacesContext)
	 */
	public void processValidators(FacesContext context) {
		ViewIdHolder holder = setupNavigation(context);
		super.processValidators(context);
		restoreNavigation(context, holder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.component.UIComponentBase#queueEvent(javax.faces.event.FacesEvent)
	 */
	public void queueEvent(FacesEvent event) {
		EventWrapper wrapper = new EventWrapper(this, event);
		super.queueEvent(wrapper);
	}

	
	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#restoreState(javax.faces.context.FacesContext, java.lang.Object)
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] componentState = (Object[]) state;
		super.restoreState(context, componentState[0]);
		this._viewId = (String) componentState[1];
		this._layout = (String) componentState[2];
		this._ajaxRendered = ((Boolean) componentState[3]).booleanValue();
		this._ajaxRenderedSet = ((Boolean) componentState[4]).booleanValue();
	}	

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#saveState(javax.faces.context.FacesContext)
	 */
	public Object saveState(FacesContext context) {
		Object[] componentState = new Object[5];
		componentState[0] = super.saveState(context);
		componentState[1] = _viewId;
		componentState[2] = _layout;
		componentState[3] = Boolean.valueOf(_ajaxRendered);
		componentState[4] = Boolean.valueOf(_ajaxRenderedSet);
		return componentState;
	}

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponent#getFamily()
	 */
	public String getFamily() {
		// TODO Auto-generated method stub
		return null;
	}

	private ViewIdHolder setupNavigation(FacesContext context) {
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		ViewIdHolder viewIdHolder = ajaxContext.getViewIdHolder();
		ajaxContext.setViewIdHolder(this);
		return viewIdHolder;
	}

	private void restoreNavigation(FacesContext context, ViewIdHolder viewIdHolder) {
		AjaxContext ajaxContext = AjaxContext.getCurrentInstance(context);
		ajaxContext.setViewIdHolder(viewIdHolder);
//		if (wasNavigation) {
//			// Clear children to avoid have different components with same id
//			getChildren().clear();
//		}
	}

	private ELContext getELContext() {
		return getFacesContext().getELContext();
	}
	
	@SuppressWarnings("serial")
	private static class EventWrapper extends FacesEvent {

		private FacesEvent wrapped;

		public EventWrapper(UIComponent component, FacesEvent wrapped) {
			super(component);
			this.wrapped = wrapped;
		}

		/**
		 * @return
		 * @see javax.faces.event.FacesEvent#getPhaseId()
		 */
		public PhaseId getPhaseId() {
			return wrapped.getPhaseId();
		}

		/**
		 * @param listener
		 * @return
		 * @see javax.faces.event.FacesEvent#isAppropriateListener(javax.faces.event.FacesListener)
		 */
		public boolean isAppropriateListener(FacesListener listener) {
			return wrapped.isAppropriateListener(listener);
		}

		/**
		 * @param listener
		 * @see javax.faces.event.FacesEvent#processListener(javax.faces.event.FacesListener)
		 */
		public void processListener(FacesListener listener) {
			wrapped.processListener(listener);
		}

		/**
		 * 
		 * @see javax.faces.event.FacesEvent#queue()
		 */
		public void queue() {
			wrapped.queue();
		}

		/**
		 * @param phaseId
		 * @see javax.faces.event.FacesEvent#setPhaseId(javax.faces.event.PhaseId)
		 */
		public void setPhaseId(PhaseId phaseId) {
			wrapped.setPhaseId(phaseId);
		}

		/**
		 * @return the wrapped
		 */
		public FacesEvent getWrapped() {
			return wrapped;
		}
	}

	/**
	 * @return the wasNavigation
	 */
	public boolean isWasNavigation() {
		return wasNavigation;
	}


}
