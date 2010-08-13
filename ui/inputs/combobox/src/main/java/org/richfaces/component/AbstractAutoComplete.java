/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.component;

import java.io.IOException;
import java.util.List;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.ajax4jsf.Messages;
import org.ajax4jsf.util.ELUtils;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Signature;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.MetaComponentEncoder;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.renderkit.MetaComponentRenderer;

/**
 * @author Nick Belaevski
 *
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.view.facelets.AutoCompleteHandler"), 
    renderer = @JsfRenderer(type = "org.richfaces.AutoCompleteRenderer")
)
public abstract class AbstractAutoComplete extends UIInput implements MetaComponentResolver, MetaComponentEncoder {

    public static final String ITEMS_META_COMPONENT_ID = "items";

    public static final String COMPONENT_TYPE = "org.richfaces.AutoComplete";

    public static final String COMPONENT_FAMILY = UIInput.COMPONENT_FAMILY;

    //TODO nick - change to Object - https://jira.jboss.org/browse/RF-8897
    public abstract Object getAutocompleteList();

    @Attribute(signature = @Signature(returnType = Object.class, parameters = { FacesContext.class, UIComponent.class,
            String.class }))
    public abstract MethodExpression getAutocompleteMethod();
    public abstract void setAutocompleteMethod(MethodExpression expression);
    public abstract void setItemConverter(Converter converter);
    @Attribute(literal = true)
    public abstract String getVar();

    //TODO nick - el-only?
    @Attribute(literal = false)
    public abstract Object getFetchValue();
    
    @Attribute(defaultValue = "1")
    public abstract int getMinChars();

    @Attribute
    public abstract String getFilterFunction();
    
    @Attribute
    public abstract String getMode();
    
    @Attribute
    public abstract String getLayout();
    
    @Attribute
    public abstract Converter getItemConverter();
    
    @Attribute(defaultValue = "false")
    public abstract boolean isAutoFill();
    
    @Attribute(defaultValue = "false")
    public abstract boolean isDisabled();
    
    @Attribute(defaultValue = "false")
    public abstract boolean isShowButton();
    
    @Attribute(defaultValue = "false")
    public abstract boolean isSelectFirst();
    
    @Attribute(events = @EventName("click"))
    public abstract String getOnclick();

    @Attribute(events = @EventName("dblclick"))
    public abstract String getOndblclick();

    @Attribute(events = @EventName("mousedown"))
    public abstract String getOnmousedown();

    @Attribute(events = @EventName("mouseup"))
    public abstract String getOnmouseup();

    @Attribute(events = @EventName("mouseover"))
    public abstract String getOnmouseover();

    @Attribute(events = @EventName("mousemove"))
    public abstract String getOnmousemove();

    @Attribute(events = @EventName("mouseout"))
    public abstract String getOnmouseout();

    @Attribute(events = @EventName("keypress"))
    public abstract String getOnkeypress();

    @Attribute(events = @EventName("keydown"))
    public abstract String getOnkeydown();

    @Attribute(events = @EventName("keyup"))
    public abstract String getOnkeyup();
    
    @Attribute(events = @EventName("listclick"))
    public abstract String getOnlistclick();

    @Attribute(events = @EventName("listdblclick"))
    public abstract String getOnlistdblclick();

    @Attribute(events = @EventName("listmousedown"))
    public abstract String getOnlistmousedown();

    @Attribute(events = @EventName("listmouseup"))
    public abstract String getOnlistmouseup();

    @Attribute(events = @EventName("listmouseover"))
    public abstract String getOnlistmouseover();

    @Attribute(events = @EventName("listmousemove"))
    public abstract String getOnlistmousemove();

    @Attribute(events = @EventName("listmouseout"))
    public abstract String getOnlistmouseout();

    @Attribute(events = @EventName("listkeypress"))
    public abstract String getOnlistkeypress();

    @Attribute(events = @EventName("listkeydown"))
    public abstract String getOnlistkeydown();

    @Attribute(events = @EventName("listkeyup"))
    public abstract String getOnlistkeyup();
    
    @Attribute(events = @EventName("change"))
    public abstract String getOnchange();
    
    @Attribute(events = @EventName("blur"))
    public abstract String getOnblur();
    
    @Attribute(events = @EventName("focus"))
    public abstract String getOnfocus();
    
    @Attribute(events = @EventName("listblur"))
    public abstract String getOnlistblur();
    
    @Attribute(events = @EventName("listfocus"))
    public abstract String getOnlistfocus();
    
    @Attribute(events = @EventName("begin"))
    public abstract String getOnbegin();
    
    @Attribute(events = @EventName("error"))
    public abstract String getOnerror();
    
    @Attribute(events = @EventName("complete"))
    public abstract String getOncomplete();
    
    @Attribute(events = @EventName("beforedomupdate"))
    public abstract String getOnbeforedomupdate();
    
    @Override
	public Converter getConverter() {
		
		Converter converter = super.getConverter();
		if(converter == null) {
			converter = getConverterForValue(FacesContext.getCurrentInstance());
		}
		
		return converter;
	}
	
	private Converter getConverterForType(FacesContext context, Class <?> type) {
		
		if (!Object.class.equals(type) && type != null) {
			Application application = context.getApplication();
			return application.createConverter(type);
		}

		return null;
	}
	
	public Converter getConverterForValue(FacesContext context) {
		Converter converter = null;
		ValueExpression expression = this.getValueExpression("value");

		if (expression != null) {
			Class<?> containerClass = ELUtils.getContainerClass(context, expression);

			converter = getConverterForType(context, containerClass);
		}
		
		return converter;
	}
	
    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (ITEMS_META_COMPONENT_ID.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }

        return null;
    }
    
    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (context instanceof ExtendedVisitContext) {
            ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
            if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                VisitResult result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback, ITEMS_META_COMPONENT_ID);
                if (result == VisitResult.COMPLETE) {
                    return true;
                } else if (result == VisitResult.REJECT) {
                    return false;
                }
            }
        }

        return super.visitTree(context, callback);
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }
}
