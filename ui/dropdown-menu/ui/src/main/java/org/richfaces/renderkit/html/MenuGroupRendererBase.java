package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.renderkit.RendererBase;

public abstract class MenuGroupRendererBase extends RendererBase {
    
    public static final String RENDERER_TYPE = "org.richfaces.MenuGroupRenderer";
    
    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {

    }
    
    protected boolean isDisabled(UIComponent component) {
        if (component instanceof AbstractMenuGroup) {
            return ((AbstractMenuGroup) component).isDisabled();
        }
        return false;
    }
    
    @Override
    public void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractMenuGroup menuGroup = (AbstractMenuGroup) component;

        for (UIComponent child : menuGroup.getChildren()) {
            if (child.isRendered()) {
                child.encodeAll(facesContext);
            }
        }
    }
    
    protected UIComponent getIconFacet(UIComponent component) {
        UIComponent facet = null;
        AbstractMenuGroup menuGroup = (AbstractMenuGroup) component; 
        if (menuGroup != null) {
            
            if (menuGroup.isDisabled()) {
                facet = menuGroup.getFacet(AbstractMenuGroup.Facets.ICON_DISABLED.toString());
            } else {
                facet = menuGroup.getFacet(AbstractMenuGroup.Facets.ICON.toString());
            }
        }
        return facet;   
    }     
    
    protected String getIconAttribute(UIComponent component) {
        String icon = null;
        AbstractMenuGroup menuGroup = (AbstractMenuGroup) component; 
        if (menuGroup != null) {
            
            if (menuGroup.isDisabled()) {
                icon = menuGroup.getIconDisabled();
            } else {
                icon = menuGroup.getIcon();
            }
        }
        return icon;   
    }
    
    protected String getStyleClass(UIComponent component, String styleDDMenu, String styleMenuGroup) {
        UIComponent ddMenu = getDDMenu(component);
        String styleClass = "";
        if (ddMenu != null) {
            if (ddMenu.getAttributes().get(styleDDMenu) != null) {
                styleClass = ddMenu.getAttributes().get(styleDDMenu).toString();
            }
        }

        return concatClasses(styleClass, component.getAttributes().get(styleMenuGroup));
    }
    
    protected UIComponent getDDMenu(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof AbstractDropDownMenu) {
                return parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
