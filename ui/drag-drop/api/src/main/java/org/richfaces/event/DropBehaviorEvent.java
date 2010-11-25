package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesListener;

public class DropBehaviorEvent extends BehaviorEvent {

    private static final long serialVersionUID = 3717071628237886288L;
   
    private Object dropValue;
    
    private Object dragValue;
    
    private UIComponent dragSource;
      
    public Object getDropValue() {
        return dropValue;
    }

    public void setDropValue(Object dropValue) {
        this.dropValue = dropValue;
    }

    public Object getDragValue() {
        return dragValue;
    }

    public void setDragValue(Object dragValue) {
        this.dragValue = dragValue;
    }

    public UIComponent getDragSource() {
        return dragSource;
    }

    public void setDragSource(UIComponent dragSource) {
        this.dragSource = dragSource;
    }

    public DropBehaviorEvent(UIComponent component, Behavior behavior) {
        super(component, behavior);
    }

    @Override
    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof DropListener);
    }
    
    @Override
    public void processListener(FacesListener listener) {
        ((DropListener) listener).processDrop(this);
    }

}
