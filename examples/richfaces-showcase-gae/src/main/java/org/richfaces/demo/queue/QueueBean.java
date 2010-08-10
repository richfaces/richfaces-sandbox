package org.richfaces.demo.queue;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class QueueBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 7503791626510224913L;
    private Long requestDelay = new Long(500);
    private boolean ignoreDupResponces = false;
    private String text = "";
    private int requests = 0;
    private int events = 0;

    public void resetText() {
        setText("");
    }

    public Long getRequestDelay() {
        return requestDelay;
    }

    public void setRequestDelay(Long requestDelay) {
        this.requestDelay = requestDelay;
    }

    public boolean isIgnoreDupResponces() {
        return ignoreDupResponces;
    }

    public void setIgnoreDupResponces(boolean ignoreDupResponces) {
        this.ignoreDupResponces = ignoreDupResponces;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int reuqests) {
        this.requests = reuqests;
    }

    public int getEvents() {
        return events;
    }

    public void setEvents(int events) {
        this.events = events;
    }

}
