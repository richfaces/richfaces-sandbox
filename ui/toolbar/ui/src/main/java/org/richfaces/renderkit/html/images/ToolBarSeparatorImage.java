package org.richfaces.renderkit.html.images;

import java.awt.Color;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.richfaces.resource.ImageType;
import org.richfaces.resource.Java2DUserResource;
import org.richfaces.resource.PostConstructResource;
import org.richfaces.resource.ResourceParameter;
import org.richfaces.resource.StateHolderResource;
import org.richfaces.skin.Skin;
import org.richfaces.skin.SkinFactory;

public abstract class ToolBarSeparatorImage implements Java2DUserResource, StateHolderResource {

    private int headerBackgroundColor;
    private int separatorHeight;
    private int headerGradientColor;

    public int getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(int bgColor) {
        this.headerBackgroundColor = bgColor;
    }

    public int getSeparatorHeight() {
        return separatorHeight;
    }

    @ResourceParameter(defaultValue = "9")
    public void setSeparatorHeight(int separatorHeight) {
        this.separatorHeight = separatorHeight;
    }

    public int getHeaderGradientColor() {
        return headerGradientColor;
    }

    public void setHeaderGradientColor(int headerGradientColor) {
        this.headerGradientColor = headerGradientColor;
    }

    public void writeState(FacesContext context, DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(headerBackgroundColor);
        dataOutput.writeInt(separatorHeight);
        dataOutput.writeInt(headerGradientColor);
    }

    public void readState(FacesContext context, DataInput dataInput) throws IOException {
        this.headerBackgroundColor = dataInput.readInt();
        this.separatorHeight = dataInput.readInt();
        this.headerGradientColor = dataInput.readInt();
    }

    public boolean isTransient() {
        return false;
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

    @PostConstructResource
    public final void initialize() {
        FacesContext context = FacesContext.getCurrentInstance();
        Skin skin = SkinFactory.getInstance(context).getSkin(context);
        Skin defaultSkin = SkinFactory.getInstance(context).getDefaultSkin(context);

        String skinParameter = "headerBackgroundColor";
        String tmp = (String) skin.getParameter(context, skinParameter);
        if (null == tmp || "".equals(tmp)) {
            tmp = (String) defaultSkin.getParameter(context, skinParameter);
        }
        this.setHeaderBackgroundColor(Color.decode(tmp == null ? "#224986" : tmp).getRGB());

        skinParameter = "headerGradientColor";
        tmp = (String) skin.getParameter(context, skinParameter);
        if (null == tmp || "".equals(tmp)) {
            tmp = (String) defaultSkin.getParameter(context, skinParameter);
        }
        this.setHeaderGradientColor(Color.decode(tmp == null ? "#CCCCFF" : tmp).getRGB());
    }
}
