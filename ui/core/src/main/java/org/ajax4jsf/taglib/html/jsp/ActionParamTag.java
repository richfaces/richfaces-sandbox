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
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentClassicTagBase;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.UIActionParameter;
import org.ajax4jsf.webapp.taglib.UIComponentTagBase;


/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:31 $
 *
 */
public class ActionParamTag extends UIComponentTagBase
{

    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType()
    {
        return "org.ajax4jsf.components.ActionParameter";
    }

    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType()
    {
        return null;
    }
    // UIComponent attributes --> already implemented in UIComponentTagBase

    // UIParameter attributes
    // value already implemented in UIComponentTagBase
    private ValueExpression _name;
    private ValueExpression _assignTo;
    private ValueExpression _converter;
    private ValueExpression _noEscape;
    private MethodExpression _actionListener;
    
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);        
        setStringProperty(component, "name", _name);
        setBooleanProperty(component, "noEscape", _noEscape);
        if (_actionListener != null) {
        	((UIActionParameter) component).setActionListener(_actionListener);
        }
        
        //Find parent UIComponentTag
        UIComponentClassicTagBase componentTag = 
        	UIComponentClassicTagBase.getParentUIComponentClassicTagBase(pageContext);
        if (componentTag == null)
        {
            throw new IllegalArgumentException(Messages.getMessage(Messages.NO_UI_COMPONENT_TAG_ANCESTOR_ERROR, "ActionParameterTag"));
        }

        if (componentTag.getCreated())
        {
            //Component was just created, so we add the Listener
            UIComponent parentComponent = componentTag.getComponentInstance();
            if (parentComponent instanceof ActionSource)
            {

            	if (_assignTo != null) {
                    if (_assignTo.isLiteralText()) throw new IllegalArgumentException(Messages.getMessage(Messages.NO_VALUE_REFERENCE_ERROR_2, _assignTo));
                    UIActionParameter al = (UIActionParameter)component;
                    al.setAssignToBinding(_assignTo);
                if (_converter != null)
                {
                	if (!_converter.isLiteralText()) {
		                component.setValueExpression("converter", _converter);
		            } else {
		                Converter conv = FacesContext.getCurrentInstance().getApplication().createConverter(_converter.getExpressionString());
		                al.setConverter(conv);
		            }
                }
                ((ActionSource)parentComponent).addActionListener(al);
                }
            }
        }

    }

    public void setName(ValueExpression name)
    {
        _name = name;
    }

    /**
     * @param converter The converter to set.
     */
    public void setConverter(ValueExpression converter)
    {
        this._converter = converter;
    }

    /**
     * @param noEscape The noEscape to set.
     */
    public void setNoEscape(ValueExpression noEscape)
    {
        this._noEscape = noEscape;
    }

    /**
     * @param property The property to set.
     */
    public void setAssignTo(ValueExpression property)
    {
        this._assignTo = property;
    }
    
    /**
     * @param property The property to set.
     */
    public void setActionListener(MethodExpression listener) {
		this._actionListener = listener;
	}

    /* (non-Javadoc)
     * @see org.apache.myfaces.taglib.UIComponentTagBase#release()
     */
    public void release()
    {
        _name = null;
        _assignTo = null;
        _converter = null;
        _noEscape = null;
        _actionListener = null;
        super.release();
    }
    
}
