package org.richfaces.bootstrap.ui.pagination;

import org.richfaces.bootstrap.ui.paginationitem.AbstractPaginationItem;
import org.richfaces.bootstrap.ui.paginationitem.PaginationItemRendererBase;
import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIComponent;
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
            + "mini" + ",", description = @Description("Predefined css based sizes. "))
    public abstract String getSize();

    @Attribute(suggestedValue = "left" + ","
            + "center" + ","
            + "right" + ",", description = @Description("Defines the horizontal alignment. The default value is left"))
    public abstract String getAlign();

    public boolean isPaginationItem(UIComponent component) {
        return component instanceof AbstractPaginationItem;
    }

    public String generateClasses() {
        StringBuilder sb = new StringBuilder("pagination");

        String size = getSize();
        if (size != null && !size.isEmpty()) {
            sb.append(" pagination-").append(size);
        }

        String align = getAlign();
        if (align != null && !align.isEmpty()) {
            if (align.equals("right")) {
                sb.append(" pagination-right");
            } else if (align.equals("center")) {
                sb.append(" pagination-centered");
            }
        }

        if (getStyleClass() != null) {
            sb.append(" ").append(getStyleClass());
        }

        return sb.toString();
    }
}
