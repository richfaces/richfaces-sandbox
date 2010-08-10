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

package org.ajax4jsf.taglib.html.facelets;

import java.io.IOException;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import org.ajax4jsf.Messages;
import org.ajax4jsf.event.AjaxPhaseListener;

/**
 * @author shura
 *
 */
public class KeepAliveHandler extends TagHandler {

	private final TagAttribute beanName;

	private final TagAttribute ajaxOnly;
	/**
	 * @param config
	 */
	public KeepAliveHandler(TagConfig config) {
		super(config);
        this.beanName = this.getRequiredAttribute("beanName");
        if (beanName != null) {
            if (!beanName.isLiteral()) {
                throw new TagAttributeException(this.tag, this.beanName, Messages.getMessage(Messages.MUST_BE_LITERAL_ERROR));
            }
        }
        this.ajaxOnly = getAttribute("ajaxOnly");
	}

	/* (non-Javadoc)
	 * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext, javax.faces.component.UIComponent)
	 */
	public void apply(FaceletContext ctx, UIComponent parent)
			throws IOException, FacesException, FaceletException, ELException {
        // Get bean instance from EL expression.
		String name = beanName.getValue();
		boolean isAjaxOnly = ajaxOnly!=null?ajaxOnly.getBoolean(ctx):false;
        FacesContext facesContext = ctx.getFacesContext();
        String beanExpression = "#{"+name+"}";
        ValueBinding valueBinding = facesContext.getApplication().createValueBinding(beanExpression);
        Object bean = valueBinding.getValue(facesContext);
        String beanAttributeName = (isAjaxOnly ? AjaxPhaseListener.AJAX_BEAN_PREFIX : AjaxPhaseListener.VIEW_BEAN_PREFIX)
        	+ name;
		// Put bean instance to ViewRoot. 
        facesContext.getViewRoot().getAttributes().put(beanAttributeName, bean);
	}

}
