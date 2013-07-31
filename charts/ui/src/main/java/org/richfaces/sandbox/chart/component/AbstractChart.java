package org.richfaces.sandbox.chart.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;


/**
 *
 * @author Lukas Macko
 */
@JsfComponent(
    type = "org.richfaces.sandbox.chart.component.Chart",
    family = "org.richfaces.sandbox.ChartFamily",
    renderer = @JsfRenderer(type = "org.richfaces.sanbox.ChartRenderer"),
    tag = @Tag(name = "chart", generate = true,type = TagType.Facelets))
public abstract class AbstractChart extends UIComponentBase{

 
     /**
      * 
      * Chart title 
      */
     @Attribute
     public abstract String getTitle();
     
     /**
      * 
      * Is zooming enable 
      */
     @Attribute
     public abstract boolean isZoom();
     
     /**
      * 
      * Click handler event for each series.  
      */
     @Attribute(events =
     @EventName("plotclick"))
     public abstract String getOnplotclick();
     
     
     /**
      * 
      * Mouse over handler event for each series  
      */
     @Attribute(events =
     @EventName("mouseover"))
     public abstract String getOnMouseOver();
     
     @Attribute
     //@Attribute(signature =
     //@Signature(parameters = DataClickEvent.class))
     public abstract MethodExpression getClickListener();
     
     /**
     * Server-side listener for mouseover event each series
     * 
     */ 
     @Attribute
     //@Attribute(signature =
     //@Signature(parameters = DataClickEvent.class))
     public abstract MethodExpression getMouseOverListener();
     
    
    
}
