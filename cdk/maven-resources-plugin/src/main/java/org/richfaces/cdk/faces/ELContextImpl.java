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

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;

import org.richfaces.skin.SkinPropertiesELResolver;

/**
 * @author Nick Belaevski
 * 
 */
public class ELContextImpl extends ELContext {

    private VariableMapper variableMapper = createVariableMapper();
    
    private ELResolver elResolver = createELResolver();
    
    private VariableMapper createVariableMapper() {
        return new VariableMapperImpl();
    }

    private ELResolver createELResolver() {
        CompositeELResolver result = new CompositeELResolver();

        result.add(new SkinPropertiesELResolver());
        result.add(new ResourceELResolver());

        return result;
    }

    /* (non-Javadoc)
     * @see javax.el.ELContext#getELResolver()
     */
    @Override
    public ELResolver getELResolver() {
        return elResolver;
    }

    /* (non-Javadoc)
     * @see javax.el.ELContext#getFunctionMapper()
     */
    @Override
    public FunctionMapper getFunctionMapper() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }

}
