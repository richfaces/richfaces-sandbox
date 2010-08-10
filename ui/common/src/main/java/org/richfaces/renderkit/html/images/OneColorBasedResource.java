/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
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

package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.ajax4jsf.resource.GifRenderer;
import org.ajax4jsf.resource.InternetResourceBase;
import org.ajax4jsf.resource.InternetResourceBuilder;
import org.ajax4jsf.resource.Java2Dresource;
import org.ajax4jsf.resource.ResourceContext;
import org.ajax4jsf.util.HtmlColor;
import org.ajax4jsf.util.Zipper2;
import org.richfaces.skin.SkinFactory;

public abstract class OneColorBasedResource extends Java2Dresource {

    private Dimension dimension;

    private String basicColorParamName;

    private Color basicColor;

    public OneColorBasedResource(int width, int height, final String basicColorParamName) {
        this.basicColorParamName = basicColorParamName;
        this.dimension = new Dimension(width, height);
        setRenderer(new GifRenderer());
        setLastModified(new Date(InternetResourceBuilder.getInstance().getStartTime()));
    }

    /**
     * @see Java2Dresource#getDimensions(ResourceContext)
     */
    protected Dimension getDimensions(ResourceContext resourceContext) {
        return dimension;
    }

    /**
     * @see Java2Dresource#getDimensions(FacesContext, Object)
     */
    public Dimension getDimensions(FacesContext facesContext, Object data) {
        return dimension;
    }

    /**
     * @see Java2Dresource#isCacheable(ResourceContext)
     */
    public boolean isCacheable(ResourceContext ctx) {
        return true;
    }

    /**
     * Gets value of basicColor field.
     * @return value of basicColor field
     */
    public Color getBasicColor() {
        return basicColor;
    }

    /**
     * @see InternetResourceBase#getDataToStore(FacesContext, Object)
     */
    protected Object getDataToStore(FacesContext context, Object data) {
        byte[] retVal = null;
        if (basicColor == null) {
            basicColor = getColorValueParameter(context, basicColorParamName);
        }

        if (basicColor != null) {
            retVal = new byte[3 * 1];
            new Zipper2(retVal).addColor(basicColor);

            return retVal;
        } else {
            return null;
        }
    }

    /**
     * @see InternetResourceBase#deserializeData(byte[])
     */
    protected Object deserializeData(byte[] objectArray) {
        if (objectArray != null) {
            Zipper2 zipper2 = new Zipper2(objectArray);
            basicColor = zipper2.nextColor();
        }

        return objectArray;
    }

    private Color getColorValueParameter(FacesContext context, String name) {
        Color retVal = null;
        String color = (String) SkinFactory.getInstance().getSkin(context).getParameter(context, name);
        if (color != null && !color.trim().equals("")) {
            retVal = HtmlColor.decode(color);
        }
        return retVal;
    }
}
