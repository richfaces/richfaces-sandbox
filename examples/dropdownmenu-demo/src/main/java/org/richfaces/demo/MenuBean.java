package org.richfaces.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class MenuBean {
    private String current;
    private String mode = "server";
    private boolean disabled = false;
    private boolean checkbox = false;

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
    
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    
    public String getCurrent() {
        return this.current;
    }
    
    public void setCurrent(String current) {
        this.current = current;
    }
    
    public String doNew() {
        this.current="New";
        return null;
    }
    public String doOpen() {
        this.current="Open";
        return null;
    }
    public String doClose() {
        this.current="Close";
        return null;
    }
    public String doSave() {
        this.current="Save";
        return null;
    }
    public String doExit() {
        this.current="Exit";
        return null;
    }    

    public void setValue(String value) {
        this.current = value;
    }
}
