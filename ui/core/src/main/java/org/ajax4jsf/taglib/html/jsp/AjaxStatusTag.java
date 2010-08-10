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

import org.ajax4jsf.component.UIAjaxStatus;
import org.ajax4jsf.renderkit.html.AjaxStatusRenderer;
import org.ajax4jsf.webapp.taglib.HtmlComponentTagBase;


/**
 * 
 * Jsp Tag for create request state component.
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:30 $
 *
 */
public class AjaxStatusTag extends HtmlComponentTagBase
{

    private ValueExpression _for = null;
    /**
     * Text to output on start request
     */
    private ValueExpression _startText = null;
    /**
     * Text to display on complete request
     */
    private ValueExpression _stopText = null;

    /**
     * Style for display on start request
     */
    private ValueExpression _startStyle = null;
    /**
     * Style for displaying on complete
     */
    private ValueExpression _stopStyle = null;
    /**
     * Style class for display on request
     */
    private ValueExpression _startStyleClass = null;
    /**
     * Style class for display on complete request
     */
    private ValueExpression _stopStyleClass = null;
    
    /**
     * Force id to render in Html as is
     */
    private ValueExpression _forceId = null;
    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getComponentType()
     */
    public String getComponentType()
    {
        // TODO Auto-generated method stub
        return UIAjaxStatus.COMPONENT_TYPE;
    }

    /* (non-Javadoc)
     * @see javax.faces.webapp.UIComponentTag#getRendererType()
     */
    public String getRendererType()
    {
        // TODO Auto-generated method stub
        return AjaxStatusRenderer.RENDERER_TYPE;
    }

    /* (non-Javadoc)
     * @see org.ajax4jsf.components.taglib.html.HtmlComponentTagBase#release()
     */
    public void release()
    {
        super.release();
        this._for = null;
        this._startStyle = null;
        this._startStyleClass = null;
        this._startText = null;
        this._stopStyle = null;
        this._stopStyleClass = null;
        this._stopText = null;
        this._forceId = null;
        
    }

    /* (non-Javadoc)
     * @see org.ajax4jsf.components.taglib.html.HtmlComponentTagBase#setProperties(javax.faces.component.UIComponent)
     */
    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        setStringProperty(component, "for", _for);
        setStringProperty(component, "startText", _startText);
        setStringProperty(component, "stopText", _stopText);
        setStringProperty(component, "startStyle", _startStyle);
        setStringProperty(component, "stopStyle", _stopStyle);
        setStringProperty(component, "startStyleClass", _startStyleClass);
        setStringProperty(component, "stopStyleClass", _stopStyleClass);
        setBooleanProperty(component, "forceId", _forceId);
        
    }

    
    /**
     * @param _for The _for to set.
     */
    public void setFor(ValueExpression _for)
    {
        this._for = _for;
    }

    /**
     * @param style The _startStyle to set.
     */
    public void setStartStyle(ValueExpression style)
    {
        _startStyle = style;
    }

    /**
     * @param styleClass The _startStyleClass to set.
     */
    public void setStartStyleClass(ValueExpression styleClass)
    {
        _startStyleClass = styleClass;
    }

    /**
     * @param text The _startText to set.
     */
    public void setStartText(ValueExpression text)
    {
        _startText = text;
    }

    /**
     * @param stopText The stopText to set.
     */
    public void setStopText(ValueExpression stopText)
    {
        this._stopText = stopText;
    }

    /**
     * @param style The _stopStyle to set.
     */
    public void setStopStyle(ValueExpression style)
    {
        _stopStyle = style;
    }

    /**
     * @param styleClass The _stopStyleClass to set.
     */
    public void setStopStyleClass(ValueExpression styleClass)
    {
        _stopStyleClass = styleClass;
    }

    /**
     * @param forceId The forceId to set.
     */
    public void setForceId(ValueExpression forceId)
    {
        this._forceId = forceId;
    }

}
