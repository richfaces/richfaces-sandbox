package org.richfaces.bootstrap.ui.well;

import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIOutput;

/**
 * base class for Well component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractWell.COMPONENT_TYPE,
        family = AbstractWell.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = WellRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "well"))
public abstract class AbstractWell extends UIOutput implements CoreProps {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Well";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Well";

    @Attribute(suggestedValue = "large" + ","
            + "small", description = @Description("Css based predefined sizes"))
    public abstract String getSize();

    public String generateClasses() {
        StringBuilder sb = new StringBuilder("well");

        String size = getSize();
        if (size != null && !size.isEmpty()) {
            if (size.equals("large")) {
                sb.append(" well-large");
            } else if (size.equals("small")) {
                sb.append(" well-small");
            }
        }

        if (getStyleClass() != null) {
            sb.append(" ").append(getStyleClass());
        }

        return sb.toString();
    }
}
