package org.richfaces.sandbox.chart;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * The class represents plotclick event fired 
 * by the chart component, when user clicks a point
 * in a chart.
 * @author Lukas Macko
 */
public class PlotClickEvent extends FacesEvent{
    /**
     * Index into chart series. The first
     * series has index 0.
     */
    private int seriesIndex;
    
    /**
     * Index into list of points inside series.
     * The first point has index 0.
     */
    private int pointIndex;
    
    /**
     * The value independent variable of the clicked point.
     * x-coordinate
     */
    private String x;
    
    /**
     * Dependent variable.
     * y-coordinate
     */
    private Number y;
    
    public PlotClickEvent(UIComponent component,int seriesIndex,int pointIndex, String x, Number y){
        super(component);
        this.seriesIndex=seriesIndex;
        this.pointIndex=pointIndex;
        this.x=x;
        this.y=y;
    }

    public int getSeriesIndex() {
        return seriesIndex;
    }

    public int getPointIndex() {
        return pointIndex;
    }

    public String getX() {
        return x;
    }

    public Number getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point with index " +getPointIndex()+
                "within series "+getSeriesIndex()+" was clicked.\n"+
                "Point coordinates ["+getX()+","+getY()+"]";
    }
    
    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return listener instanceof PlotClickListener;
    }

    @Override
    public void processListener(FacesListener listener) {
        ((PlotClickListener) listener).processDataClick(this);
    }
    
}
