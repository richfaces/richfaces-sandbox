package org.richfaces.demo.common.navigation;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class BaseDescriptor implements Serializable {

    private static final long serialVersionUID = 5614594358147757458L;
    
    private String id;
    private String name;
    private boolean newItem;
    private boolean currentItem;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "new")
    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    public boolean isCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(boolean currentItem) {
        this.currentItem = currentItem;
    }
    
    @XmlElement
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
