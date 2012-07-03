package org.richfaces.bootstrap.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractSemanticComponent<T> extends UIComponentBase implements ComponentSystemEventListener {

    public abstract Class<T> getRendererCapability();
    
    public abstract String getRendererType(T container);

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        Class<T> rendererCapability = getRendererCapability();
        T container = findParentImplementing(rendererCapability);
        if (container == null) {
            throw new IllegalArgumentException("Semantic component must be nested within the top-level component");
        }
        String rendererType = getRendererType(container);
        if (rendererType != null) {
            this.setRendererType(rendererType);
        }
    }
    
    @SuppressWarnings("unchecked")
    private T findParentImplementing(Class<T> type) {
        UIComponent current = this;
        
        while (current != null) {
            if (type.isAssignableFrom(current.getClass())) {
                return (T) current;
            }
            current = current.getParent();
        }
        
        return null;
    }
}
