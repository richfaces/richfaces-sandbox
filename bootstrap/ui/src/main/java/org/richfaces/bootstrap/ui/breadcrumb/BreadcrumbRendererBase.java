package org.richfaces.bootstrap.ui.breadcrumb;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.richfaces.renderkit.RendererBase;

/**
 * Base class for the breadcrumb renderer
 * 
 * @author damien.gouyette@gmail.com
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib") })
public class BreadcrumbRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.BreadcrumbRenderer";
}
