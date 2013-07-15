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
import org.richfaces.ui.input.InputRendererBase;
import org.richfaces.util.InputUtils;

/**
 * Renderer for Autocomplete which handles rendering of suggestions as meta component.
 *
 * @author Lukas Fryc
 * @author Nick Belaevski
 */
public abstract class AutocompleteRendererBase extends InputRendererBase implements MetaComponentRenderer {

    public static final String RENDERER_TYPE = "org.richfaces.sandbox.AutocompleteRenderer";

    private static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();

    /**
     * Do not render content
     */
    @Override
    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    protected Object saveVar(FacesContext context, String var) {
        if (var != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            return requestMap.get(var);
        }

        return null;
    }

    protected void setVar(FacesContext context, String var, Object varObject) {
        if (var != null) {
            Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
            requestMap.put(var, varObject);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.ui.input.InputRendererBase#doDecode(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent)
     */
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        final AbstractAutocomplete autocomplete = (AbstractAutocomplete) component;
        final Map<String, String> requestParameters = context.getExternalContext().getRequestParameterMap();
        final PartialViewContext pvc = context.getPartialViewContext();

        // do not decode component if it is disabled
        if (InputUtils.isDisabled(autocomplete)) {
            return;
        }

        // set submitted value
        String value = requestParameters.get(component.getClientId(context) + "Input");
        if (value != null) {
            autocomplete.setSubmittedValue(value);
        }

        // if search is requested, the suggestions will be renderer to partial response
        String searchTerm = requestParameters.get(component.getClientId(context) + "SearchTerm");
        if (searchTerm != null) {
            String suggestionsMetaComponentId = autocomplete.getSuggestionsMetaComponentId(context);
            pvc.getRenderIds().add(suggestionsMetaComponentId);

            // force to render immediately
            context.renderResponse();
        }
    }

    /**
     * Returns suggestions as DataModel ready for iteration
     */
    @SuppressWarnings("unchecked")
    protected DataModel<Object> getSuggestionsAsDataModel(FacesContext facesContext, AbstractAutocomplete component) {
        Object suggestions = getSuggestions(facesContext, component);
        return asDataModel(suggestions);
    }

    /**
     * Encodes meta components supported by this renderer:
     *
     * <ul>
     * <li>@suggestions</li>
     * </ul>
     */
    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) throws IOException {
        if (AbstractAutocomplete.SUGGESTIONS_META_COMPONENT_ID.equals(metaComponentId)) {
            encodeSuggestions(context, component);
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
    }

    /**
     * Encodes suggestions to partial response
     */
    private void encodeSuggestions(FacesContext context, UIComponent component) throws IOException {
        List<Object> fetchValues = new ArrayList<Object>();
        String clientId = component.getClientId();

        PartialResponseWriter partialWriter = context.getPartialViewContext().getPartialResponseWriter();
        partialWriter.startUpdate(clientId + "Suggestions");
        renderSuggestions(context.getResponseWriter(), context, component);
        partialWriter.endUpdate();

        if (!fetchValues.isEmpty()) {
            Map<String, Object> dataMap = ExtendedPartialViewContext.getInstance(context).getResponseComponentDataMap();
            dataMap.put(component.getClientId(context), fetchValues);
        }
    }

    /**
     * The implementation of rendering suggestions must be provided so that it can be used by both on initial request as well as
     * during AJAX updates of suggestions.
     */
    public abstract void renderSuggestions(ResponseWriter responseWriter, FacesContext facesContext, UIComponent uiComponent)
            throws IOException;

    /**
     * Decoding of meta component isn't supported by this renderer
     */
    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the object representing suggestions as returned by application logic. It needs to be further converted in
     * {@link #getSuggestionsAsDataModel(FacesContext, AbstractAutocomplete)} method.
     */
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

    /**
     * Wraps the object representing suggestions to the {@link DataModel} instance so that we can be easily iterated over.
     */
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

    /*
     * (non-Javadoc)
     *
     * @see org.richfaces.ui.input.InputRendererBase#getConvertedValue(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.Object)
     */
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

    /**
     * Finds the appropriate converter for given {@link ValueExpression} represented by component's &#64;value attribute.
     */
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

    /**
     * Returns the &#64;clientFilterFunction as reference to the function so that it can be easily converted for registration in
     * component bridge.
     */
    public JSReference getClientFilterFunction(UIComponent component) {
        AbstractAutocomplete autocomplete = (AbstractAutocomplete) component;
        String clientFilter = (String) autocomplete.getAttributes().get("clientFilterFunction");

        if (clientFilter == null || clientFilter.isEmpty()) {
            return null;
        }

        return new JSReference(clientFilter);
    }
}
