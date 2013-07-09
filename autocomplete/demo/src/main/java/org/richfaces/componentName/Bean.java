package org.richfaces.componentName;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {

    public String[] getSuggestions() {
        return new String[] { "Java", "Haskell", "C++" };
    }

    public String[] suggest(FacesContext facesContext, UIComponent component, String prefix) {
        return new String[] { "Test" + prefix };
    }
}
