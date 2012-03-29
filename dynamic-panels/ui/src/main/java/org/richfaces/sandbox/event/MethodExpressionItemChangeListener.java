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

import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.MethodExpressionEventListener;

import javax.el.MethodExpression;
import javax.faces.event.AbortProcessingException;

/**
 * <p>
 * <strong><span class="changed_modified_2_0">MethodExpressionItemChangeListener</span></strong> is a {@link org.richfaces.event.ItemChangeListener}
 * that wraps a {@link MethodExpression}. When it receives a {@link org.richfaces.event.ItemChangeEvent}, it executes a method on an object
 * identified by the {@link MethodExpression}.
 * </p>
 *
 * @author akolonitsky
 * @version 1.0
 * @since -4712-01-01
 *
 */
public class MethodExpressionItemChangeListener extends MethodExpressionEventListener implements ItemChangeListener {
    public MethodExpressionItemChangeListener() {
    }

    public MethodExpressionItemChangeListener(MethodExpression methodExprOneArg) {
        super(methodExprOneArg);
    }

    public MethodExpressionItemChangeListener(MethodExpression methodExprOneArg, MethodExpression methodExprZeroArg) {
        super(methodExprOneArg, methodExprZeroArg);
    }

    // ------------------------------------------------------- Listener Method

    public void processItemChange(ItemChangeEvent itemChangeEvent) throws AbortProcessingException {
        processEvent(itemChangeEvent);
    }
}
