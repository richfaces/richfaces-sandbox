package org.richfaces.bootstrap.resource;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

import com.google.common.collect.ImmutableList;

public class CssLibrary implements ResourceLibrary {

    private static final ImmutableList<ResourceKey> BOOTSTRAP_MIN = ImmutableList.of(
            ResourceKey.create("min/rf-bootstrap-css.css", "org.richfaces"));
    
    private static final ImmutableList<ResourceKey> BOOTSTRAP_RESPONSIVE_MIN = ImmutableList.of(
            ResourceKey.create("min/rf-bootstrap-responsive.css", "org.richfaces"));
    
    private static final ImmutableList<ResourceKey> BOOTSTRAP_CLIENT_SIDE = ImmutableList.of(
            ResourceKey.create("bootstrap.less", "org.richfaces.develop"),
            ResourceKey.create("less.min.js", "less"),
            ResourceKey.create("less.instant.js", "less"));
    
    private static final ImmutableList<ResourceKey> BOOTSTRAP_RESPONSIVE_CLIENT_SIDE = ImmutableList.of(
            ResourceKey.create("bootstrap-responsive.less", "org.richfaces.develop"),
            ResourceKey.create("less.min.js", "less"),
            ResourceKey.create("less.instant.js", "less"));

    @Override
    public Iterable<ResourceKey> getResources() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ProjectStage stage = facesContext.getApplication().getProjectStage();
        // TODO add this context-param on CoreConfiguration class and use it the static way
        // waiting for the huge refactoring to be done
        String responsiveParam = facesContext.getExternalContext().getInitParameter("org.richfaces.responsiveDesign");
        Boolean isResponsive = (responsiveParam == null) || new Boolean(responsiveParam);

        if (ProjectStage.Development == stage) {
            if(isResponsive) {
                return BOOTSTRAP_RESPONSIVE_CLIENT_SIDE;
            } else {
                return BOOTSTRAP_CLIENT_SIDE;
            }
        } else {
            if(isResponsive) {
                return BOOTSTRAP_RESPONSIVE_MIN;
            } else {
                return BOOTSTRAP_MIN;
            }
        }
    }
}
