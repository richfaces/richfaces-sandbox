package org.richfaces.demo.tables;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.richfaces.demo.tables.model.capitals.Capital;

@ManagedBean
@SessionScoped
public class CapitalsBean implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -1509108399715814302L;
    @ManagedProperty(value = "#{capitalsParser.capitalsList}")
    private List<Capital> capitals;

    public CapitalsBean() {
        // TODO Auto-generated constructor stub
    }

    public List<Capital> getCapitals() {
        return capitals;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

}
