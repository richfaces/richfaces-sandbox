package org.richfaces.sandbox.chart.model;

import org.richfaces.json.JSONArray;

/**
 *
 * @author Lukas Macko
 */
public interface ChartStrategy {
    
    public JSONArray export(ChartDataModel model);
}
