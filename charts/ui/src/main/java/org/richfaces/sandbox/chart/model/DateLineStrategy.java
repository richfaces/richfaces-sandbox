package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONObject;
import org.richfaces.sandbox.chart.ChartRendererBase;

/**
 *
 * @author Lukas Macko
 */
class DateLineStrategy implements ChartStrategy {

    @Override
    public Object export(ChartDataModel model) throws IOException {
        JSONObject output = new JSONObject();
        JSONArray jsdata;

        //data
        jsdata = new JSONArray();
        for (Iterator it = model.getData().entrySet().iterator(); it.hasNext();) {
            JSONArray point = new JSONArray();
            Map.Entry entry = (Map.Entry) it.next();
            point.put(((Date)entry.getKey()).getTime());
            point.put(entry.getValue());
            jsdata.put(point);
        }
        ChartRendererBase.addAttribute(output,"data", jsdata);
        //TODO label
        //ChartRendererBase.addAttribute(output, "label", getAttributes().get("label"));
        //TODO color
        //ChartRendererBase.addAttribute(output, "color", getAttributes().get("color"));
        
        return output;
    }

   
    
}
