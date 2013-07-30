package org.richfaces.sandbox.chart.component;

import org.richfaces.cdk.annotations.*;
/**
 * The &lt;lm:yaxis&lt; tag 
 * @author Lukas Macko
 */
@JsfComponent(
        type = "org.richfaces.sandbox.chart.component.point",
        family = "org.richfaces.sandbox.ChartFamily",
        tag = @Tag(name="point"))
abstract public class AbstractPoint extends javax.faces.component.UIComponentBase implements AxisAttributes{

    @Attribute(required = true)
    public abstract Object getX();
    
    
    @Attribute(required = true)
    public abstract Object getY();
    
}

