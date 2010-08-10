/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk.faces;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class ReadOnlyValueExpression extends ValueExpression {

    private static final long serialVersionUID = 7424302517400727467L;

    @Override
    public abstract Object getValue(ELContext context);

    @Override
    public void setValue(ELContext context, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly(ELContext context) {
        return true;
    }

    @Override
    public Class<?> getType(ELContext context) {
        return null;
    }

    @Override
    public Class<?> getExpectedType() {
        return Object.class;
    }

    @Override
    public String getExpressionString() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    public boolean isLiteralText() {
        return false;
    }

    protected FacesContext getFacesContext(ELContext elContext) {
        return (FacesContext) elContext.getContext(FacesContext.class);
    }
}
