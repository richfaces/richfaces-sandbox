package org.richfaces.componentName;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {

    private List<Person> people = Arrays.asList(new Person("John", 26), new Person("David", 32), new Person("Humpfrey", 68),
            new Person("Lisa", 27));

    public List<Person> getSuggestions() {
        return people;
    }

    public Collection<Person> suggest(FacesContext facesContext, UIComponent component, final String prefix) {
        return Collections2.filter(people, new Predicate<Person>() {
            @Override
            public boolean apply(Person input) {
                if (prefix == null) {
                    return true;
                }
                return input.name.toLowerCase().startsWith(prefix);
            }
        });
    }

    public static class Person {

        private static volatile int SEQUENCE = 0;

        private final int id = SEQUENCE++;
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
