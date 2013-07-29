package org.richfaces.sandbox.chart.model;

/**
 *
 * @author Lukas Macko
 */
public class LineChartDataModel extends ChartDataModel<Number, Number>{
    
    public LineChartDataModel(){
        super();
        strategy = new LineStrategy();
    }

    @Override
    public Class getKeyType() {
        return Number.class;
    }

    @Override
    public Class getValueType() {
        return Number.class;
    }
    
}
