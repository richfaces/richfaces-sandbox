package org.richfaces.sandbox.radio;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionScoped
@ManagedBean
public class RadioBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private List<SomeEntity> entities = new ArrayList<SomeEntity>();

    private Converter entityConverter = new Converter() {
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Long id;
            id = Long.parseLong(value);
            for (SomeEntity entity : entities) {
                if (entity.id.equals(id)) {
                    return entity;
                }
            }
            return null;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) {
            return ((SomeEntity) value).id.toString();
        }
    };

    private List<String> options = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    private Map<String, String> optionsMap = new HashMap<String, String>();

    private Object selectedOption;

// --------------------------- CONSTRUCTORS ---------------------------

    public RadioBean() {
        optionsMap.put("first", "Master");
        optionsMap.put("second", "Looser");
        optionsMap.put("third", "The rest of the loosers");
        entities = Arrays.asList(new SomeEntity(1L), new SomeEntity(2L), new SomeEntity(3L));
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public List<SomeEntity> getEntities() {
        return entities;
    }

    public Converter getEntityConverter() {
        return entityConverter;
    }

    public List<String> getOptions() {
        return options;
    }

    public Map<String, String> getOptionsMap() {
        return optionsMap;
    }

    public Object getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Object selectedOption) {
        this.selectedOption = selectedOption;
    }

// -------------------------- INNER CLASSES --------------------------

    private static class SomeEntity {
// ------------------------------ FIELDS ------------------------------

        private Long id;

// --------------------------- CONSTRUCTORS ---------------------------

        private SomeEntity(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "SomeEntity{" +
                "id=" + id +
                '}';
        }
    }
}
