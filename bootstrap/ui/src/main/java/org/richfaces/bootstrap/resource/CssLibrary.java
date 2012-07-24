package org.richfaces.bootstrap.resource;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

import com.google.common.collect.ImmutableList;

public class CssLibrary implements ResourceLibrary {

    private static final ImmutableList<ResourceKey> BOOTSTRAP_MIN = ImmutableList.of(ResourceKey.create("rf-bootstrap-css.css",
            "org.richfaces.min"));

    private static final ImmutableList<ResourceKey> BOOTSTRAP_CLIENT_SIDE = ImmutableList.of(
            ResourceKey.create("bootstrap.less", "org.richfaces.develop"),
            ResourceKey.create("less.min.js", "less"),
            ResourceKey.create("less.instant.js", "less"));

    @Override
    public Iterable<ResourceKey> getResources() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ProjectStage stage = facesContext.getApplication().getProjectStage();

        if (ProjectStage.Development == stage) {
            return BOOTSTRAP_CLIENT_SIDE;
        } else {
            return BOOTSTRAP_MIN;
        }
    }
}
