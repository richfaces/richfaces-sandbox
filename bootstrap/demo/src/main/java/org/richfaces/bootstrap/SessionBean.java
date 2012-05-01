package org.richfaces.bootstrap;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "sessionBean")
@SessionScoped
@SuppressWarnings("serial")
public class SessionBean implements Serializable {
    private boolean fluid = true;

    public boolean isFluid() {
        return fluid;
    }

    public void setFluid(boolean fluid) {
        this.fluid = fluid;
    }
}
