package org.richfaces.sandbox.chart.component;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.Event;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;


import org.richfaces.sandbox.chart.PlotClickEvent;
import org.richfaces.sandbox.chart.PlotClickListener;


/**
 *
 * @author Lukas Macko
 */
@JsfComponent(
    type = "org.richfaces.sandbox.chart.component.Chart",
    family = "org.richfaces.sandbox.ChartFamily",
    renderer = @JsfRenderer(type = "org.richfaces.sanbox.ChartRenderer"),
    tag = @Tag(name = "chart", generate = true,type = TagType.Facelets),
    fires = {
            @Event(value = PlotClickEvent.class, listener = PlotClickListener.class)})
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
     public abstract String getOnmouseover();
     
     /**
      * Complementary event for mouseover fired
      * when mouse leaves grid.
      */
     @Attribute(events =
     @EventName("mouseout"))
     public abstract String getOnmouseout();
     
     
     @Attribute(signature =
     @Signature(parameters = PlotClickEvent.class))
     public abstract MethodExpression getClickListener();

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        
        
        if(event instanceof PlotClickEvent){
            FacesContext context = getFacesContext();
            MethodExpression expression = getClickListener();
            
            if(expression!=null){
                expression.invoke(context.getELContext(), new Object[]{event});
            }
        }
        super.broadcast(event); 
    }
     
     
     
    
    
}
