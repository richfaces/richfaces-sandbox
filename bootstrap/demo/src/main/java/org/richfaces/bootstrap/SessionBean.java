package org.richfaces.bootstrap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "sessionBean")
@SessionScoped
@SuppressWarnings("serial")
public class SessionBean implements Serializable {
    private boolean fluid = true;

    public boolean isFluid() {
        return fluid;
    }

    public void setFluid(boolean fluid) {
        this.fluid = fluid;
    }
    
    private List<String> selectedValues;
    private List<String> availableValues;

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(List<String> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public List<String> getAvailableValues() {
        if(availableValues == null) {
            availableValues = new ArrayList<String>();
            availableValues.add("Value1");
            availableValues.add("Value2");
            availableValues.add("Value3");
            availableValues.add("Value4");
        }
        
        return availableValues;
    }

    public void setAvailableValues(List<String> availableValues) {
        this.availableValues = availableValues;
    }
    
    
}
