package org.richfaces.sandbox.chart.component;

import org.richfaces.cdk.annotations.*;
/**
 * The &lt;lm:xaxis&lt; tag 
 * @author Lukas Macko
 */
@JsfComponent(
        type = "org.richfaces.sandbox.chart.component.xaxis",
        family = "org.richfaces.sandbox.ChartFamily",
        tag = @Tag(name="xaxis"))
abstract public class AbstractXaxis extends javax.faces.component.UIComponentBase implements AxisAttributes{

}
