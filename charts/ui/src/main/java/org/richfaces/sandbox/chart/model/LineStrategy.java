package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONObject;
import org.richfaces.sandbox.chart.ChartRendererBase;

/**
 *
 * @author Lukas Macko
 */
public class LineStrategy implements ChartStrategy{

    @Override
    public JSONObject export(ChartDataModel model) throws IOException{
        JSONObject output = new JSONObject();
        JSONArray data;

        //data
        data = new JSONArray();
        for (Iterator it = model.getData().entrySet().iterator(); it.hasNext();) {
            JSONArray point = new JSONArray();
            Map.Entry entry = (Map.Entry) it.next();
            point.put(entry.getKey());
            point.put(entry.getValue());
            data.put(point);
        }
        ChartRendererBase.addAttribute(output,"data", data);
        //label
        ChartRendererBase.addAttribute(output, "label", model.getAttributes().get("label"));
        //color
        ChartRendererBase.addAttribute(output, "color", model.getAttributes().get("color"));
        //points->symbol
        JSONObject points = new JSONObject();
        ChartRendererBase.addAttribute(points, "symbol", model.getAttributes().get("symbol"));
        
        ChartRendererBase.addAttribute(output, "points", points);

        return output;
    }
    
}
