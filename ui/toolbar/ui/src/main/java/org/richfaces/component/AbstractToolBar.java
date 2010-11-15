package org.richfaces.component;

import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.ToolBarRendererBase;

@JsfComponent(family = AbstractToolBar.COMPONENT_FAMILY, type = AbstractToolBar.COMPONENT_TYPE, 
        renderer=@JsfRenderer(type = ToolBarRendererBase.RENDERER_TYPE), tag = @Tag(name="toolBar")
)
public abstract class AbstractToolBar extends UIComponentBase {

    public static final String COMPONENT_TYPE = "org.richfaces.ToolBar";

    public static final String COMPONENT_FAMILY = "org.richfaces.ToolBar";
    
    @Attribute
    public abstract String getHeight();
    
    @Attribute
    public abstract String getWidth();
    
    @Attribute
    public abstract String getItemSeparator();
    
    @Attribute(events=@EventName("itemclick"))
    public abstract String getOnitemclick();

    @Attribute(events=@EventName("itemdblclick"))
    public abstract String getOnitemdblclick();
    
    @Attribute(events=@EventName("itemmousedown"))
    public abstract String getOnitemmousedown();
    
    @Attribute(events=@EventName("itemmouseup"))
    public abstract String getOnitemmouseup();
    
    @Attribute(events=@EventName("itemmouseover"))
    public abstract String getOnitemmouseover();
    
    @Attribute(events=@EventName("itemmousemove"))
    public abstract String getOnitemmousemove();
    
    @Attribute(events=@EventName("itemmouseout"))
    public abstract String getOnitemmouseout();
    
    @Attribute(events=@EventName("itemkeypress"))
    public abstract String getOnitemkeypress();

    @Attribute(events=@EventName("itemkeydown"))
    public abstract String getOnitemkeydown();

    @Attribute(events=@EventName("itemkeyup"))
    public abstract String getOnitemkeyup();
    
}
