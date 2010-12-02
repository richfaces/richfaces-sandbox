package org.richfaces.renderkit.html;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.component.AbstractMenuItem;
import org.richfaces.component.MenuComponent;
import org.richfaces.renderkit.AjaxCommandRendererBase;

public class MenuItemRendererBase extends AjaxCommandRendererBase {
    
    public static final String RENDERER_TYPE = "org.richfaces.MenuItemRenderer";
    
    protected boolean isDisabled(FacesContext facesContext, UIComponent component) {
        if (component instanceof AbstractMenuItem) {
            return ((AbstractMenuItem) component).isDisabled();
        }
        return false;
    }

    protected UIComponent getIconFacet(FacesContext facesContext, UIComponent component) {
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
    
    protected String getIconAttribute(FacesContext facesContext, UIComponent component) {
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
    
    private UIComponent getUIForm(UIComponent component) {
        if (component != null) {
            UIComponent parent = component.getParent();
            while (parent != null) {
                if (parent instanceof UIForm) {
                    return parent;
                }
                parent = parent.getParent();
            }
        }
        return null;
    }
    
    private String getServerSubmitFunction(UIComponent component) {
        UIComponent form = getUIForm(component);
        if (component != null && form != null) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put(component.getClientId(), component.getClientId());
            
            JSFunction submitFunction = new JSFunction("RichFaces.submitForm");
            submitFunction.addParameter(form.getClientId());
            submitFunction.addParameter(param);
            
            return submitFunction.toScript();
        }

        return "";
    }
    
    protected String getOnClickFunction(FacesContext facesContext, UIComponent component) {
        AbstractMenuItem menuItem = (AbstractMenuItem) component;
        String subminMode = resolveSubmitMode(menuItem);
        if (subminMode == null || MenuComponent.MODE_SERVER.equalsIgnoreCase(subminMode)) {
            return getServerSubmitFunction(menuItem);
        } else if (MenuComponent.MODE_AJAX.equalsIgnoreCase(subminMode)) {
            return getOnClick(facesContext, menuItem);
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
    
    protected String getStyleClass(FacesContext facesContext, UIComponent component, String ddMenuStyle, String menuGroupStyle, String menuItemStyle) {
        UIComponent ddMenu = getDDMenu(facesContext, component);
        UIComponent menuGroup = getMenuGroup(facesContext, component);
        Object styleClass = null;
        if (ddMenu != null && ddMenuStyle != null && ddMenuStyle.length() > 0) {
            styleClass = ddMenu.getAttributes().get(ddMenuStyle);
        }
        if (menuGroup != null && menuGroupStyle != null && menuGroupStyle.length() > 0) {
            styleClass = concatClasses(styleClass, menuGroup.getAttributes().get(menuGroupStyle));
        }

        return concatClasses(styleClass, component.getAttributes().get(menuItemStyle));
    }
    
    /**
     * Returns a parent <code>AbstractDropDownMenu</code> object of the given component.
     * @param facesContext
     * @param component
     * @return <code>UIComponent</code>
     */
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
    
    /**
     * Returns a parent <code>AbstractMenuGroup</code> object of the given component.
     * @param facesContext
     * @param component
     * @return <code>UIComponent</code>
     */
    protected UIComponent getMenuGroup(FacesContext facesContext, UIComponent component) {
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
