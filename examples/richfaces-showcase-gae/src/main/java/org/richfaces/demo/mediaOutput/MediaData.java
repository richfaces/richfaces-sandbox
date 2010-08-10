package org.richfaces.demo.mediaOutput;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import java.io.Serializable;

@ManagedBean(name = "mediaData")
@SessionScoped
public class MediaData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int height = 120;
    int width = 300;
    Integer color;
    float scale;
    String text;

    public MediaData() {
        setText("RichFaces 4.0");
        setColor(1000);
        setScale(2);
        System.out.println("MediaData instantiated");
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
