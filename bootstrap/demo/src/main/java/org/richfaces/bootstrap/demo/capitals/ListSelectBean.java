package org.richfaces.bootstrap.demo.capitals;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import java.util.List;

@ManagedBean
@RequestScoped
public class ListSelectBean {
    @ManagedProperty(value = "#{capitalsParser.capitalsList}")
    private List<Capital> capitals;
    private List<Capital> selectedCapitals;

    public List<Capital> getCapitals() {
        return capitals;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }

    public List<Capital> getSelectedCapitals() {
        return selectedCapitals;
    }

    public void setSelectedCapitals(List<Capital> selectedCapitals) {
        this.selectedCapitals = selectedCapitals;
    }

    public String getCapitalsString() {
        StringBuilder sb = new StringBuilder();
        for (Capital capital : capitals) {
            sb.append(capital.getName()).append(" ");
        }
        return sb.toString().trim();
    }
}
