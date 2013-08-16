package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.sandbox.chart.ChartRendererBase;

/**
 *
 * @author Lukas Macko
 */
class CategoryBarStrategy implements ChartStrategy {

    public CategoryBarStrategy() {
        
    }

    @Override
    public Object export(ChartDataModel model) throws IOException {
       JSONObject obj = new JSONObject();
       JSONObject data = new JSONObject();
               
       
       for (Iterator it = model.getData().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            ChartRendererBase.addAttribute(data, entry.getKey().toString(), entry.getValue());
        }
       ChartRendererBase.addAttribute(obj, "data", data);
       
       JSONObject bars = new JSONObject();
       ChartRendererBase.addAttribute(bars, "show", true);
       ChartRendererBase.addAttribute(obj, "bars", bars);
       
       //label
       ChartRendererBase.addAttribute(obj, "label", model.getAttributes().get("label"));
       //color
       ChartRendererBase.addAttribute(obj, "color", model.getAttributes().get("color"));
       
       return obj;
    }
    
}
