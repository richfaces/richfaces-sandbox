package org.richfaces.sandbox.chart.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.sandbox.chart.PlotClickEvent;
import org.richfaces.sandbox.chart.model.ChartDataModel;


/**
 * The &lt;s:series&lt; defines the data to be plotted in a chart. It represents
 * the set of values with a common label. Data can be passed using attribute data.
 * It expects ChartDataModel object. You can also use facelet iteration.
 * 
 * @author Lukas Macko
 */
@JsfComponent(
    type = "org.richfaces.sandbox.chart.component.series",
    family = "org.richfaces.sandbox.ChartFamily",
    tag = @Tag(name = "series"))
public abstract class AbstractSeries extends UIComponentBase{

    /**
     * The attributes define type of a chart.
     * Allowed values:
     * <ul>
     * <li>line</li>
     * <li>bar</li>
     * <li>pie</li>
     * </ul>
     * 
     */
    @Attribute(required = true)
    public abstract ChartDataModel.ChartType getType();
    
    /**
     * Point symbol for line chart
     * Allowed values:
     * <ul>
     * <li>circle</li>
     * <li>square</li>
     * <li>cross</li>
     * <li>triangle</li>
     * <li>diamond</li>
     * </ul>
     */
    @Attribute
    public abstract SymbolType getSymbol();
    
    /**
     * Description of data shown in a legend.
     * 
     */
    @Attribute
    public abstract String getLabel();
    
    /**
     * Data passed into chart. If attribute is null, nested &lt;s:point&gt;
     * tags are expected - facelet iteration.
     */
    @Attribute
    public abstract ChartDataModel getData();
    
    /**
     * Attribute define the color of data plotted.
     * 
     */
    @Attribute
    public abstract String getColor();
    
    /**
      * 
      * Javascript handler for plotclick event for this series only.  
      */
     @Attribute(events =
     @EventName("plotclick"))
     public abstract String getOnplotclick();
     
     
     /**
      * 
      * Mouse over handler event for this series only.
      */
     @Attribute(events =
     @EventName("plothover"))
     public abstract String getOnplothover();
     
     
     

     /**
      * 
      * Server-side listener for plotclick event fired by this series only.
      * Not implemented yet.
      */
     @Attribute(signature =
     @Signature(parameters = PlotClickEvent.class))
     public abstract MethodExpression getClickListener();

     
     /**
      * Point symbols for line chart
      */
     public enum SymbolType{
         circle,square,diamond,triangle,cross
     }
     
     
 
}
