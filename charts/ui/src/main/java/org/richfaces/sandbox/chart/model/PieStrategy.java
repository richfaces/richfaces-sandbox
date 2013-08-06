package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;

/**
 *
 * @author Lukas Macko
 */
public class PieStrategy implements ChartStrategy{

    @Override
    public Object export(ChartDataModel model) throws IOException {
        JSONArray jsData = new JSONArray();
        for (Iterator it = model.getData().entrySet().iterator(); it.hasNext();) {
            JSONObject point = new JSONObject();
            Map.Entry entry = (Map.Entry) it.next();
            try{
                point.put("label", entry.getKey());
                point.put("data", entry.getValue());
            }catch (JSONException ex){
                throw new IOException(ex);
            }
            jsData.put(point);
        }
        return jsData;
    }

    
    
}
