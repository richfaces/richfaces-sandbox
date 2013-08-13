package org.richfaces.sandbox.chart.model;

import java.util.Date;


/**
 *
 * @author Lukas Macko
 */
public class DateChartDataModel extends ChartDataModel<Date, Number>{
    
    public DateChartDataModel(ChartType type){
        super(type);
        switch(type){
            case line:
                strategy = new DateLineStrategy();
                break;
            default:
                throw new IllegalArgumentException(type + "not supported by StringChartDataModel" );
                
        }
    }

    @Override
    public Class getKeyType() {
        return Date.class;
    }

    @Override
    public Class getValueType() {
        return Number.class;
    }
    
}
