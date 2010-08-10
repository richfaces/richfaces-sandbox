package org.richfaces.demo.function;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "functionBean")
@RequestScoped
public class FunctionBean {
    private String text;

    public void processHover() {
        setText(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("name"));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
