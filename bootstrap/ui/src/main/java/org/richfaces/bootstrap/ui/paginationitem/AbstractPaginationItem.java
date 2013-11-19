package org.richfaces.bootstrap.ui.paginationitem;

import org.richfaces.bootstrap.ui.pagination.PaginationRendererBase;
import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIOutput;

/**
 * Base Item for PagitionItem
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractPaginationItem.COMPONENT_TYPE,
        family = AbstractPaginationItem.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = PaginationItemRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "paginationItem"))
public abstract class AbstractPaginationItem extends UIOutput implements CoreProps {

    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.PaginationItem";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.PaginationItem";

    @Attribute(suggestedValue = "true" + ","
            + "false", description = @Description("Button active"))
    public abstract String getActive();

    @Attribute(suggestedValue = "true" + ","
            + "false", description = @Description("Button disabled"))
    public abstract String getDisabled();
}
