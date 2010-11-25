package org.richfaces.component.behavior;

import javax.faces.component.behavior.ClientBehavior;


public interface ClientDragBehavior extends ClientBehavior {
    
    public String getType();
    
    public String getIndicator();
    
    public Object getDragValue();

}

