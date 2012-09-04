package org.richfaces.bootstrap.resource;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

import com.google.common.collect.ImmutableList;

public class ResponsiveLibrary implements ResourceLibrary {

    private static final ImmutableList<ResourceKey> BOOTSTRAP_RESPONSIVE_MIN = ImmutableList.of(ResourceKey.create("min/rf-bootstrap-responsive.css",
            "org.richfaces"));

    private static final ImmutableList<ResourceKey> BOOTSTRAP_RESPONSIVE_CLIENT_SIDE = ImmutableList.of(
            ResourceKey.create("bootstrap-responsive.less", "org.richfaces.develop"),
            ResourceKey.create("less.min.js", "less"),
            ResourceKey.create("less.instant.js", "less"));

    @Override
    public Iterable<ResourceKey> getResources() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ProjectStage stage = facesContext.getApplication().getProjectStage();

        if (ProjectStage.Development == stage) {
            return BOOTSTRAP_RESPONSIVE_CLIENT_SIDE;
        } else {
            return BOOTSTRAP_RESPONSIVE_MIN;
        }
    }

}
