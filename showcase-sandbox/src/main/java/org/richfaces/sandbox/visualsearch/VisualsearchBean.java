/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.sandbox.visualsearch;

import org.richfaces.component.event.FacetSuggestionEvent;
import org.richfaces.component.event.SearchEvent;
import org.richfaces.component.event.ValueSuggestionEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SessionScoped
@ManagedBean
public class VisualsearchBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private int counter;

    private List<String> facets;

    private String query;

    private String value;

    private Map<String, List<String>> values;

// --------------------------- CONSTRUCTORS ---------------------------

    public VisualsearchBean() {
        facets = new ArrayList<String>();
        facets.add("Status");
        facets.add("Version");
        facets.add("Assignee");

        values = new HashMap<String, List<String>>();
        List<String> list = new ArrayList<String>();
        values.put("Status", list);
        list.add("Open");
        list.add("Closed");
        list.add("In Progress");

        list = new ArrayList<String>();
        values.put("Version", list);
        list.add("1.0.0");
        list.add("1.0.1");
        list.add("1.0.2");
        list = new ArrayList<String>();
        values.put("Assignee", list);
        list.add("John");
        list.add("Brian");
        list.add("Scott");
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

// -------------------------- OTHER METHODS --------------------------

    public int getCounter() {
        return counter++;
    }

    public List<String> getFixedFacetSuggestions() {
        return facets;
    }

    public Map<String, List<String>> getFixedValueSuggestions() {
        return values;
    }

    public void search(SearchEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Searching for: " + getValue()));
    }

    public List<String> suggestFacet(FacetSuggestionEvent event) {
        /**
         * We could even filter this list by event.getSearchTerm()
         */
        return facets;
    }

    public List<String> suggestValue(ValueSuggestionEvent event) {
        final List<String> list = values.get(event.getFacet());
        /**
         * We could even filter this list by event.getSearchTerm()
         */
        return list;
    }
}
