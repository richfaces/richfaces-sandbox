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

package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Konstantin Mishin
 * 
 */
@DynamicResource
public abstract class SliderArrowBase implements Java2DUserResource, StateHolderResource {

    private Dimension dimension =  new Dimension(7, 7);
    private int color;
    private String colorName = Skin.GENERAL_TEXT_COLOR;

    private void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        this.color = skin.getColorParameter(context, colorName);
    }

    protected final void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public Dimension getDimension() {
        return dimension;
    }
    public void paint(Graphics2D graphics2d, Dimension dimension) {
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setColor(new Color(color));
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        initialize();
        dataOutput.writeInt(this.color);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.color = dataInput.readInt();
    }
    
    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    public ImageType getImageType() {
        return ImageType.PNG;
    }

    public boolean isTransient() {
        return false;
    }

}
