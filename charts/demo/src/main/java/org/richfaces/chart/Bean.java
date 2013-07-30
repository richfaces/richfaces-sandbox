package org.richfaces.chart;

import java.io.Serializable;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.richfaces.sandbox.chart.model.ChartDataModel;
import org.richfaces.sandbox.chart.model.NumberChartDataModel;


@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {
    
    NumberChartDataModel model;
    NumberChartDataModel model2;
    NumberChartDataModel barModel;
    
    @PostConstruct
    public void init(){
        model = new NumberChartDataModel(ChartDataModel.ChartType.line);
        model.put(1, 3);
        model.put(2, 6);
        model.put(4, 5);
        
        model2 = new NumberChartDataModel(ChartDataModel.ChartType.line);
        model2.put(2, 3);
        model2.put(3, 6);
        model2.put(5, 5);
    
        barModel = new NumberChartDataModel(ChartDataModel.ChartType.bar);
        barModel.put(2,3);
        barModel.put(4,6);
                
    }

    public NumberChartDataModel getModel() {
        return model;
    }

    public NumberChartDataModel getModel2() {
        return model2;
    }

    public NumberChartDataModel getBarModel() {
        return barModel;
    }
    
    
    
    
    
    
    

}
