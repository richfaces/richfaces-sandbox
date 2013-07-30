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
import javax.faces.FacesException;
import org.richfaces.json.JSONArray;
import org.richfaces.sandbox.chart.component.AbstractPoint;
import org.richfaces.sandbox.chart.model.ChartDataModel;
import org.richfaces.sandbox.chart.model.NumberChartDataModel;

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

    public JSONObject getOpts(FacesContext context, UIComponent component) throws IOException {
        JSONObject obj = new JSONObject();

        JSONObject xaxis = new JSONObject();
        addAttribute(xaxis, "min", component.getAttributes().get("xmin"));
        addAttribute(xaxis, "max", component.getAttributes().get("xmax"));
        addAttribute(xaxis, "autoscaleMargin", component.getAttributes().get("xpad"));
        addAttribute(xaxis, "axisLabel", component.getAttributes().get("xlabel"));
        addAttribute(xaxis, "format", component.getAttributes().get("xformat"));

        JSONObject yaxis = new JSONObject();
        addAttribute(yaxis, "min", component.getAttributes().get("ymin"));
        addAttribute(yaxis, "max", component.getAttributes().get("ymax"));
        addAttribute(yaxis, "autoscaleMargin", component.getAttributes().get("ypad"));
        addAttribute(yaxis, "axisLabel", component.getAttributes().get("ylabel"));
        addAttribute(yaxis, "format", component.getAttributes().get("yformat"));

        JSONObject legend = new JSONObject();
        addAttribute(legend, "position", component.getAttributes().get("position"));
        addAttribute(legend, "sorted", component.getAttributes().get("sorting"));

        addAttribute(obj, "xaxis", xaxis);
        addAttribute(obj, "yaxis", yaxis);
        addAttribute(obj, "legend", legend);
        return obj;
    }

    public JSONArray getData(FacesContext ctx, UIComponent component) {
        return (JSONArray) component.getAttributes().get("data");
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);

        AbstractChart chart = (AbstractChart) component;

        VisitChart visitCallback = new VisitChart(chart);
        //copy attributes to parent tag and process data
        chart.visitTree(VisitContext.createVisitContext(FacesContext.getCurrentInstance()), visitCallback);

        //store data to parent tag
        component.getAttributes().put("data", visitCallback.getData());


    }

    class VisitChart implements VisitCallback {

        private AbstractChart chart;
        private JSONArray data;

        public VisitChart(AbstractChart ch) {
            this.chart = ch;
            this.data = new JSONArray();
        }

        private void copyAttr(UIComponent src, UIComponent target, String prefix, String attr) {
            Object val = src.getAttributes().get(attr);
            if (val != null) {
                target.getAttributes().put(prefix + attr, val);
            }
        }

        /**
         * Copy attributes from source UIComponent to target.
         *
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
                copyAttrs(target, chart, "", asList("position", "sorting"));
            } else if (target instanceof AbstractSeries) {
                AbstractSeries s = (AbstractSeries) target;
                ChartDataModel model = s.getData();

                if (model == null) {
                    /**
                     * data model priority: if there is data model passed
                     * through data attribute use it. Otherwise nested point
                     * tags are expected.
                     */
                    VisitSeries seriesCallback = new VisitSeries(s.getType());
                    s.visitTree(VisitContext.createVisitContext(FacesContext.getCurrentInstance()), seriesCallback);
                    model = seriesCallback.getModel();
                }
                model.setAttributes(s.getAttributes());
                
                try {
                    data.put(model.export());
                } catch (IOException ex) {
                        throw new FacesException(ex);
                }

            } else if (target instanceof AbstractXaxis) {
                copyAttrs(target, chart, "x", asList("min", "max", "pad", "label", "format"));
            } else if (target instanceof AbstractYaxis) {
                copyAttrs(target, chart, "y", asList("min", "max", "pad", "label", "format"));
            }
            return VisitResult.ACCEPT;
        }

        public JSONArray getData() {
            return data;
        }
    }

    class VisitSeries implements VisitCallback {

        private ChartDataModel model=null;
        private ChartDataModel.ChartType type;

        public VisitSeries(ChartDataModel.ChartType type) {
            this.type = type;
         
        }

        @Override
        public VisitResult visit(VisitContext context, UIComponent target) {
            
            if (target instanceof AbstractPoint) {

                AbstractPoint p = (AbstractPoint) target;

                Object x = p.getX();
                Object y = p.getY();

                //the first point determine type of data model 
                if (model == null) {
                    if (x instanceof Number && y instanceof Number) {
                        model = new NumberChartDataModel(type);
                    } else {
                        throw new IllegalArgumentException("Not supported type");
                    }
                }


                if (model.getKeyType().isAssignableFrom(x.getClass()) &&
                    model.getValueType().isAssignableFrom(y.getClass())) {
                    
                    
                    if(x instanceof Number && y instanceof Number){
                       model.put((Number)x,(Number)y);
                    }
                    else{
                       throw new IllegalArgumentException("Not supported types " + x.getClass()+" " +y.getClass()+" for "+model.getClass());
                    }
                    
                   
                    
                    
                } else {
                    throw new IllegalArgumentException("Not supported types " + x.getClass()+" " +y.getClass()+" for "+model.getClass());
                }
            }
            return VisitResult.ACCEPT;
        }

        public ChartDataModel getModel() {
            return model;
        }
    }
}
