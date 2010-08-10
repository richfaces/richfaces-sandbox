/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.ajax4jsf.renderkit.html;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.component.UIAjaxStatus;
import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.renderkit.RendererBase;
import org.ajax4jsf.renderkit.RendererUtils.HTML;


/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.2 $ $Date: 2007/02/19 11:17:10 $
 *
 * Render status component as two span's, one for start request,
 * one for complete.
 */
public class AjaxStatusRenderer extends RendererBase
{
    /**
     *
     */
    public static final String RENDERER_TYPE = "org.ajax4jsf.components.AjaxStatusRenderer";
    
    /**
     *
     */
    public static final String START_STYLE = "display: none";

    /* (non-Javadoc)
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException
    {
        // find form clientId for wich we render status.
        String statusId = component.getClientId(context);
    	String tag = getTag(component);
        writer.startElement(tag, component);
		writer.writeAttribute(HTML.id_ATTRIBUTE, statusId , null);
        // render 2 span components for different states
        encodeSpan(writer, context, component,statusId, "start", tag);
        encodeSpan(writer, context, component,statusId, "stop", tag);
        writer.endElement(tag);
    }
    
    /**
     * Encode one span for start or stop status
     * @param context - current context
     * @param component - status component
     * @param state - name of state ( start or stop ) for span.
     * @param tag TODO
     * @throws IOException 
     */
    protected void encodeSpan(ResponseWriter writer, FacesContext context, UIComponent component, String id, String state, String tag) throws IOException {
        writer.startElement(tag, component);
        String spanId = id+"."+state;
		writer.writeAttribute(HTML.id_ATTRIBUTE, spanId , null);
        // Styles for concrete state.
        String style = getNamedAttribute(component, "Style", state);
        // for start state rendered style always disable display of status 
        // ( since it will enabled by JavaScript on start request ) 
        if("start".equals(state)) {
            if(null == style) {
                style = START_STYLE;
            } else {
                style += "; "+START_STYLE;
            }
        }
//        HtmlRendererUtils.renderHTMLAttribute(writer, "style", "style", style );
//        HtmlRendererUtils.renderHTMLAttribute(writer, "styleClass", "styleClass", getNamedAttribute(component, "StyleClass", state));
        if (null != style) {
			writer.writeAttribute("style", style, null);
		}
        String styleClass = getNamedAttribute(component, "StyleClass", state);
        if(null != styleClass){
			writer.writeAttribute("class", styleClass, null);        	
        }
//        getUtils().encodeAttribute(context,component,"class");
        getUtils().encodePassThru(context , component);
        // render facet or text.
        UIComponent facet = component.getFacet(state);
        if(null != facet) {
            renderChild(context, facet);
        } else {
            String namedAttribute = getNamedAttribute(component, "Text", state);
			if (null != namedAttribute) {
				writer.writeText(namedAttribute, "text");
			} 
//			else {
//				throw new FacesException("Status component must have one of '"+state+"' facet or '"+state+"Text' atribute");
//			}
        }
        writer.endElement(tag);
        // ENCODE onstart/onstop javaScript.
        Object eventHandler = component.getAttributes().get("on"+state);
        if(null != eventHandler){
        	StringBuffer script = new StringBuffer("\n");
        	script.append("window.document.getElementById('").append(spanId).append("').on").append(state).append("=");
        	JSFunctionDefinition function = new JSFunctionDefinition();
        	function.addToBody(eventHandler).addToBody(";").appendScript(script);
        	script.append(";\n");
        	getUtils().writeScript(context, component, script);
        }
    }
    
    /**
     * @param component 
     * @param name name of attribute for current state
     * @param state 'start' or 'stop' . all dublicated attributes foe Ajax state
     * have same syntax : startStyle/stopStyle ...
     * @return value of attribute for name and state
     */
    protected String getNamedAttribute(UIComponent component, String name, String state)
    {
        String fullName = (new StringBuffer(state).append(name)).toString();
        return (String) component.getAttributes().get(fullName);
    }

	protected Class getComponentClass() {
		// TODO Auto-generated method stub
		return UIAjaxStatus.class;
	}

	/**
	 * @param status
	 * @return
	 */
	private String getTag(UIComponent status) {
		// TODO Auto-generated method stub
		return "block".equals(status.getAttributes().get("layout"))?HTML.DIV_ELEM:HTML.SPAN_ELEM;
	}

	/* (non-Javadoc)
	 * @see javax.faces.render.Renderer#getRendersChildren()
	 */
	public boolean getRendersChildren() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
