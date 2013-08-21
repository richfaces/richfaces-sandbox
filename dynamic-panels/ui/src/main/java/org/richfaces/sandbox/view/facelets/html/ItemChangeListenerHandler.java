/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.sandbox.view.facelets.html;

import java.io.Serializable;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;

import org.richfaces.ui.common.EventListenerHandler;
import org.richfaces.ui.common.EventListenerHandler.LazyEventListener;
import org.richfaces.ui.toggle.ItemChangeEvent;
import org.richfaces.ui.toggle.ItemChangeListener;
import org.richfaces.ui.toggle.ItemChangeSource;

/**
 *
 * @author akolonitsky
 * @version 1.0
 */
public final class ItemChangeListenerHandler extends EventListenerHandler {
    private static class LazyItemChangeListener extends LazyEventListener<ItemChangeListener> implements ItemChangeListener,
        Serializable {
        private static final long serialVersionUID = 7715606467989165179L;

        LazyItemChangeListener(String type, ValueExpression binding) {
            super(type, binding);
        }

        public void processItemChange(ItemChangeEvent event) throws AbortProcessingException {
            processEvent(event);
        }
    }

    public ItemChangeListenerHandler(TagConfig config) {
        super(config);
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        ValueExpression valueExpr = null;
        if (this.binding != null) {
            FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
            valueExpr = this.binding.getValueExpression(ctx, ItemChangeListener.class);
        }

        ItemChangeSource evh = (ItemChangeSource) parent;
        evh.addItemChangeListener(new LazyItemChangeListener(this.listenerType, valueExpr));
    }

    @Override
    public boolean isEventSource(UIComponent comp) {
        return comp instanceof ItemChangeSource;
    }
}
