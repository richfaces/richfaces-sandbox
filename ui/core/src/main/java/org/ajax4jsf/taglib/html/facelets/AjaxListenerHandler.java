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
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;

import org.ajax4jsf.Messages;
import org.ajax4jsf.event.AjaxListener;
import org.ajax4jsf.event.AjaxListenerHelper;
import org.ajax4jsf.event.AjaxSource;

/**
 * Register an ActionListener instance on the UIComponent associated with the
 * closest parent UIComponent custom action. <p/> See <a target="_new"
 * href="http://java.sun.com/j2ee/javaserverfaces/1.1_01/docs/tlddocs/f/actionListener.html">tag
 * documentation</a>.
 * 
 * @see javax.faces.event.ActionListener
 * @see javax.faces.component.AjaxContainer
 * @author Jacob Hookom
 * @version $Id: AjaxListenerHandler.java,v 1.1.2.1 2007/02/01 15:31:23 alexsmirnov Exp $
 */
public final class AjaxListenerHandler extends TagHandler {

    private Class listenerType;

    private final TagAttribute type;

    private final TagAttribute binding;

    /**
     * @param config
     */
    public AjaxListenerHandler(TagConfig config) {
        super(config);
        this.binding = this.getAttribute("binding");
        this.type = this.getAttribute("type");
        if(binding == null && type == null){
            throw new TagException(this.tag, "One of the attribute 'binding' or type' is required");
        }
        if (binding == null && type != null) {
            if (!type.isLiteral()) {
                throw new TagAttributeException(this.tag, this.type, Messages.getMessage(Messages.MUST_BE_LITERAL_ERROR));
            }
            try {
                this.listenerType = Class.forName(type.getValue());
            } catch (Exception e) {
                throw new TagAttributeException(this.tag, this.type, e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
     *      javax.faces.component.UIComponent)
     */
    public void apply(FaceletContext ctx, UIComponent parent)
            throws IOException, FacesException, FaceletException, ELException {
        if (parent instanceof AjaxSource) {
            // only process if parent was just created
            if (parent.getParent() == null) {
                AjaxSource src = (AjaxSource) parent;
                AjaxListener listener = null;
                ValueExpression ve = null;
                if (this.binding != null) {
                    ve = this.binding.getValueExpression(ctx,
                            AjaxListener.class);
                    // TODO - handle both JSF 1.2/1.1 cases.
                    listener = new AjaxListenerHelper(ve);
                }
                if (listener == null) {
                    try {
                        listener = (AjaxListener) listenerType.newInstance();
                    } catch (Exception e) {
                        throw new TagAttributeException(this.tag, this.type, e.getCause());
                    }
                    if (ve != null) {
                        ve.setValue(ctx, ve);
                    }
                }
                src.addAjaxListener(listener);
            }
        } else {
            throw new TagException(this.tag, Messages.getMessage(Messages.NOT_PARENT_AJAX_COMPONENT_ERROR, parent));
        }
    }
}
