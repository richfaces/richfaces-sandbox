package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class PositionChangeEvent extends FacesEvent {
// ------------------------------ FIELDS ------------------------------

    private int endX;

    private int endY;

    private int startX;

    private int startY;

// --------------------------- CONSTRUCTORS ---------------------------

    public PositionChangeEvent(UIComponent component, int startX, int startY, int endX, int endY)
    {
        super(component);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getEndX()
    {
        return endX;
    }

    public int getEndY()
    {
        return endY;
    }

    public int getStartX()
    {
        return startX;
    }

    public int getStartY()
    {
        return startY;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public String toString()
    {
        return "PositionChangeEvent{" + "endX=" + endX + ", endY=" + endY + ", startX=" + startX + ", startY=" + startY + '}';
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public boolean isAppropriateListener(FacesListener listener)
    {
        return listener instanceof PositionChangeListener;
    }

    @Override
    public void processListener(FacesListener listener)
    {
        ((PositionChangeListener) listener).positionChange(this);
    }
}
