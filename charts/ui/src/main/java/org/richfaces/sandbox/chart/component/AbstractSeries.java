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

    @Attribute
    public abstract String getType();
    
    @Attribute
    public abstract String getSymbol();
    
    @Attribute
    public abstract String getLabel();
    
    @Attribute
    public abstract ChartDataModel getData();
    
    @Attribute
    public abstract String getColor();
    
    /**
      * 
      * Click handler event for this series  
      */
     @Attribute(events =
     @EventName("click"))
     public abstract String getOnClick();
     
     
     /**
      * 
      * Mouse over handler event for this series  
      */
     @Attribute(events =
     @EventName("mouseover"))
     public abstract String getOnMouseOver();
     
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
     
 
}
