package org.richfaces.sandbox.carousel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.util.Arrays;
import java.util.List;

public class Bean {
// ------------------------------ FIELDS ------------------------------

    private List<ImageData> imageDatas = Arrays.asList(
            new ImageData("banner_bike.jpg", "This is a bike"),
            new ImageData("banner_paint.jpg", "Nice graffitti"),
            new ImageData("banner_tunnel.jpg", "The tunnel")
    );

    private DataModel<ImageData> imageDatasDataModel;
    private RowKeyConverter rowKeyConverter = new RowKeyConverter();

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<ImageData> getImageDatas() {
        return imageDatas;
    }

    public DataModel<ImageData> getImageDatasDataModel() {
        if (imageDatasDataModel == null) {
            imageDatasDataModel = new ListDataModel<ImageData>(imageDatas);
        }
        return imageDatasDataModel;
    }

    public RowKeyConverter getRowKeyConverter() {
        return rowKeyConverter;
    }

// -------------------------- INNER CLASSES --------------------------

    public class ImageData {
// ------------------------------ FIELDS ------------------------------

        private String filename;
        private String text;

// --------------------------- CONSTRUCTORS ---------------------------

        public ImageData(String filename, String text) {
            this.filename = filename;
            this.text = text;
        }

// --------------------- GETTER / SETTER METHODS ---------------------

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public class RowKeyConverter implements Converter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            for (ImageData data : imageDatas) {
                if (data.getFilename().equals(value)) {
                    return data;
                }
            }
            return null;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return imageDatas.get((Integer) value).getFilename();
        }
    }
}
