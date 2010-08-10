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
package org.richfaces.cdk.skin;

import javax.faces.context.FacesContext;

import org.richfaces.skin.AbstractSkinFactory;
import org.richfaces.skin.Skin;
import org.richfaces.skin.Theme;

/**
 * @author Nick Belaevski
 * 
 */
public class SkinFactoryImpl extends AbstractSkinFactory {

    private static ThreadLocal<String> skinNames = new ThreadLocal<String>();

    public static void setSkinName(String skinName) {
        skinNames.set(skinName);
    }
    
    @Override
    public Skin getDefaultSkin(FacesContext context) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Skin getSkin(FacesContext context) {
        return getSkin(context, skinNames.get());
    }

    /* (non-Javadoc)
     * @see org.richfaces.skin.SkinFactory#getBaseSkin(javax.faces.context.FacesContext)
     */
    @Override
    public Skin getBaseSkin(FacesContext facesContext) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.richfaces.skin.SkinFactory#getTheme(javax.faces.context.FacesContext, java.lang.String)
     */
    @Override
    public Theme getTheme(FacesContext facesContext, String name) {
        // TODO Auto-generated method stub
        return null;
    }

}
