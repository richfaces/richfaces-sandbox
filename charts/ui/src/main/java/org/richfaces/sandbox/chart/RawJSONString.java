
package org.richfaces.sandbox.chart;

import org.richfaces.json.JSONString;

/**
 * <p> The aim of this class <b>RawJSONString</b> is to output content
 * into JSON without quotes</p>
 * @author Lukas Macko
 */
public class RawJSONString implements JSONString{
    
    private String string;
    
    public RawJSONString(String s){
        this.string=s;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
       
    @Override
    public String toJSONString() {
        return getString();
    }
    
}
