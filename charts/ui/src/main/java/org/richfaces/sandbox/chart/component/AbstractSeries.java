package org.richfaces.sandbox.chart.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.sandbox.chart.model.ChartDataModel;


/**
 *
 * @author Lukas Macko
 */
@JsfComponent(
    type = "org.richfaces.sandbox.chart.component.series",
    family = "org.richfaces.sandbox.ChartFamily",
    tag = @Tag(name = "series"))
public abstract class AbstractSeries extends UIComponentBase{

    /**
     * 
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
    
    @Attribute
    public abstract String getLabel();
    
    @Attribute
    public abstract ChartDataModel getData();
    
    @Attribute
    public abstract String getColor();
    
    /**
      * 
      * Click handler event for this series only.  
      */
     @Attribute(events =
     @EventName("plotclick"))
     public abstract String getOnplotclick();
     
     
     /**
      * 
      * Mouse over handler event for this series only.
      */
     @Attribute(events =
     @EventName("mouseover"))
     public abstract String getOnmouseover();
     
     
     
     @Attribute
     //@Attribute(signature =
     //@Signature(parameters = DataClickEvent.class))
     public abstract MethodExpression getClickListener();
     
     /**
     * Server-side listener for mouseover event this series
     * 
     */ 
     @Attribute
     //@Attribute(signature =
     //@Signature(parameters = DataClickEvent.class))
     public abstract MethodExpression getMouseOverListener();
     
     
     /**
      * Point symbols for line chart
      */
     public enum SymbolType{
         circle,square,diamond,triangle,cross
     }
     
     
 
}
