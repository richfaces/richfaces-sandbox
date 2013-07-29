package org.richfaces.sandbox.chart.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.richfaces.json.JSONObject;

/**
 *
 * @author Lukas Macko
 */
public abstract class ChartDataModel<T,S> {
    
    
    private Map<T,S> data;
    
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
    
    public JSONObject export() throws IOException{
        return strategy.export(this);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    
    
    
    public abstract Class getKeyType();
    
    public abstract Class getValueType();
    
}
