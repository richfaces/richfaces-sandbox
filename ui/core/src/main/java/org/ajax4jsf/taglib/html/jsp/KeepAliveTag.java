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

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.ajax4jsf.Messages;
import org.ajax4jsf.event.AjaxPhaseListener;

/**
 * @author shura
 *
 */
public class KeepAliveTag extends TagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4322021112358067548L;
	
	private String beanName = null;
	
	private ValueExpression ajaxOnly = null;

	/**
	 * @return the ajaxOnly
	 */
	public ValueExpression getAjaxOnly() {
		return ajaxOnly;
	}

	/**
	 * @param ajaxOnly the ajaxOnly to set
	 */
	public void setAjaxOnly(ValueExpression ajaxOnly) {
		this.ajaxOnly = ajaxOnly;
	}

	/**
	 * @return the name
	 */
	public String getBeanName() {
		return beanName;
	}

	/**
	 * @param name the name to set
	 */
	public void setBeanName(String name) {
		this.beanName = name;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	public int doStartTag() throws JspException {
        if (beanName == null) {
            throw new JspException(Messages.getMessage(Messages.NULL_TYPE_ATTRIBUTE_ERROR));
        }
        
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ExpressionFactory factory = application.getExpressionFactory();
		ELContext elContext = facesContext.getELContext();
		ValueExpression beanNameEL = 
			factory.createValueExpression(elContext, beanName, String.class);
        if(!beanNameEL.isLiteralText()){
            throw new JspException(Messages.getMessage(Messages.NAME_MUST_BE_LITERAL));
        }
		boolean isAjaxOnly = false;
		if (null != ajaxOnly) {
            if (ajaxOnly.isLiteralText())
            {
                //TODO: More sophisticated way to convert boolean value (yes/no, 1/0, on/off, etc.)
                isAjaxOnly = Boolean.parseBoolean(ajaxOnly.getExpressionString());
            }
            else
            {
                isAjaxOnly = Boolean.TRUE.equals(ajaxOnly.getValue(elContext));
            }

		}
        // Get bean instance from EL expression.
        String beanExpression = "#{"+beanName+"}";
        ValueExpression valueExpression = 
        	factory.createValueExpression(elContext, beanExpression, Object.class);
        Object bean = valueExpression.getValue(elContext);
        // Put bean instance to ViewRoot. 
        String beanAttributeName = (isAjaxOnly ? AjaxPhaseListener.AJAX_BEAN_PREFIX : AjaxPhaseListener.VIEW_BEAN_PREFIX) 
        	+ beanName;
        facesContext.getViewRoot().getAttributes().put(beanAttributeName, bean);
		return Tag.SKIP_BODY;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#release()
	 */
	public void release() {
		beanName = null;
		ajaxOnly = null;
		super.release();
	}
	

}
