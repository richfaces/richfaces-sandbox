package org.richfaces.sandbox.colorpicker;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.awt.*;
import java.io.Serializable;

@SessionScoped
@ManagedBean
public class ColorpickerBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    public Color objectColor = Color.MAGENTA;
    public String stringColor = "#aaFFaa";
    private ColorConverter colorConverter = new ColorConverter();

// -------------------------- STATIC METHODS --------------------------

    public static Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16));
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public ColorConverter getColorConverter() {
        return colorConverter;
    }

    public Color getObjectColor() {
        return objectColor;
    }

    public void setObjectColor(Color objectColor) {
        this.objectColor = objectColor;
    }

    public String getStringColor() {
        return stringColor;
    }

    public void setStringColor(String stringColor) {
        this.stringColor = stringColor;
    }

// -------------------------- INNER CLASSES --------------------------

    public class ColorConverter implements Converter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            return hex2Rgb(value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return "#" + Integer.toHexString(((Color) value).getRGB() & 0x00ffffff);
        }
    }
}
