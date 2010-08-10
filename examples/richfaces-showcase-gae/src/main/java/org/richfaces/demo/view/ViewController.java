package org.richfaces.demo.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ViewController {

    public List<SampleToolBarItem> getTableToolBarItems() {
        List<SampleToolBarItem> items = new ArrayList<SampleToolBarItem>();
        SampleToolBarItem item = new SampleToolBarItem("Simple", "/richfaces/dataTable/simpleTable.jsf");
        items.add(item);
        item = new SampleToolBarItem("Complex layouts", "/richfaces/dataTable/subtables.jsf");
        items.add(item);
        return items;
    }

}
