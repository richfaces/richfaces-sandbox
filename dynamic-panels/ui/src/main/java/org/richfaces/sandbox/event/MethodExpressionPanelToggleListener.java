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
package org.richfaces.sandbox.event;

import org.richfaces.event.MethodExpressionEventListener;
import org.richfaces.event.PanelToggleEvent;
import org.richfaces.event.PanelToggleListener;

import javax.el.MethodExpression;
import javax.faces.event.AbortProcessingException;

/**
 * <p>
 * <strong><span class="changed_modified_2_0">MethodExpressionPanelToggleListener</span></strong> is a
 * {@link org.richfaces.event.PanelToggleListener} that wraps a {@link MethodExpression}. When it receives a {@link org.richfaces.event.PanelToggleEvent}, it executes a
 * method on an object identified by the {@link MethodExpression}.
 * </p>
 *
 * @author akolonitsky
 * @version 1.0
 *
 */
public class MethodExpressionPanelToggleListener extends MethodExpressionEventListener implements PanelToggleListener {
    public MethodExpressionPanelToggleListener() {
        super();
    }

    public MethodExpressionPanelToggleListener(MethodExpression methodExprOneArg) {
        super(methodExprOneArg);
    }

    public MethodExpressionPanelToggleListener(MethodExpression methodExprOneArg, MethodExpression methodExprZeroArg) {
        super(methodExprOneArg, methodExprZeroArg);
    }

    // ------------------------------------------------------- Listener Method

    public void processPanelToggle(PanelToggleEvent panelToggleEvent) throws AbortProcessingException {
        processEvent(panelToggleEvent);
    }
}
