package org.richfaces.dataTable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {
    private List<String> values = Arrays.asList("hellow", "world", "foo", "bar", "test");

    public List<String> getValues() {
        return values;
    }
}
