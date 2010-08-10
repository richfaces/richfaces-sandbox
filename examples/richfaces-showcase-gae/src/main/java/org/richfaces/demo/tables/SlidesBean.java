package org.richfaces.demo.tables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.demo.tables.model.slides.Picture;

/**
 * @author Ilya Shaikovsky
 * 
 */
@ManagedBean
@ViewScoped
public class SlidesBean implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -6498592143189891265L;
    private static final String FILE_EXT = ".jpg";
    private static final int FILES_COUNT = 9;
    private static final String PATH_PREFIX = "/images/nature/";
    private static final String PIC_NAME = "pic";
    private List<Picture> pictures;
    private int currentIndex = 1;

    public SlidesBean() {
        pictures = new ArrayList<Picture>();
        for (int i = 1; i <= FILES_COUNT; i++) {
            pictures.add(new Picture(PATH_PREFIX + PIC_NAME + i + FILE_EXT, PIC_NAME + i));
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
