package org.richfaces.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;


@ResourceDependencies({
    @ResourceDependency(library = "javax.faces", name = "jsf.js"),
    @ResourceDependency(name = "jquery.js"),
    @ResourceDependency(name = "richfaces.js"),
    @ResourceDependency(name = "richfaces-base-component.js"),
    @ResourceDependency(library = "org.richfaces", name = "dnd-indicator.js"),
    @ResourceDependency(library = "org.richfaces", name = "indicator.ecss") })
public class DragIndicatorRendererBase extends RendererBase {
}
