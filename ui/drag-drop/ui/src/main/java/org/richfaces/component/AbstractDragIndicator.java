package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

@JsfComponent(
        type = AbstractDragIndicator.COMPONENT_TYPE,
        family = AbstractDragIndicator.COMPONENT_FAMILY, 
        generate = "org.richfaces.component.UIDragIndicator",
        renderer = @JsfRenderer(type = "org.richfaces.DragIndicatorRenderer"),
        tag = @Tag(name="dragIndicator")
)
public abstract class AbstractDragIndicator extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.DragIndicator";

    public static final String COMPONENT_FAMILY = "org.richfaces.DragIndicator";    

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
    
    @Attribute
    public abstract String getAcceptClass();
    
    @Attribute
    public abstract String getRejectClass();
    
    @Attribute
    public abstract String getDraggingClass();
}
