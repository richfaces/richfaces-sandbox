package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.richfaces.json.JSONArray;
import org.richfaces.json.JSONObject;
import org.richfaces.sandbox.chart.ChartRendererBase;

/**
 *
 * @author Lukas Macko
 */
public abstract class ChartDataModel<T,S> {
    
    
    private Map<T,S> data;
    
    protected ChartType type;
    
    protected ChartStrategy strategy;
    
    private Map<String,Object> attributes;
    
    public ChartDataModel(){
        data = new HashMap<T, S>();
    }
    
    public void setData(Map<T,S> data){
        this.data = data;
    }
    
    public Map<T,S> getData(){
        return data;
    }
    
    public void put(T key,S value){
        data.put(key, value);
    }
    
    public void remove(T key){
        data.remove(key);
    }
    
    public JSONObject defaultExport() throws IOException{
        JSONObject output = new JSONObject();
        JSONArray jsdata;

        //data
        jsdata = new JSONArray();
        for (Iterator it = getData().entrySet().iterator(); it.hasNext();) {
            JSONArray point = new JSONArray();
            Map.Entry entry = (Map.Entry) it.next();
            point.put(entry.getKey());
            point.put(entry.getValue());
            jsdata.put(point);
        }
        ChartRendererBase.addAttribute(output,"data", jsdata);
        //label
        ChartRendererBase.addAttribute(output, "label", getAttributes().get("label"));
        //color
        ChartRendererBase.addAttribute(output, "color", getAttributes().get("color"));
        
        return output;
    }
    
    public JSONObject export() throws IOException{
        return strategy.export(this);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public ChartType getType() {
        return type;
    }
    
    
    
    
    public abstract Class getKeyType();
    
    public abstract Class getValueType();
    
    public enum ChartType{
        line,bar,pie
    }
    
}
