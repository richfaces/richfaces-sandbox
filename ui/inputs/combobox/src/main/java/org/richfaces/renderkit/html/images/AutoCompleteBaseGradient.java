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
package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.CacheableResource;
import org.richfaces.resource.DynamicResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 * 
 */
@DynamicResource
public abstract class AutoCompleteBaseGradient implements Java2DUserResource, CacheableResource, StateHolderResource {

    private static final Dimension DIMENSION = new Dimension(18, 8);
  
    private String topColorSkinParameter;
    
    private String bottomColorSkinParameter;
    
    private Color topColor;
    
    private Color bottomColor;
    
    public Map<String, String> getResponseHeaders() {
        return null;
    }

    public Date getLastModified() {
        return null;
    }

    public ImageType getImageType() {
        return ImageType.PNG;
    }

    public Dimension getDimension() {
        return DIMENSION;
    }

    public void paint(Graphics2D graphics2d, Dimension dimension) {
        GradientPaint paint = new GradientPaint(0, 0, topColor, 0, dimension.height, bottomColor);
        graphics2d.setPaint(paint);
        graphics2d.fill(new Rectangle(dimension));
    }

    public boolean isCacheable(FacesContext context) {
        return true;
    }

    public Date getExpires(FacesContext context) {
        return null;
    }

    public int getTimeToLive(FacesContext context) {
        return 0;
    }

    public String getEntityTag(FacesContext context) {
        return null;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        Skin skin = SkinFactory.getInstance().getSkin(context);
        
        Integer topColor = skin.getColorParameter(context, topColorSkinParameter);
        Integer bottomColor = skin.getColorParameter(context, bottomColorSkinParameter);
        
        dataOutput.writeInt(topColor);
        dataOutput.writeInt(bottomColor);
    }
    
    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        topColor = new Color(dataInput.readInt());
        bottomColor = new Color(dataInput.readInt());
    }
 
    public boolean isTransient() {
        return false;
    }
    
    protected void setTopColorSkinParameter(String topColorSkinParameter) {
        this.topColorSkinParameter = topColorSkinParameter;
    }
    
    protected void setBottomColorSkinParameter(String bottomColorSkinParameter) {
        this.bottomColorSkinParameter = bottomColorSkinParameter;
    }
}
