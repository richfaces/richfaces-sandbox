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
package org.richfaces.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;

/**
 * @author Nick Belaevski
 * 
 */
public final class TreeDecoderHelper extends UIComponentBase {

    public static final String HELPER_ID = "__treeDecoderHelper";
    
    private AbstractTree tree;
    
    public TreeDecoderHelper(AbstractTree tree) {
        super();
        this.tree = tree;
        setId(HELPER_ID);
        setTransient(true);
    }

    @Override
    public boolean isInView() {
        return tree.isInView();
    }
    
    @Override
    public boolean isRendered() {
        return tree.isRendered();
    }
    
    @Override
    public String getFamily() {
        return null;
    }

    @Override
    public UIComponent getParent() {
        return tree;
    }

}
