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

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;

import org.richfaces.ui.common.EventListenerHandler;
import org.richfaces.ui.common.EventListenerHandler.LazyEventListener;
import org.richfaces.ui.toggle.PanelToggleEvent;
import org.richfaces.ui.toggle.PanelToggleListener;
import org.richfaces.ui.toggle.PanelToggleSource;

/**
 *
 * @author akolonitsky
 * @version 1.0
 */
public final class PanelToggleListenerHandler extends EventListenerHandler {
    private static class LazyPanelToggleListener extends LazyEventListener<PanelToggleListener> implements PanelToggleListener {
        private static final long serialVersionUID = -391020876192823200L;

        LazyPanelToggleListener(String type, ValueExpression binding) {
            super(type, binding);
        }

        public void processPanelToggle(PanelToggleEvent event) throws AbortProcessingException {
            processEvent(event);
        }
    }

    public PanelToggleListenerHandler(TagConfig config) {
        super(config);
    }

    public void applyAttachedObject(FacesContext context, UIComponent parent) {
        ValueExpression expression = null;
        if (this.binding != null) {
            FaceletContext ctx = (FaceletContext) context.getAttributes().get(FaceletContext.FACELET_CONTEXT_KEY);
            expression = this.binding.getValueExpression(ctx, PanelToggleListener.class);
        }

        PanelToggleSource source = (PanelToggleSource) parent;
        source.addPanelToggleListener(new LazyPanelToggleListener(this.listenerType, expression));
    }

    @Override
    public boolean isEventSource(UIComponent comp) {
        return comp instanceof PanelToggleSource;
    }
}
