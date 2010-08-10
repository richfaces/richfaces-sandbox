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
import javax.el.VariableMapper;
import javax.faces.context.FacesContext;

import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 * 
 */
public class VariableMapperImpl extends VariableMapper {

    private static final ValueExpression SKIN_VALUE_EXPRESSION = new ReadOnlyValueExpression() {

        private static final long serialVersionUID = 3552483406787835235L;

        @Override
        public Object getValue(ELContext elContext) {
            FacesContext facesContext = getFacesContext(elContext);
            return SkinFactory.getInstance(facesContext).getSkin(facesContext);
        }
    };

    private static final ValueExpression RESOURCE_VALUE_EXPRESSION = new ReadOnlyValueExpression() {

        private static final long serialVersionUID = -8545250767102884398L;

        @Override
        public Object getValue(ELContext elContext) {
            FacesContext facesContext = getFacesContext(elContext);
            return facesContext.getApplication().getResourceHandler();
        }
    };
    
    
    /* (non-Javadoc)
     * @see javax.el.VariableMapper#resolveVariable(java.lang.String)
     */
    @Override
    public ValueExpression resolveVariable(String variable) {
        if ("richSkin".equals(variable)) {
            return SKIN_VALUE_EXPRESSION;
        } else if ("resource".equals(variable)) {
            return RESOURCE_VALUE_EXPRESSION;
        }
        
        return null;
    }

    /* (non-Javadoc)
     * @see javax.el.VariableMapper#setVariable(java.lang.String, javax.el.ValueExpression)
     */
    @Override
    public ValueExpression setVariable(String variable, ValueExpression expression) {
        // TODO Auto-generated method stub
        return null;
    }

}
