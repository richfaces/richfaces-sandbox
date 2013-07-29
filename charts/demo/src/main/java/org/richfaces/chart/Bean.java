package org.richfaces.chart;

import java.io.Serializable;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.richfaces.sandbox.chart.model.LineChartDataModel;

@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {
    
    LineChartDataModel model;
    
    @PostConstruct
    public void init(){
        model = new LineChartDataModel();
        model.put(1, 3);
        model.put(2, 6);
        model.put(4, 5);
    }

    public LineChartDataModel getModel() {
        return model;
    }
    
    

}
