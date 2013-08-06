package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import org.richfaces.json.JSONObject;
import org.richfaces.sandbox.chart.ChartRendererBase;

/**
 *
 * @author Lukas Macko
 */
public class BarStrategy implements ChartStrategy{

    @Override
    public Object export(ChartDataModel model) throws IOException {
       JSONObject obj = model.defaultExport();
       
       JSONObject bars = new JSONObject();
       ChartRendererBase.addAttribute(bars, "show", true);
       ChartRendererBase.addAttribute(obj, "bars", bars);
       
       return obj;
    }
    
}
