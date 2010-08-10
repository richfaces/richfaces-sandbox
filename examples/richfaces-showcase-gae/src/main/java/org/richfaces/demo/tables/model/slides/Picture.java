package org.richfaces.demo.tables.model.slides;

import java.io.Serializable;

public class Picture implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -248567945955339619L;
    private String name;
    private String uri;
    
    public Picture(String uri, String name) {
        this.name = name;
        this.uri = uri;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
