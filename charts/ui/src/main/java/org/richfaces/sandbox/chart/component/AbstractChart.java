package org.richfaces.sandbox.chart.component;

import javax.faces.component.UIComponentBase;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;


/**
 *
 * @author lukas
 */
@JsfComponent(
        type = "org.richfaces.sandbox.chart.component.Chart",
        family = "org.richfaces.sandbox.ChartFamily",
        renderer = @JsfRenderer(type = "org.richfaces.sanbox.ChartRenderer"),
        tag = @Tag(name = "chart", generate = true,type = TagType.Facelets))
public abstract class AbstractChart extends UIComponentBase{

 

    
    
}
