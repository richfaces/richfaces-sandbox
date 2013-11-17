package org.richfaces.bootstrap.ui.pagination;

import org.richfaces.bootstrap.ui.paginationitem.PaginationItemRendererBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIOutput;

/**
 * Base class for Pagination component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractPagination.COMPONENT_TYPE,
        family = AbstractPagination.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = PaginationRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "pagination"))
public abstract class AbstractPagination extends UIOutput implements CoreProps {

    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Pagination";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Pagination";

    @Attribute(suggestedValue = "large" + ","
            + "normal" + ","
            + "small" + ","
            + "mini" + ",")
    public abstract String getSize();

    @Attribute(suggestedValue = "left" + ","
            + "center" + ","
            + "right" + ",")
    public abstract String getAlign();
}
