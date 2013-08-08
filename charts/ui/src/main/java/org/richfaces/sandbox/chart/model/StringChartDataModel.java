package org.richfaces.sandbox.chart.model;


/**
 *
 * @author Lukas Macko
 */
public class StringChartDataModel extends ChartDataModel<String, Number>{
    
    public StringChartDataModel(ChartType type){
        super(type);
        switch(type){
            case bar:
                strategy = new CategoryBarStrategy();
                break;
            case pie:
                strategy = new PieStrategy();
                break;
            default:
                throw new IllegalArgumentException(type + "not supported by StringChartDataModel" );
                
        }
    }

    @Override
    public Class getKeyType() {
        return String.class;
    }

    @Override
    public Class getValueType() {
        return Number.class;
    }
    
}
