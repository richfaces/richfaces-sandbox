package org.richfaces.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "skinBean")
@SessionScoped
public class SkinBean {

    private static final String[] SKINS = {"blueSky", "deepMarine", "emeraldTown", "NULL", "ruby", "classic",
        "DEFAULT", "japanCherry", "plain", "wine" };

    private String skin = "blueSky";
    
    public SkinBean() {
        skin = "blueSky";
    }
    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
    
    public String[] getSkins() {
        return SKINS;
    }
}
