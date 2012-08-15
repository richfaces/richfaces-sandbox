package org.richfaces.bootstrap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.Serializable;

@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void logMessage(String message) {
        System.out.println(message);
    }
}
