package org.richfaces.sandbox.watermark;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class WatermarkBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private Object date;

    private String text = "Watermark text";
    private Object watermarkedInput;

// --------------------- GETTER / SETTER METHODS ---------------------

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getWatermarkedInput() {
        return watermarkedInput;
    }

    public void setWatermarkedInput(Object watermarkedInput) {
        this.watermarkedInput = watermarkedInput;
    }

// -------------------------- OTHER METHODS --------------------------

    public Object submit() {
        final FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Text:" + getWatermarkedInput()));
        context.addMessage(null, new FacesMessage("Date:" + getDate()));
        return null;
    }
}
