package org.richfaces.bootstrap.component.props;

import org.richfaces.bootstrap.component.BootstrapSize;
import org.richfaces.cdk.annotations.Attribute;

/**
 * Interface to add the "scale" attribute based on the BootstrapSize enum
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 *
 */
public interface BootstrapScaleProps {

    /**
     * This attribute allows you to scale a component, changing its global size.
     */
    @Attribute
    BootstrapSize getScale();
}
