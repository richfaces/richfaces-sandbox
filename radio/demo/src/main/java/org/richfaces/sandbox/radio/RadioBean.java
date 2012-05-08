package org.richfaces.sandbox.radio;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean
public class RadioBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private List<String> options = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    private Map<String, String> optionsMap = new HashMap<String, String>();

    private Object selectedOption;

// --------------------------- CONSTRUCTORS ---------------------------

    public RadioBean() {
        optionsMap.put("first", "Master");
        optionsMap.put("second", "Looser");
        optionsMap.put("third", "The rest of the loosers");
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<String> getOptions() {
        return options;
    }

    public Map<String, String> getOptionsMap() {
        return optionsMap;
    }

    public Object getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Object selectedOption) {
        this.selectedOption = selectedOption;
    }
}
