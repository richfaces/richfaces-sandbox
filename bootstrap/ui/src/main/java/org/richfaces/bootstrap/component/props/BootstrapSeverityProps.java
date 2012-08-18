package org.richfaces.bootstrap.component.props;

import org.richfaces.bootstrap.component.BootstrapSeverity;
import org.richfaces.cdk.annotations.Attribute;

/**
 * Interface to add the "severity" attribute based on the BootstrapSeverity enum
 * 
 * @author <a href="http://pauldijou.fr">Paul Dijou</a>
 *
 */
public interface BootstrapSeverityProps {

    /**
     * In the same spirit of the JSF messaging severity, this attribute allows you to customize
     * the design of a component based on its severity.
     */
    @Attribute
    BootstrapSeverity getSeverity();
}
