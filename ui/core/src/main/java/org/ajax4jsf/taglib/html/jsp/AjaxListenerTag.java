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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.ajax4jsf.Messages;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.AjaxListenerHelper;
import org.ajax4jsf.event.AjaxSource;


/**
 * @author shura
 * 
 * Custom tag for append AJAX request listeners to AjaxContainer.
 *  
 */
public class AjaxListenerTag extends TagSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 2576519366310474212L;
    /**
     * class name for ajax events listener.   
     */
    private ValueExpression type = null;
    
    private ValueExpression binding = null;

    /**
     *  
     */
    public AjaxListenerTag() {
    }

    public void setType(ValueExpression type) {
        this.type = type;
    }

    /**
	 * @param binding the binding to set
	 */
	public void setBinding(ValueExpression binding) {
		this.binding = binding;
	}

	public int doStartTag() throws JspException {
        if (type == null) {
            throw new JspException(Messages.getMessage(Messages.NULL_TYPE_ATTRIBUTE_ERROR));
        }

        //Find parent UIComponentTag
        UIComponentClassicTagBase componentTag = 
        	UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
        if (componentTag == null) {
            throw new JspException(
            		Messages.getMessage(Messages.NO_UI_COMPONENT_TAG_ANCESTOR_ERROR, "AjaxListenerTag"));
        }

        if (componentTag.getCreated()) {
            //Component was just created, so we add the Listener
            UIComponent component = componentTag.getComponentInstance();
            if (component instanceof AjaxSource) {
                AjaxListener listener;
                if(null != binding){
                 	listener = new AjaxListenerHelper(binding);
                } else {
				try {
	                String className = (String) type.getValue(FacesContext.getCurrentInstance().getELContext());
					listener = (AjaxListener) Class.forName(className).newInstance();
				} catch (Exception e) {
	                throw new JspException(Messages.getMessage(Messages.INSTANTIATE_LISTENER_ERROR, type, component.getId()), e);
				} 
                }
                ((AjaxSource) component).addAjaxListener(listener);
            } else {
                throw new JspException(Messages.getMessage(Messages.NOT_PARENT_AJAX_COMPONENT_ERROR, component.getId()));
            }
        }

        return Tag.SKIP_BODY;
    }

}
