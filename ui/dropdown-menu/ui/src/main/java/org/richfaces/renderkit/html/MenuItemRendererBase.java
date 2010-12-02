package org.richfaces.renderkit.html;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.component.AbstractMenuItem;
import org.richfaces.component.MenuComponent;
import org.richfaces.renderkit.AjaxCommandRendererBase;

public class MenuItemRendererBase extends AjaxCommandRendererBase {
    
    public static final String RENDERER_TYPE = "org.richfaces.MenuItemRenderer";
    
    protected boolean isDisabled(UIComponent component) {
        if (component instanceof AbstractMenuItem) {
            return ((AbstractMenuItem) component).isDisabled();
        }
        return false;
    }

    protected UIComponent getIconFacet(UIComponent component) {
        UIComponent facet = null;
        AbstractMenuItem menuItem = (AbstractMenuItem) component; 
        if (menuItem != null) {
            
            if (menuItem.isDisabled()) {
                facet = menuItem.getFacet(AbstractMenuItem.Facets.ICON_DISABLED.toString());
            } else {
                facet = menuItem.getFacet(AbstractMenuItem.Facets.ICON.toString());
            }
        }
        return facet;   
    }      
    
    protected String getIconAttribute(UIComponent component) {
        String icon = null;
        AbstractMenuItem menuItem = (AbstractMenuItem) component; 
        if (menuItem != null) {
            
            if (menuItem.isDisabled()) {
                icon = menuItem.getIconDisabled();
            } else {
                icon = menuItem.getIcon();
            }
        }
        return icon;   
    }     
    
    @Override
    public void doDecode(FacesContext context, UIComponent component) {
        AbstractMenuItem menuItem = (AbstractMenuItem) component;

        if (menuItem != null) {
            String mode = resolveSubmitMode(menuItem);
            if (!MenuComponent.MODE_CLIENT.equalsIgnoreCase(mode)) {
                super.doDecode(context, component);
            }
        }
    }
    
    protected String getOnClickFunction(UIComponent component) {
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        String subminMode = resolveSubmitMode(menuItem);
        if (subminMode == null || MenuComponent.MODE_SERVER.equalsIgnoreCase(subminMode)) {
            return "submit()";
        } else if (MenuComponent.MODE_AJAX.equalsIgnoreCase(subminMode)) {
            return getOnClick(FacesContext.getCurrentInstance(), menuItem);
        } else if (menuItem.isDisabled()) {
            return "";
        } else if (MenuComponent.MODE_CLIENT.equalsIgnoreCase(subminMode)) {
            return "";
        }
        
        return "";
    }
    
    protected String resolveSubmitMode(AbstractMenuItem menuItem) {
        String submitMode = menuItem.getMode();
        if (null != submitMode) {
            return submitMode;
        }
        UIComponent parent = menuItem.getParent();
        while (null != parent) {
            if (parent instanceof MenuComponent) {
                return ((MenuComponent) parent).getMode();
            }
            parent = parent.getParent();
        }

        return MenuComponent.MODE_SERVER;
    } 
    
    protected String getStyleClass(UIComponent component, String ddMenuStyle, String menuGroupStyle, String menuItemStyle) {
        UIComponent ddMenu = getDDMenu(component);
        UIComponent menuGroup = getMenuGroup(component);
        Object styleClass = null;
        if (ddMenu != null && ddMenuStyle != null && ddMenuStyle.length() > 0) {
            styleClass = ddMenu.getAttributes().get(ddMenuStyle);
        }
        if (menuGroup != null && menuGroupStyle != null && menuGroupStyle.length() > 0) {
            styleClass = concatClasses(styleClass, menuGroup.getAttributes().get(menuGroupStyle));
        }

        return concatClasses(styleClass, component.getAttributes().get(menuItemStyle));
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
    
    protected UIComponent getMenuGroup(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof AbstractMenuGroup) {
                return parent;
            }
            parent = parent.getParent();
        }
        return null;
    }
}
