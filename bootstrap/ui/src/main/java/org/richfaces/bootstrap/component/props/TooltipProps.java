package org.richfaces.bootstrap.component.props;

import org.richfaces.cdk.annotations.Attribute;

public interface TooltipProps {

    /**
     * Apply a css fade transition to the tooltip (default value: true)
     */
    @Attribute
    boolean isAnimation();

    /**
     * How to position the tooltip - top | bottom | left | right (default value: top)
     */
    @Attribute
    String getPlacement();

    /**
     * How the tooltip is triggered - hover | focus | manual (default value: hover)
     */
    @Attribute
    String getTrigger();

    /**
     * Delay showing and hiding the tooltip (ms) - does not apply to manual trigger type (default value: 0)
     */
    @Attribute
    int getDelay();
}
