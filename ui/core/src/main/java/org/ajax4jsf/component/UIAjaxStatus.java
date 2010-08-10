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

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import org.ajax4jsf.Messages;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererUtils;


/**
 * 
 * Component for create request state status area.
 * can create 2 output text areas ( &lt;span&gt; ) with different text and styles.
 * if defined "start" or/and "stop" facets, it's use as conttent
 * for different state.
 * By default, "start" text/facet rendered with display = none style.
 * On client side, display style changed for elements on 
 * start-stop request.
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:25 $
 *
 */
public class UIAjaxStatus extends UIComponentBase
{

    private static final String COMPONENT_FAMILY = "javax.faces.Output";
    public static final String COMPONENT_TYPE = "org.ajax4jsf.AjaxStatus";
    

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#getFamily()
     */
    public String getFamily()
    {
        return COMPONENT_FAMILY;
    }
    
    /**
     * id of form for wich status will displayed
     */
    private String _for = null;

    /**
     *
     * @param new value of id of form for wich status will displayed to set
     */
    public void setFor(String _for)
    {
        this._for = _for;
    }

    /**
     * @return value or result of valueBinding of id of form for wich status will displayed
     *
     */
    public String getFor()
    {
        return (String) getValueOrBinding(_for, "for");
    }
    
    /**
     * Text to output on start request
     */
    private String _startText = null;

    /**
     * setter method for property
     * @param new value of Text to output on start request to set
     */
    public void setStartText(String startText)
    {
        this._startText = startText;
    }

    /**
     * @return value or result of valueBinding of Text to output on start request
     */
    public String getStartText()
    {
        return (String) getValueOrBinding(_startText, "startText");
    }
    
    /**
     * Text to display on complete request
     */
    private String _stopText = null;

    /**
     * setter method for property
     * @param new value of Text to display on complete request to set
     */
    public void setStopText(String stopText)
    {
        this._stopText = stopText;
    }

    /**
     * @return value or result of valueBinding of Text to display on complete request
     */
    public String getStopText()
    {
        return (String) getValueOrBinding(_stopText, "stopText");
    }
    /**
     * Style for display on start request
     */
    private String _startStyle = null;

    /**
     * setter method for property
     * @param new value of Style for display on start request to set
     */
    public void setStartStyle(String startStyle)
    {
        this._startStyle = startStyle;
    }

    /**
     * @return value or result of valueBinding of Style for display on start request
     */
    public String getStartStyle()
    {
        return (String) getValueOrBinding(_startStyle, "startStyle");
    }
    /**
     * Style for displaying on complete
     */
    private String _stopStyle = null;

    /**
     * setter method for property
     * @param new value of Style for displaying on complete to set
     */
    public void setStopStyle(String stopStyle)
    {
        this._stopStyle = stopStyle;
    }

    /**
     * @return value or result of valueBinding of Style for displaying on complete
     */
    public String getStopStyle()
    {
        return (String) getValueOrBinding(_stopStyle, "stopStyle");
    }
    /**
     * Style class for display on request
     */
    private String _startStyleClass = null;

    /**
     * setter method for property
     * @param new value of Style class for display on request to set
     */
    public void setStartStyleClass(String startStyleClass)
    {
        this._startStyleClass = startStyleClass;
    }

    /**
     * @return value or result of valueBinding of Style class for display on request
     */
    public String getStartStyleClass()
    {
        return (String) getValueOrBinding(_startStyleClass, "startStyleClass");
    }
    /**
     * Style class for display on complete request
     */
    private String _stopStyleClass = null;

    /**
     * setter method for property
     * @param new value of Style class for display on complete request to set
     */
    public void setStopStyleClass(String stopStyleClass)
    {
        this._stopStyleClass = stopStyleClass;
    }

    
    /**
     * Force id to render in Html as is
     */
    private boolean _forceId = false;
    private boolean _forceIdSet = false;

    /**
     * setter method for property
     * @param new value of Force id to render in Html as is to set
     */
    public void setForceId(boolean forceId)
    {
        this._forceId = forceId;
        this._forceIdSet = true;
    }

    /**
     * @return value or result of valueBinding of Force id to render in Html as is
     */
    public boolean isForceId()
    {
        return  isValueOrBinding(_forceId, _forceIdSet, "forceId");
    }
    
    
    
    
	private String _clientId = null;

