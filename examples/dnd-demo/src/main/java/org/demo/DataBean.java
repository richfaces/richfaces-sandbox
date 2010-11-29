package org.demo;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.DropEvent;

@ManagedBean
@SessionScoped
public class DataBean {
    
    private List<String> dropValues = new ArrayList<String>();
    
    private String dragValue1 = "dragValue 1";
    
    private String dragValue2 = "dragValue 3";
    
    private String dragValue3 = "dragValue 3";
    

    public List<String> getDropValues(){
        return dropValues;
    }
    
    public String getDragValue1() {
        return dragValue1;
    }

    public void setDragValue1(String dragValue1) {
        this.dragValue1 = dragValue1;
    }

    public String getDragValue2() {
        return dragValue2;
    }

    public void setDragValue2(String dragValue2) {
        this.dragValue2 = dragValue2;
    }

    public String getDragValue3() {
        return dragValue3;
    }

    public void setDragValue3(String dragValue3) {
        this.dragValue3 = dragValue3;
    }

    public void setDropValues(List<String> dropValues){
        this.dropValues = dropValues;
    }
    
    public void processEvent(DropEvent event) {
        String value = (String)event.getDragValue();
        dropValues.add(value);
        System.out.println("DataBean.processEvent()");
    }
    
    
}
