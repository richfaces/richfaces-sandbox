package org.richfaces.component;

import javax.faces.component.UIOutput;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.renderkit.html.MenuGroupRendererBase;

@JsfComponent(family = AbstractDropDownMenu.COMPONENT_FAMILY, type = AbstractMenuGroup.COMPONENT_TYPE, 
    renderer=@JsfRenderer(type = MenuGroupRendererBase.RENDERER_TYPE), 
    tag = @Tag(name="menuGroup"),
    attributes = {"events-props.xml", "core-props.xml", "i18n-props.xml"})
public abstract class AbstractMenuGroup extends UIOutput {
    
    public static final String COMPONENT_TYPE = "org.richfaces.MenuGroup";

    @Attribute
    public abstract boolean isDisabled();

    @Attribute
    public abstract String getIcon();

    @Attribute
    public abstract String getIconDisabled();
    
    @Attribute
    public abstract String getIconFolder();
    
    @Attribute
    public abstract String getIconFolderDisabled();
    
    @Attribute
    public abstract String getStyleClass();

    @Attribute
    public abstract String getStyle();
    
    @Attribute(defaultValue = "auto")
    public abstract String getDirection();    
    
    @Attribute(defaultValue = "0")
    public abstract String getVerticalOffset();
    
    @Attribute(defaultValue = "0")
    public abstract String getHorizontalOffset();    
    
    @Attribute(events = @EventName("show"))
    public abstract String getOnshow();
    
    @Attribute(events = @EventName("hide"))
    public abstract String getOnhide();    
    
    public enum Facets {
        ICON("icon"), ICON_DISABLED("iconDisabled");
        
        private String facetName;
        private Facets(String name) {
            this.facetName = name;
        }
        
        @Override
        public String toString() {
            return facetName;
        }
    }    
}
