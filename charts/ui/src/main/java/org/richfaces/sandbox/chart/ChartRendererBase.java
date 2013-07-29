package org.richfaces.sandbox.chart;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import org.richfaces.json.JSONException;
import org.richfaces.json.JSONObject;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.sandbox.chart.component.AbstractChart;
import org.richfaces.sandbox.chart.component.AbstractLegend;
import org.richfaces.sandbox.chart.component.AbstractSeries;
import org.richfaces.sandbox.chart.component.AbstractXaxis;
import org.richfaces.sandbox.chart.component.AbstractYaxis;
import static java.util.Arrays.asList;


/**
 *
 *
 * @author Lukas Macko
 */
public abstract class ChartRendererBase extends RendererBase {

    public static final String RENDERER_TYPE = "org.richfaces.sandbox.ChartRenderer";

    /**
     * Method adds key-value pair to object.
     *
     * @param obj
     * @param key
     * @param value
     * @return
     * @throws IOException if put to JSONObject fails
     */
    public static JSONObject addAttribute(JSONObject obj, String key, Object value) throws IOException {
        try {
            if (value != null && !value.equals("")) {
                obj.put(key, value);
            }
        } catch (JSONException ex) {
            throw new IOException("JSONObject put failed.");
        }
        return obj;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        //cyklus cez tagy potomkov(xaxis,yaxis,legend), ktory nakopiruje atributy do rodicovskeho tagy

        AbstractChart ch = (AbstractChart) component;
        AbstractChart chart = (AbstractChart) component;

        chart.visitTree(VisitContext.createVisitContext(FacesContext.getCurrentInstance()), new VisitChart(ch));
    }

    public JSONObject getOpts(FacesContext context, UIComponent component) throws IOException {
        JSONObject obj = new JSONObject();
        JSONObject xaxis = new JSONObject();
        addAttribute(xaxis, "min", component.getAttributes().get("xmin"));
        addAttribute(xaxis, "max", component.getAttributes().get("xmax"));
        addAttribute(xaxis, "pad", component.getAttributes().get("xpad"));
        addAttribute(xaxis, "axisLabel", component.getAttributes().get("xlabel"));
        addAttribute(xaxis, "format", component.getAttributes().get("xformat"));
        
        JSONObject yaxis = new JSONObject();
        addAttribute(yaxis, "min", component.getAttributes().get("ymin"));
        addAttribute(yaxis, "max", component.getAttributes().get("ymax"));
        addAttribute(yaxis, "pad", component.getAttributes().get("ypad"));
        addAttribute(yaxis, "axisLabel", component.getAttributes().get("ylabel"));
        addAttribute(yaxis, "format", component.getAttributes().get("yformat"));
        
        addAttribute(obj, "xaxis", xaxis);
        addAttribute(obj, "yaxis", yaxis);
        return obj;
    }
    
    class VisitChart implements VisitCallback{
        
            private AbstractChart chart;
            
            public VisitChart(AbstractChart ch){
                this.chart = ch;
            }
            
            private void copyAttr(UIComponent src, UIComponent target, String prefix, String attr){
                Object val = src.getAttributes().get(attr);
                if(val!=null){
                    target.getAttributes().put(prefix+attr, val);
                }
            }
            
            /**
             * Copy attributes from source UIComponent to target.
             * @param src
             * @param target
             * @param prefix
             * @param attrs 
             */ 
            private void copyAttrs(UIComponent src, UIComponent target, String prefix, List<String> attrs) {
                for (Iterator<String> it = attrs.iterator(); it.hasNext();) {
                    String attr = it.next();
                    copyAttr(src, target, prefix, attr);
                }
            }

            @Override
            public VisitResult visit(VisitContext context, UIComponent target) {
                

                if (target instanceof AbstractLegend) {
                   copyAttrs(target, chart, "",asList("position","sorting"));
                } else if (target instanceof AbstractSeries) {
                    
                } else if (target instanceof AbstractXaxis) {
                    copyAttrs(target, chart, "x",asList("min","max","pad","label","format"));
                } else if (target instanceof AbstractYaxis) {
                    copyAttrs(target, chart, "y",asList("min","max","pad","label","format"));
                }
                return VisitResult.ACCEPT;
            }
        
    }
}
