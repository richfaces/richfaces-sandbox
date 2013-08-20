package org.richfaces.sandbox.chart.component;

import org.richfaces.cdk.annotations.*;
/**
 * The &lt;s:point&lt; tag defines the value of point. It is
 * supposed to be used inside the &lt;s:series&lt; tag.
 * @author Lukas Macko
 */
@JsfComponent(
        type = "org.richfaces.sandbox.chart.component.point",
        family = "org.richfaces.sandbox.ChartFamily",
        tag = @Tag(name="point"))
abstract public class AbstractPoint extends javax.faces.component.UIComponentBase implements AxisAttributes{

    /**
     * Value plotted on x-axis.
     */
    @Attribute(required = true)
    public abstract Object getX();
    
    /**
     * Value plotted on y-axis.
     */
    @Attribute(required = true)
    public abstract Object getY();
    
}

