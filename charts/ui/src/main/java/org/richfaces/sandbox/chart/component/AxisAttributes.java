package org.richfaces.sandbox.chart.component;

import org.richfaces.cdk.annotations.Attribute;

/**
 * 
 * @author Lukas Macko
 */
public interface AxisAttributes {
    
   
    
    /**
     * Format for axis ticks (Date series only)
     */
    @Attribute
    public abstract String getFormat();
    
    /*
     * Text shown next to axis.
     */
    @Attribute
    public abstract String getLabel();
    
    /**
     * Minimum value shown on the axis.
     */
    @Attribute
    public abstract String getMin();
    
    /**
     * Maximum value of the axis
     */
    @Attribute
    public abstract String getMax();
    
    /*
     * It’s the fraction of margin that the scaling algorithm will add to avoid that
     * the outermost points ends up on the grid border. 
     */
    @Attribute
    public abstract Double getPad();
    
    
    
}
