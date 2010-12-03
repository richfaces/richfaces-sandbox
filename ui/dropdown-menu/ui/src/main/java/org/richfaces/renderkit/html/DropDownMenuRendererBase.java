package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.AbstractDropDownMenu;
import org.richfaces.component.AbstractMenuGroup;
import org.richfaces.component.AbstractMenuItem;
import org.richfaces.component.AbstractMenuSeparator;
import org.richfaces.renderkit.RenderKitUtils;
import org.richfaces.renderkit.RenderKitUtils.ScriptHashVariableWrapper;
import org.richfaces.renderkit.RendererBase;

@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "jquery.position.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(name = "richfaces-event.js"),
    @ResourceDependency(library = "org.richfaces", name = "popup.js"),
    @ResourceDependency(library = "org.richfaces", name = "dropdownmenu.ecss", target="head"),
    @ResourceDependency(library = "org.richfaces", name = "menu.js"),
    @ResourceDependency(library = "org.richfaces", name = "popupList.js")})
public abstract class DropDownMenuRendererBase extends RendererBase {
	 
    public static final String RENDERER_TYPE = "org.richfaces.DropDownMenuRenderer";
    
    public static final int DEFAULT_MIN_POPUP_WIDTH = 250;    
    
    @Override
    public void renderChildren(FacesContext facesContext, UIComponent component) throws IOException {
        AbstractDropDownMenu dropDownMenu = (AbstractDropDownMenu) component;

        for (UIComponent child : dropDownMenu.getChildren()) {
            if (child.isRendered() && (
                child instanceof AbstractMenuGroup ||
                child instanceof AbstractMenuItem ||
                child instanceof AbstractMenuSeparator)) {
                
                child.encodeAll(facesContext);
            }
        }
    }
     
    protected boolean isDisabled(FacesContext facesContext, UIComponent component) {
        if (component instanceof AbstractDropDownMenu) {
            return ((AbstractDropDownMenu) component).isDisabled();
        }
        return false;
    }
     
    protected UIComponent getLabelFacet(FacesContext facesContext, UIComponent component) {
        UIComponent facet = null;
        AbstractDropDownMenu ddmenu = (AbstractDropDownMenu) component; 
        if (ddmenu != null) {
            
            if (ddmenu.isDisabled()) {
                facet = ddmenu.getFacet(AbstractDropDownMenu.Facets.LABEL_DISABLED.toString());
            } else {
                facet = ddmenu.getFacet(AbstractDropDownMenu.Facets.LABEL.toString());
            }
        }
        return facet;   
    }  

    public List<Map<String, Object>> getMenuGroups(FacesContext facesContext, UIComponent component) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        List<AbstractMenuGroup> groups = new ArrayList<AbstractMenuGroup>();
        if (component instanceof AbstractDropDownMenu) {
            if (component.isRendered() && !((AbstractDropDownMenu) component).isDisabled()) {
                getMenuGroups(component, groups);        
            }
        }
        for (AbstractMenuGroup group : groups) {
            if (group.isRendered() && !group.isDisabled()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", group.getClientId());
                map.put("horizontalOffset", group.getHorizontalOffset());
                map.put("verticalOffset", group.getVerticalOffset());
                map.put("direction", group.getDirection());
                RenderKitUtils.addToScriptHash(map, "onhide", group.getOnhide(), null, ScriptHashVariableWrapper.eventHandler);
                RenderKitUtils.addToScriptHash(map, "onshow", group.getOnshow(), null, ScriptHashVariableWrapper.eventHandler);
                results.add(map);
            }
        }
        return results;
    }
    
    private void getMenuGroups(UIComponent component, List<AbstractMenuGroup> list) {
        if (component != null && list != null) {
            for (UIComponent c : component.getChildren()) {
                if (c instanceof AbstractMenuGroup) {
                    list.add((AbstractMenuGroup) c);
                }
                getMenuGroups(c, list);                
            }
        }
    }
    
    protected int getMinPopupWidth(FacesContext facesContext, UIComponent component) {
        if (component instanceof AbstractDropDownMenu) {
            ((AbstractDropDownMenu) component).getPopupWith();
        }
        return DEFAULT_MIN_POPUP_WIDTH;
    }    
}
