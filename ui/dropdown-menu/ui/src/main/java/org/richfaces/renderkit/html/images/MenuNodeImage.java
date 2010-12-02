/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.DynamicUserResource;
import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

@DynamicUserResource
public class MenuNodeImage implements Java2DUserResource, StateHolderResource {

    private static final Dimension DIMENSIONS = new Dimension(7, 14);
    
    private Integer disabledColor;
    private Integer generalColor;
    
    public Integer getDisabledColor() {
        return disabledColor;
    }
    
    public void setDisabledColor(Integer disabledColor) {
        this.disabledColor = disabledColor;
    }
    
    public Integer getGeneralColor() {
        return generalColor;
    }
    
    public void setGeneralColor(Integer generalColor) {
        this.generalColor = generalColor;
    }
    
    public boolean isTransient() {
        return false;
    }
    
    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(disabledColor);
        dataOutput.writeInt(generalColor);
    }
    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.disabledColor = dataInput.readInt();
        this.generalColor = dataInput.readInt();
    }
    public Map<String, String> getResponseHeaders() {
        return null;
    }
    public Date getLastModified() {
        return null;
    }
    public ImageType getImageType() {
        return ImageType.GIF;
    }
    public Dimension getDimension() {
        return DIMENSIONS;
    }
    public void paint(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(getGeneralColor().intValue()));
        drawTriangle(g2d, 2, 0);
        
        g2d.setColor(new Color(getDisabledColor().intValue()));
        drawTriangle(g2d, 2, DIMENSIONS.height / 2);
    }
    
    private void drawTriangle(Graphics2D g2d, int startPosX, int startPosY) {
        int dTriangle = 6;
        for (int i = 0; i < 4; i++) {
            g2d.drawLine(startPosX + i, startPosY + i, startPosX + i, startPosY + dTriangle - i);    
        }
    }
    
    @PostConstructResource
    public final void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        String skinParameter = "generalTextColor";
        String tmp = (String) skin.getParameter(context, skinParameter);
        if (null == tmp || "".equals(tmp)) {
            tmp = (String) defaultSkin.getParameter(context, skinParameter);
        }
        
        this.setGeneralColor(Color.decode(tmp == null ? "#4A75B5" : tmp).getRGB());

        skinParameter = "tabDisabledTextColor";
        tmp = (String) skin.getParameter(context, skinParameter);
        if (null == tmp || "".equals(tmp)) {
            tmp = (String) defaultSkin.getParameter(context, skinParameter);
        }
        this.setDisabledColor(Color.decode(tmp == null ? "#6A92CF" : tmp).getRGB());
    }    
}
