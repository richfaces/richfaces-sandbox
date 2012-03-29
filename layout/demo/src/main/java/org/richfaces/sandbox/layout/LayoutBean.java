package org.richfaces.sandbox.layout;

import org.richfaces.component.LayoutPosition;

import java.io.Serializable;

public class LayoutBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private String pageTitle;
    private Integer pageWidth = 700;
    private LayoutPosition sidebarPosition;
    private Integer sidebarWidth = 300;
    private String theme = "simple";

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }

    public LayoutPosition getSidebarPosition() {
        return sidebarPosition;
    }

    public void setSidebarPosition(LayoutPosition position) {
        this.sidebarPosition = position;
    }

    public Integer getSidebarWidth() {
        return sidebarWidth;
    }

    public void setSidebarWidth(Integer sidebarWidth) {
        this.sidebarWidth = sidebarWidth;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
