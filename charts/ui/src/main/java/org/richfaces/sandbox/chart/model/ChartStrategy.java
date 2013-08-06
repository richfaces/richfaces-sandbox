package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import org.richfaces.json.JSONObject;

/**
 *
 * @author Lukas Macko
 */
public interface ChartStrategy {
    
    public Object export(ChartDataModel model)throws IOException;
}
