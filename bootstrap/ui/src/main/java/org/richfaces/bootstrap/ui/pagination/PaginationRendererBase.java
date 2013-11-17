package org.richfaces.bootstrap.ui.pagination;

import org.richfaces.renderkit.RendererBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

/**
 * Base class for the pagination renderer
 *
 * @author Lukas Eichler
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib")})
public abstract class PaginationRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.PaginationRenderer";

}
