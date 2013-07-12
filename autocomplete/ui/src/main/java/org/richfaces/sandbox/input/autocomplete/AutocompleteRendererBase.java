package org.richfaces.sandbox.input.autocomplete;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodNotFoundException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.ResultDataModel;
import javax.faces.model.ResultSetDataModel;
import javax.servlet.jsp.jstl.sql.Result;

import org.richfaces.context.ExtendedPartialViewContext;
import org.richfaces.el.GenericsIntrospectionService;
import org.richfaces.javascript.JSReference;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.services.ServiceTracker;
import org.richfaces.ui.common.meta.MetaComponentRenderer;
import org.richfaces.ui.common.meta.MetaComponentResolver;
import org.richfaces.ui.input.InputRendererBase;
import org.richfaces.util.InputUtils;

public abstract class AutocompleteRendererBase extends InputRendererBase implements MetaComponentRenderer {

    public static final String RENDERER_TYPE = "org.richfaces.sandbox.AutocompleteRenderer";

    private static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();

    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        final AbstractAutocomplete autocomplete = (AbstractAutocomplete) component;
        final Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();

        if (InputUtils.isDisabled(autocomplete)) {
            return;
        }

        String value = requestParameters.get(component.getClientId(context) + "Input");
        if (value != null) {
            autocomplete.setSubmittedValue(value);
        }

        String searchTerm = requestParameters.get(component.getClientId(context) + "SearchTerm");
        if (searchTerm != null) {
            PartialViewContext pvc = context.getPartialViewContext();
            pvc.getRenderIds().add(
                    component.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR
                            + AbstractAutocomplete.ITEMS_META_COMPONENT_ID);

            context.renderResponse();
        }
    }

    @SuppressWarnings("unchecked")
    protected DataModel<Object> getSuggestionsDataModel(FacesContext facesContext, AbstractAutocomplete component) {
        Object suggestions = getSuggestions(facesContext, component);
        return asDataModel(suggestions);
    }

    @Override
    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractAutocomplete.ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {

            List<Object> fetchValues = new ArrayList<Object>();
            String clientId = component.getClientId();

            PartialResponseWriter partialWriter = context.getPartialViewContext().getPartialResponseWriter();
            partialWriter.startUpdate(clientId + "Suggestions");
            renderSuggestions(FacesContext.getCurrentInstance().getResponseWriter(), context, component);
            partialWriter.endUpdate();

            if (!fetchValues.isEmpty()) {
                Map<String, Object> dataMap = ExtendedPartialViewContext.getInstance(context).getResponseComponentDataMap();
                dataMap.put(component.getClientId(context), fetchValues);
            }
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    public abstract void renderSuggestions(ResponseWriter responseWriter, FacesContext facesContext, UIComponent uiComponent)
            throws IOException;

    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    protected Object getSuggestions(FacesContext facesContext, AbstractAutocomplete component) {
        MethodExpression autocompleteMethod = component.getAutocompleteMethod();
        if (autocompleteMethod != null) {
            Map<String, String> requestParameters = facesContext.getExternalContext().getRequestParameterMap();
            String searchTerm = requestParameters.get(component.getClientId(facesContext) + "SearchTerm");
            try {
                try {
                    return autocompleteMethod.invoke(facesContext.getELContext(), new Object[] { facesContext, component,
                            searchTerm });
                } catch (MethodNotFoundException e) {
                    ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
                    autocompleteMethod = expressionFactory.createMethodExpression(facesContext.getELContext(),
                            autocompleteMethod.getExpressionString(), Object.class, new Class[] { String.class });
                    return autocompleteMethod.invoke(facesContext.getELContext(), new Object[] { searchTerm });
                }
            } catch (ELException ee) {
                LOGGER.error(ee.getMessage(), ee);
            }
        } else {
            return component.getAutocompleteList();
        }

        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public DataModel asDataModel(Object suggestions) {
        if (suggestions instanceof Object[]) {
            return new ArrayDataModel((Object[]) suggestions);
        } else if (suggestions instanceof List) {
            return new ListDataModel((List<Object>) suggestions);
        } else if (suggestions instanceof Result) {
            return new ResultDataModel((Result) suggestions);
        } else if (suggestions instanceof ResultSet) {
            return new ResultSetDataModel((ResultSet) suggestions);
        } else if (suggestions != null) {
            List<Object> temp = new ArrayList<Object>();
            Iterator<Object> iterator = ((Iterable<Object>) suggestions).iterator();
            while (iterator.hasNext()) {
                temp.add(iterator.next());
            }
            return new ListDataModel(temp);
        } else {
            return new ListDataModel(null);
        }
    }

    private Converter getConverterForValue(FacesContext context, UIComponent component) {
        Converter converter = ((ValueHolder) component).getConverter();
        if (converter == null) {
            ValueExpression expression = component.getValueExpression("value");

            if (expression != null) {
                Class<?> containerClass = ServiceTracker.getService(context, GenericsIntrospectionService.class)
                        .getContainerClass(context, expression);

                converter = InputUtils.getConverterForType(context, containerClass);
            }
        }
        return converter;
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object val) throws ConverterException {
        String s = (String) val;
        Converter converter = getConverterForValue(context, component);
        if (converter != null) {
            return converter.getAsObject(context, component, s);
        } else {
            return s;
        }
    }

    public JSReference getClientFilterFunction(UIComponent component) {
        AbstractAutocomplete autocomplete = (AbstractAutocomplete) component;
        String clientFilter = (String) autocomplete.getAttributes().get("clientFilterFunction");
        if (clientFilter != null && clientFilter.length() != 0) {
            return new JSReference(clientFilter);
        }

        return null;
    }
}
