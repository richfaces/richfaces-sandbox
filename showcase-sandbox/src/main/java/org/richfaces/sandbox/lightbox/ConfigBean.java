package org.richfaces.sandbox.lightbox;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@SessionScoped
@ManagedBean
public class ConfigBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private int containerBorderSize = 10;

    private int containerResizeSpeed = 400;

    private boolean fixedNavigation = false;

    private String imageBlank;

    private String imageBtnClose;

    private String imageBtnNext;

    private String imageBtnPrev;

    private String imageLoading;

    private String keyToClose = "c";

    private String keyToNext = "n";

    private String keyToPrev = "p";

    private String overlayBgColor = "#000";

    private double overlayOpacity = .8;

    private String txtImage = "Image";

    private String txtOf = "of";

// --------------------- GETTER / SETTER METHODS ---------------------

    public int getContainerBorderSize() {
        return containerBorderSize;
    }

    public void setContainerBorderSize(int containerBorderSize) {
        this.containerBorderSize = containerBorderSize;
    }

    public int getContainerResizeSpeed() {
        return containerResizeSpeed;
    }

    public void setContainerResizeSpeed(int containerResizeSpeed) {
        this.containerResizeSpeed = containerResizeSpeed;
    }

    public String getImageBlank() {
        return imageBlank;
    }

    public void setImageBlank(String imageBlank) {
        this.imageBlank = imageBlank;
    }

    public String getImageBtnClose() {
        return imageBtnClose;
    }

    public void setImageBtnClose(String imageBtnClose) {
        this.imageBtnClose = imageBtnClose;
    }

    public String getImageBtnNext() {
        return imageBtnNext;
    }

    public void setImageBtnNext(String imageBtnNext) {
        this.imageBtnNext = imageBtnNext;
    }

    public String getImageBtnPrev() {
        return imageBtnPrev;
    }

    public void setImageBtnPrev(String imageBtnPrev) {
        this.imageBtnPrev = imageBtnPrev;
    }

    public String getImageLoading() {
        return imageLoading;
    }

    public void setImageLoading(String imageLoading) {
        this.imageLoading = imageLoading;
    }

    public String getKeyToClose() {
        return keyToClose;
    }

    public void setKeyToClose(String keyToClose) {
        this.keyToClose = keyToClose;
    }

    public String getKeyToNext() {
        return keyToNext;
    }

    public void setKeyToNext(String keyToNext) {
        this.keyToNext = keyToNext;
    }

    public String getKeyToPrev() {
        return keyToPrev;
    }

    public void setKeyToPrev(String keyToPrev) {
        this.keyToPrev = keyToPrev;
    }

    public String getOverlayBgColor() {
        return overlayBgColor;
    }

    public void setOverlayBgColor(String overlayBgColor) {
        this.overlayBgColor = overlayBgColor;
    }

    public double getOverlayOpacity() {
        return overlayOpacity;
    }

    public void setOverlayOpacity(double overlayOpacity) {
        this.overlayOpacity = overlayOpacity;
    }

    public String getTxtImage() {
        return txtImage;
    }

    public void setTxtImage(String txtImage) {
        this.txtImage = txtImage;
    }

    public String getTxtOf() {
        return txtOf;
    }

    public void setTxtOf(String txtOf) {
        this.txtOf = txtOf;
    }

    public boolean isFixedNavigation() {
        return fixedNavigation;
    }

    public void setFixedNavigation(boolean fixedNavigation) {
        this.fixedNavigation = fixedNavigation;
    }
}
