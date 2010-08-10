package org.richfaces.demo.tables.model.capitals;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class Capital implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -1042449580199397136L;

    private static final String FILE_EXT = ".gif";
    
    private String name;
    private String state;
    private String timeZone;

    public Capital() {
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    private String stateNameToFileName() {
        return state.replaceAll("\\s", "").toLowerCase();
    }

    public String getStateFlag() {
        return "/images/capitals/" + stateNameToFileName() + FILE_EXT;
    }

    @XmlElement
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
