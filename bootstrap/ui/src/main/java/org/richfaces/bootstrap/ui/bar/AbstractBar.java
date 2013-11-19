package org.richfaces.bootstrap.ui.bar;

import org.richfaces.bootstrap.component.BootstrapSeverity;
import org.richfaces.bootstrap.ui.progressbar.ProgressBarRendererBase;
import org.richfaces.cdk.annotations.*;
import org.richfaces.ui.attribute.CoreProps;

import javax.faces.component.UIOutput;

/**
 * base class for the Bar component
 *
 * @author Lukas Eichler
 */
@JsfComponent(
        type = AbstractBar.COMPONENT_TYPE,
        family = AbstractBar.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = BarRendererBase.RENDERER_TYPE),
        tag = @Tag(name = "bar"))
public abstract class AbstractBar extends UIOutput implements CoreProps {

    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Bar";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Bar";

    @Attribute(suggestedValue = BootstrapSeverity.SUCCESS + ","
            + BootstrapSeverity.INFO + ","
            + BootstrapSeverity.WARNING + ","
            + BootstrapSeverity.ERROR, description = @Description("Severity Color of the bar"))
    public abstract String getSeverity();

    @Attribute(description = @Description("Width of the bar in %"))
    public abstract String getWidth();
}