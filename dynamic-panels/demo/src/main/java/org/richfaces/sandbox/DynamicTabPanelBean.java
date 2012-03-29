package org.richfaces.sandbox;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SessionScoped
@ManagedBean
public class DynamicTabPanelBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private Object accordionValue;

    private DataModel<Person> dataModel;

    private List<Person> ladies;

    private Person newPerson = new Person(null, null);

// --------------------------- CONSTRUCTORS ---------------------------

    public DynamicTabPanelBean()
    {
        ladies = new ArrayList<Person>(Arrays.asList(new Person(new Date(25, 9, 13), "Margaret Tatcher"), new Person(new Date(05, 1, 2), "Ayn Rand"),
            new Person(new Date(1852 - 1900, 5, 1), "Calamity Jane"), new Person(new Date(1819 - 1900, 4, 24), "Alexandrina Victoria"),
            new Person(new Date(1533 - 1900, 8, 7), "Elizabeth I"), new Person(new Date(1412 - 1900, 0, 1), "Joan of Arc")));
        dataModel = new ListDataModel<Person>(ladies);
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public Object getAccordionValue()
    {
        return accordionValue;
    }

    public void setAccordionValue(Object accordionValue)
    {
        this.accordionValue = accordionValue;
    }

    public DataModel<Person> getDataModel()
    {
        return dataModel;
    }

    public List<Person> getLadies()
    {
        return ladies;
    }

    public Person getNewPerson()
    {
        return newPerson;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addPerson()
    {
        ladies.add(newPerson);
        newPerson = new Person(new Date(), "");
    }

    public void removeFirstLady()
    {
        if (!ladies.isEmpty()) {
            ladies.remove(ladies.get(0));
        }
    }

// -------------------------- INNER CLASSES --------------------------

    public class Person implements Serializable {
// ------------------------------ FIELDS ------------------------------

        private Date birthDate;

        private String name;

// --------------------------- CONSTRUCTORS ---------------------------

        public Person(Date birthDate, String name)
        {
            this.birthDate = birthDate;
            this.name = name;
        }

// --------------------- GETTER / SETTER METHODS ---------------------

        public Date getBirthDate()
        {
            return birthDate;
        }

        public void setBirthDate(Date birthDate)
        {
            this.birthDate = birthDate;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }
}
