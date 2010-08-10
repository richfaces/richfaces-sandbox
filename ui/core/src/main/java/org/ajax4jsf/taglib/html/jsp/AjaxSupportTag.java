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

package org.ajax4jsf.taglib.html.jsp;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentClassicTagBase;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.UIAjaxSupport;
import org.ajax4jsf.renderkit.AjaxRendererUtils;
import org.ajax4jsf.renderkit.RendererUtils.HTML;
import org.ajax4jsf.webapp.taglib.UIComponentTagBase;


/**
 * 
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:32 $
 *
 */
public class AjaxSupportTag extends UIComponentTagBase {

    static final String[] EVENT_HANDLER_ATTRIBUTES =
    {
        HTML.ondblclick_ATTRIBUTE,
        HTML.onmousedown_ATTRIBUTE,
        HTML.onmouseup_ATTRIBUTE,
        HTML.onmouseover_ATTRIBUTE,
        HTML.onmousemove_ATTRIBUTE,
        HTML.onmouseout_ATTRIBUTE,
        HTML.onkeypress_ATTRIBUTE,
        HTML.onkeydown_ATTRIBUTE,
        HTML.onkeyup_ATTRIBUTE,
        HTML.onclick_ATTRIBUTE,
        HTML.onchange_ATTRIBUTE,
        HTML.onblur_ATTRIBUTE,
        HTML.onfocus_ATTRIBUTE,
        HTML.onselect_ATTRIBUTE
    };
    
	public static final String AJAX_SUPPORT_FACET = "org.ajax4jsf.ajax.SUPPORT";
    /**
     * Id ( in form of findComponent method param ) of JSF component,
     * updated by this component ( parnt tag ) action.
     */
    private ValueExpression reRender = null;
    
    /**
     * Generate script for given event ( onclick, onenter ... )
     */
    private String event = null;
    
    
    /**
     * Id of request status component.
     */
    private ValueExpression status = null;
    
    /**
     * JavaScript function for call after request completed.
     */
    private ValueExpression oncomplete = null ;
    
    private MethodExpression action = null;
    private MethodExpression actionListener = null;
    private ValueExpression limitToList = null;
    private ValueExpression disableDefault = null;
    private ValueExpression value = null;
    private ValueExpression immediate = null;
    private ValueExpression ajaxSingle = null;
    



    /**
     * @param type The type to set.
     */
    public void setDisableDefault(ValueExpression ajaxType)
    {
        this.disableDefault = ajaxType;
    }

    /**
     * @param reRender
     *            The targetId to set.
     */
    public void setReRender(ValueExpression ajaxId) {
        this.reRender = ajaxId;
    }

    
    /**
     * @param event The event to set.
     */
    public void setEvent(String event)
    {
        this.event = event;
    }

    /**
     * @param oncomplete The oncomplete to set.
     */
    public void setOncomplete(ValueExpression oncomplete)
    {
        this.oncomplete = oncomplete;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(ValueExpression status)
    {
        this.status = status;
    }

    /**
     * @param limitToList The submitForm to set.
     */
    public void setLimitToList(ValueExpression limitToList)
    {
        this.limitToList = limitToList;
    }

    /**
     * @param action The action to set.
     */
    public void setAction(MethodExpression action)
    {
        this.action = action;
    }

    /**
     * @param actionListener The actionListener to set.
     */
    public void setActionListener(MethodExpression actionListener)
    {
        this.actionListener = actionListener;
    }

    protected void setParentProperties(UIAjaxSupport uiComponent)  {

        //Find parent UIComponentTag
        UIComponentClassicTagBase componentTag = null;
		try {
			componentTag = UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
	        if (componentTag == null) {
	            throw new IllegalArgumentException(
	            		Messages.getMessage(Messages.NO_UI_COMPONENT_TAG_ANCESTOR_ERROR, "AjaxSupportTag"));
	        }

	        if (componentTag.getCreated()) {
	            //Component was just created, so we add the Listener
	            UIComponent component = componentTag.getComponentInstance();
	            uiComponent.setParentProperties(component);
	        }
		} catch (ClassCastException e) {
			// JSF 1.2 - tags have other parent.
		}

    } 

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        this.reRender = null;
        this.oncomplete = null;
        this.event = null;
        this.status =null;
        this.action = null;
        this.actionListener = null;
        this.limitToList = null;
        this.disableDefault = null;
        this.value = null;
        this.immediate=null;
        this.ajaxSingle = null;
        super.release();
    }

    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType()
    {
        
        return UIAjaxSupport.COMPONENT_TYPE;
    }

    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType()
    {
        
        return UIAjaxSupport.DEFAULT_RENDERER_TYPE;
    }

    /**
	 * @param value The value to set.
	 */
	public void setValue(ValueExpression value) {
		this.value = value;
	}

	/**
	 * @param immediate The immediate to set.
	 */
	public void setImmediate(ValueExpression immediate) {
		this.immediate = immediate;
	}

	/**
	 * @param ajaxSingle The ajaxSingle to set.
	 */
	public void setAjaxSingle(ValueExpression ajaxSingle) {
		this.ajaxSingle = ajaxSingle;
	}

	/* (non-Javadoc)
     * @see org.ajax4jsf.components.taglib.UIComponentTagBase#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component)
    {        
        super.setProperties(component);
        setStringProperty(component, "event", event);
        setReRenderProperty(component);
        setStringProperty(component, "status", status);
        setStringProperty(component, "oncomplete", oncomplete);
        setBooleanProperty(component, "disableDefault", disableDefault );
        setBooleanProperty(component, "limitToList", limitToList);
        setActionProperty(component, action);
        setActionListenerProperty(component, actionListener);
        setValueProperty(component,value);
        setBooleanProperty(component,"immediate",immediate);
        setBooleanProperty(component,"ajaxSingle",ajaxSingle);
        
        setParentProperties((UIAjaxSupport) component);
    }
    
    
    private void setReRenderProperty(UIComponent component) {
        if (reRender != null) {
            if (reRender.isLiteralText()) {
            	setStringProperty(component, "reRender", reRender);
            } else {
            	//FIXME: Why do we evaluate reRender right now?
            	//Collection can change any time.
                ((UIAjaxSupport) component).setReRender( AjaxRendererUtils.asSet(reRender.getValue(getELContext())));
            }
        }
    	
    }

	/* (non-Javadoc)
	 * @see javax.faces.webapp.UIComponentTag#getFacetName()
	 */
	protected String getFacetName() {
		// TODO Auto-generated method stub
		if (null != event) {
			return AJAX_SUPPORT_FACET + event;
		} else {
			return AJAX_SUPPORT_FACET;			
		}
	}
    
    
}
