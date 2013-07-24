package org.richfaces.sandbox.chart.component;

import org.richfaces.cdk.annotations.*;
/**
 * The &lt;lm:yaxis&lt; tag 
 * @author Lukas Macko
 */
@JsfComponent(
        type = "org.richfaces.sandbox.chart.component.yaxis",
        family = "org.richfaces.sandbox.ChartFamily",
        tag = @Tag(name="yaxis"))
abstract public class AbstractYaxis extends javax.faces.component.UIComponentBase implements AxisAttributes{

}

