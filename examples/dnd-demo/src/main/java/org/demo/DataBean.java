package org.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.DropEvent;

@ManagedBean
@SessionScoped
public class DataBean {
    
    public void processEvent(DropEvent event) {
        System.out.println("DataBean.processEvent()");
    }
}
