package org.richfaces.component;

import java.util.ArrayList;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.ToolBarGroupRenderer;

@JsfComponent(family = AbstractToolBar.COMPONENT_FAMILY, type = AbstractToolBarGroup.COMPONENT_TYPE,
        renderer=@JsfRenderer(type = ToolBarGroupRenderer.RENDERER_TYPE), tag = @Tag(name="toolBarGroup"))
public abstract class AbstractToolBarGroup extends UIComponentBase {
    
    public static final String COMPONENT_TYPE = "org.richfaces.ToolBarGroup";
    
    @Attribute
    public abstract String getItemSeparator();
    
    @Attribute
    public abstract String getLocation();
    
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
    
    public AbstractToolBar getToolBar() {
        UIComponent component = this.getParent();
        if (component == null) {
            throw new FacesException("The component: " + this.getClientId(getFacesContext()) + 
                    " is not nested within " + AbstractToolBar.class.getSimpleName());
        } else if (!(component instanceof AbstractToolBar)) {
            throw new FacesException("The component: " + this.getClientId(getFacesContext()) + 
                    " is not a direct child of " + AbstractToolBar.class.getSimpleName());
        }
        return (AbstractToolBar) component;
    }
    
    public List<UIComponent> getRenderedChildren() {
        List<UIComponent> children = this.getChildren();
        List<UIComponent> renderedChildren = new ArrayList<UIComponent>(children.size());

        for (UIComponent child : children) {
            if (child.isRendered()) {
                renderedChildren.add(child);
            }
        }
        
        return renderedChildren;
    } 

}