	/* (non-Javadoc)
	 * @see javax.faces.component.UIComponentBase#getClientId(javax.faces.context.FacesContext)
	 */
	public String getClientId(FacesContext context) {
        if (null == _clientId) {
			String forValue = getFor();
			UIComponent container;
			if (null != forValue) {
				container = RendererUtils.getInstance().findComponentFor(this, forValue);
				// 'for' attribute must be pointed to real container in view tree
				if (null == container || !(container instanceof AjaxContainer)) {
					throw new FacesException(
							Messages.getMessage(Messages.FOR_TARGETS_NO_AJAX_CONTAINER, getId()));
				}
				_clientId = container.getClientId(context) + ":status";

			} else if (isForceId()) {
				_clientId = getRenderer(context).convertClientId(context,
						this.getId());
			} else if (null !=(container = (UIComponent) AjaxRendererUtils.findAjaxContainer(context,this))) {
				_clientId = container.getClientId(context) + ":status";
			} else {
				_clientId = super.getClientId(context);
			}
		}
        return _clientId;
	}

	
	/* (non-Javadoc)
	 * reset clientId for calculate
	 * @see javax.faces.component.UIComponentBase#setId(java.lang.String)
	 */
	public void setId(String arg0) {
		super.setId(arg0);
		_clientId = null;
	}

	/* (non-Javadoc)
     * @see javax.faces.component.UIComponentBase#restoreState(javax.faces.context.FacesContext, java.lang.Object)
     */
    public void restoreState(FacesContext context, Object state)
    {
        State myState = (State) state;
        _for = myState._for;
        _startText = myState._startText;
        _stopText = myState._stopText;
        _startStyle = myState._startStyle;
        _stopStyle = myState._stopStyle;
        _startStyleClass = myState._startStyleClass;
        _stopStyleClass = myState._stopStyleClass;
        _forceId = myState._forceId;
        _forceIdSet = myState._forceIdSet;
        super.restoreState(context, myState.superState);
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponentBase#saveState(javax.faces.context.FacesContext)
     */
    public Object saveState(FacesContext context)
    {
        State state = new State();
        state._for = _for;
        state._startText = _startText;
        state._stopText = _stopText;
        state._startStyle = _startStyle;
        state._stopStyle = _stopStyle;
        state._startStyleClass = _startStyleClass;
        state._stopStyleClass = _stopStyleClass;
        state._forceId = _forceId;
        state._forceIdSet = _forceIdSet;
        state.superState = super.saveState(context);
        return state;
    }
    
    /**
     * @author shura (latest modification by $Author: alexsmirnov $)
     * @version $Revision: 1.1.2.1 $ $Date: 2007/01/09 18:58:25 $
     * Memento pattern state class for save-restore component.
     */
    public static class State implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 317266574102385358L;
        Object superState;
        String _for;
        String _startText;
        String _stopText;
        String _startStyle;
        String _stopStyle;
        String _startStyleClass;
        String _stopStyleClass;        
        boolean _forceIdSet;
        boolean _forceId;
    }

    /**
     * @return value or result of valueBinding of Style class for display on complete request
     */
    public String getStopStyleClass()
    {
        return (String) getValueOrBinding(_stopStyleClass, "stopStyleClass");
    }
    
    /**
     * @param field - value of field to get.
     * @param name - name of field, to get from ValueBinding
     * @return field or value of binding expression.
     */
    private Object getValueOrBinding(Object field, String name){
        if( null != field){
            return field;
        }
        ValueExpression vb = getValueExpression(name);
        if (null != vb) {
            return vb.getValue(getELContext());
        } else {
            return null;
        }

    }
    
    /**
     * @param field - value of field to get.
     * @param name - name of field, to get from ValueBinding
     * @return boolean value, based on field or valuebinding.
     */
    private boolean isValueOrBinding(boolean field, boolean fieldSet, String name){
        if( fieldSet ){
            return field;
        }
        ValueExpression vb = getValueExpression(name);
        if (null != vb) {
            return ((Boolean)vb.getValue(getELContext())).booleanValue();
        } else {
            return false;
        }

    }

    private ELContext getELContext() {
    	return getFacesContext().getELContext();
    }
}
