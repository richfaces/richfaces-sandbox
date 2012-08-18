package org.richfaces.bootstrap.component.props;

import org.richfaces.bootstrap.component.HorizontalPosition;
import org.richfaces.bootstrap.component.VerticalPosition;
import org.richfaces.cdk.annotations.Attribute;

/**
 * Interface to add "vertical" and "horizontal" attributes
 * based on VerticalPosition and HorizontalPosition enums.
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 *
 */
public interface CardinalPositionProps {

    /**
     * This attribute allows you to position vertically a component or its content.
     */
    @Attribute(suggestedValue = "top,bottom")
    VerticalPosition getVertical();
    
    /**
     * This attribute allows you to position horizontally a component or its content.
     */
    @Attribute(suggestedValue = "left,right")
    HorizontalPosition getHorizontal();
}
