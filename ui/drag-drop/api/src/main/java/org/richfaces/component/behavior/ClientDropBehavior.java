package org.richfaces.component.behavior;

import java.util.Set;

import javax.faces.component.behavior.ClientBehavior;


public interface ClientDropBehavior extends ClientBehavior{
    
    public Set<String> getAcceptType();
    
    public Object getDropValue();

}
