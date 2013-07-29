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

    /**
     * Chart legend position
     * allowed values: 
     * <ul>
     * <li>nw - top left </li>
     * <li>sw - bottom left</li>
     * <li>ne - top right</li>
     * <li>se - bottom right</li>
     * </ul>
     * 
     */
    @Attribute
    public abstract PositionType getPosition();
    
    
    /**
     * The attribute defines the order of series labels in legend.
     * If not specified the order labels is the same as the order of 
     * series in facelet.
     * Allowed values:
     * <ul>
     * <li>ascending</li>
     * <li>descending</li>
     * </ul>
     * 
     */
    @Attribute
    public abstract SortingType getSorting();
    
    public enum PositionType{
      nw,sw,ne,se  
    };
    
    public enum SortingType{
      ascending,descending  
    }
}
