package org.richfaces.demo.common.navigation;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class DemoNavigator implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3970933260901989658L;
    private static final String DEMO_VIEW_PARAMETER = "demo";
    private static final String SAMPLE_VIEW_PARAMETER = "sample";
    private static final String SEPARATOR = "/";

    @ManagedProperty(value = "#{navigationParser.groupsList}")
    private List<GroupDescriptor> groups;
    private DemoDescriptor currentDemo;
    private SampleDescriptor currentSample;
    private String sample;
    private String demo;
    
    @PostConstruct
    public void init() {
        currentDemo =null;
        currentSample = null;
    }
    
    public DemoDescriptor getCurrentDemo() {
        String id = getViewParameter(DEMO_VIEW_PARAMETER);
        if (currentDemo == null || !currentDemo.getId().equals(id)) {
            if (id != null) {
                currentDemo = findDemoById(id);
                currentSample = null;
            }
            if (currentDemo == null) {
                currentDemo = groups.get(0).getDemos().get(0);
                currentSample = null;
            }
        }
        return currentDemo;
    }

    public SampleDescriptor getCurrentSample() {
        String id = getViewParameter(SAMPLE_VIEW_PARAMETER);
        if (currentSample == null || !currentSample.getId().equals(id)) {
            if (id != null) {
                currentSample = getCurrentDemo().getSampleById(id);
            }
            if (currentSample == null) {
                currentSample = getCurrentDemo().getSamples().get(0);
            }
        }
        return currentSample;
    }

    private String getViewParameter(String name) {
        FacesContext fc = FacesContext.getCurrentInstance();
        String param = (String) fc.getExternalContext().getRequestParameterMap().get(name);
        if (param != null && param.trim().length() > 0) {
            return param;
        } else {
            return null;
        }
    }

    public DemoDescriptor findDemoById(String id) {
        Iterator<GroupDescriptor> it = groups.iterator();
        while (it.hasNext()) {
            GroupDescriptor group = it.next();
            Iterator<DemoDescriptor> dit = group.getDemos().iterator();
            while (dit.hasNext()) {
                DemoDescriptor demo = (DemoDescriptor) dit.next();
                if (demo.getId().equals(id)) {
                    return demo;
                }
            }
        }
        return null;
    }

    public String getSampleURI() {
        DemoDescriptor currentDemo = getCurrentDemo();
        FacesContext context = FacesContext.getCurrentInstance();

        NavigationHandler handler = context.getApplication().getNavigationHandler();

        if (handler instanceof ConfigurableNavigationHandler) {
            ConfigurableNavigationHandler navigationHandler = (ConfigurableNavigationHandler) handler;

            NavigationCase navCase = navigationHandler.getNavigationCase(context, null, getCurrentDemo().getId()
                + SEPARATOR + getCurrentSample().getId());

            return navCase.getToViewId(context);
        }

        return null;
    }

    public List<GroupDescriptor> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDescriptor> groups) {
        this.groups = groups;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }
}
