package org.richfaces.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.renderkit.RendererBase;

public abstract class MenuGroupRendererBase extends RendererBase {
    
    public static final String RENDERER_TYPE = "org.richfaces.MenuGroupRenderer";
    
    public static final int DEFAULT_MIN_POPUP_WIDTH = 250;
    
    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {

    }
    
    protected boolean isDisabled(FacesContext facesContext, UIComponent component) {
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
    
    protected UIComponent getIconFacet(FacesContext facesContext, UIComponent component) {
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
    
    protected String getIconAttribute(FacesContext facesContext, UIComponent component) {
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
    
    protected String getStyleClass(FacesContext facesContext, UIComponent component, String styleDDMenu, String styleMenuGroup) {
        UIComponent ddMenu = getDDMenu(facesContext, component);
        String styleClass = "";
        if (ddMenu != null) {
            if (ddMenu.getAttributes().get(styleDDMenu) != null) {
                styleClass = ddMenu.getAttributes().get(styleDDMenu).toString();
            }
        }

        return concatClasses(styleClass, component.getAttributes().get(styleMenuGroup));
    }
    
    protected UIComponent getDDMenu(FacesContext facesContext, UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof AbstractDropDownMenu) {
                return parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
    
    protected int getMinPopupWidth(FacesContext facesContext, UIComponent component) {
        UIComponent parent = getDDMenu(facesContext, component);
        if (parent != null) {
            return ((AbstractDropDownMenu) parent).getPopupWith();
        }
        return DEFAULT_MIN_POPUP_WIDTH;
    }
}
