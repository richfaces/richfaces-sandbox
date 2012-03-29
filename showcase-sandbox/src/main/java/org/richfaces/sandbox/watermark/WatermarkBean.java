package org.richfaces.sandbox.watermark;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.Serializable;

@SessionScoped
@ManagedBean
public class WatermarkBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private Converter converter = new MyConverter();

    private Object date;

    private String text = "Watermark text";

    private TextObject textObject = new TextObject();

    private Object watermarkedInput;

// --------------------- GETTER / SETTER METHODS ---------------------

    public Converter getConverter() {
        return converter;
    }

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

    public TextObject getTextObject() {
        return textObject;
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

// -------------------------- INNER CLASSES --------------------------

    private class MyConverter implements Converter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return new TextObject();
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            if (!(value instanceof TextObject)) {
                throw new ConverterException(String.format("%s is not instance of %s", value.getClass().getCanonicalName(), TextObject.class.getCanonicalName()));
            }
            return ((TextObject) value).getText();
        }
    }

    private class TextObject {
// --------------------- GETTER / SETTER METHODS ---------------------

        String getText() {
            return text;
        }
    }
}
