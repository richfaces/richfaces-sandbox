package org.richfaces.sandbox.chart;

import javax.faces.event.FacesListener;

/**
 * Define listener for PlotClickEvent.
 * @author Lukas Macko
 */
public interface PlotClickListener extends FacesListener{
    
    public void processDataClick(PlotClickEvent event);
    
}
