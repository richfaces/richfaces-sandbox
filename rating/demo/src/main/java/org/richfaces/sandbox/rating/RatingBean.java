package org.richfaces.sandbox.rating;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@ManagedBean
public class RatingBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private int intValue;
    private String stringValue;
    private List<String> values = Arrays.asList("Aragorn", "Sam", "Frodo");

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public List<String> getValues() {
        return values;
    }
}
