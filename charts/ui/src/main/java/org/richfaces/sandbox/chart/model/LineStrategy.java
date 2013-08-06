package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import org.richfaces.json.JSONObject;
import org.richfaces.sandbox.chart.ChartRendererBase;

/**
 *
 * @author Lukas Macko
 */
public class LineStrategy implements ChartStrategy{

    @Override
    public Object export(ChartDataModel model) throws IOException{
        JSONObject output = model.defaultExport();
        
        //points->symbol
        Object symbol = model.getAttributes().get("symbol");
        if(symbol!=null){
            JSONObject points = new JSONObject();
            ChartRendererBase.addAttribute(points, "symbol", model.getAttributes().get("symbol"));
            ChartRendererBase.addAttribute(points, "show", true);
            ChartRendererBase.addAttribute(output, "points", points);
            
            //connect symblos with line
            JSONObject lines = new JSONObject();
            ChartRendererBase.addAttribute(lines, "show", true);
            ChartRendererBase.addAttribute(output, "lines", lines);

        }
        return output;
    }
    
}
