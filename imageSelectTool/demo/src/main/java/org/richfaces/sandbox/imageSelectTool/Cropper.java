package org.richfaces.sandbox.imageSelectTool;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.awt.*;
import java.io.Serializable;

@SessionScoped
@ManagedBean
public class Cropper implements Serializable {

    private String srcImage = "images/sample.jpg";
    private Double aspectRatio;
    private String backgroundColor;
    private Double backgroundOpacity;
    private Integer maxHeight;
    private Integer maxWidth;
    private Integer minHeight;
    private Integer minWidth;
    private Rectangle selection;

    public String getSrcImage() {
        return srcImage;
    }

    public void setSrcImage(String srcImage) {
        this.srcImage = srcImage;
    }

    public Double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Double getBackgroundOpacity() {
        return backgroundOpacity;
    }

    public void setBackgroundOpacity(Double backgroundOpacity) {
        this.backgroundOpacity = backgroundOpacity;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Rectangle getSelection() {
        return selection;
    }

    public void setSelection(Rectangle selection) {
        this.selection = selection;
    }
}
