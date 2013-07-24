package org.richfaces.sandbox.chart.component;

import javax.faces.component.UIComponentBase;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.Tag;


/**
 *
 * @author Lukas Macko
 */
@JsfComponent(
    type = "org.richfaces.sandbox.chart.component.legend",
    family = "org.richfaces.sandbox.ChartFamily",
    tag = @Tag(name = "legend"))
public abstract class AbstractLegend extends UIComponentBase{

    @Attribute
    public abstract String getPosition();
    
    @Attribute
    public abstract String getSorting();
    
    
}
