package org.richfaces.sandbox.chart.model;

import java.util.HashMap;
import java.util.Map;
import org.richfaces.json.JSONArray;

/**
 *
 * @author Lukas Macko
 */
public abstract class ChartDataModel<T,S> {
    
    
    private Map<T,S> data;
    
    protected ChartStrategy strategy;
    
    public ChartDataModel(){
        data = new HashMap<T, S>();
    }
    
    public void put(T key,S value){
        data.put(key, value);
    }
    
    public void remove(T key){
        data.remove(key);
    }
    
    public JSONArray export(){
        return strategy.export(this);
    }
    
    public abstract Class getKeyType();
    
    public abstract Class getValueType();
    
}
