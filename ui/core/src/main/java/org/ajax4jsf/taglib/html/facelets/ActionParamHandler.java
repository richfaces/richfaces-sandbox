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

package org.ajax4jsf.taglib.html.facelets;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;

import org.ajax4jsf.Messages;
import org.ajax4jsf.component.UIActionParameter;

/**
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:23 $
 *
 */
public class ActionParamHandler extends ComponentHandler {

    /**
	 * @author shura (latest modification by $Author: alexsmirnov $)
	 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:23 $
	 *
	 */
	public static class ActionParamMetaRule extends MetaRule {

		/* (non-Javadoc)
		 * @see org.ajax4jsf.tag.SuggestionHandler.SuggestionMetaRule#applyRule(java.lang.String, com.sun.facelets.tag.TagAttribute, com.sun.facelets.tag.MetadataTarget)
		 */
		public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
	        if (meta.isTargetInstanceOf(UIActionParameter.class)) {
	        	if ("assignTo".equals(name)) {
					return new AssignToValueBindingMetadata(attribute);
				} else if ("converter".equals(name)) {
	                if (attribute.isLiteral()) {
	                    return new LiteralConverterMetadata(attribute.getValue());
	                } else {
	                    return new DynamicConverterMetadata(attribute);
	                }
					
				} else if ("actionListener".equals(name)) {
					return new ActionListenerMetadata(attribute);
				}
	        }

			return null;
		}

		
	}
	
    final static class LiteralConverterMetadata extends Metadata {

        private final String converterId;

        public LiteralConverterMetadata(String converterId) {
            this.converterId = converterId;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIActionParameter) instance).setConverter(ctx.getFacesContext()
                    .getApplication().createConverter(this.converterId));
        }
    }

    final static class DynamicConverterMetadata extends Metadata {

        private final TagAttribute attr;

        public DynamicConverterMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIActionParameter) instance).setConverter((Converter) this.attr
                    .getObject(ctx, Converter.class));
        }
    }

    final static class AssignToValueBindingMetadata extends Metadata {

        private final TagAttribute attr;

        public AssignToValueBindingMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIActionParameter) instance).setAssignToBinding(attr.getValueExpression(ctx,
                            Object.class));
        }
    }

    final static class ActionListenerMetadata extends Metadata {

    	private static final Class[] SIGNATURE = new Class[] {ActionEvent.class};
    	
    	private final TagAttribute attribute;

		public ActionListenerMetadata(TagAttribute attribute) {
			super();
			this.attribute = attribute;
		}
    	
    	public void applyMetadata(FaceletContext ctx, Object instance) {
            ((UIActionParameter) instance).setActionListener(
            		attribute.getMethodExpression(ctx, null, SIGNATURE));
    	}
    };
    
	private TagAttribute _assignTo;
    private TagAttribute _converter;
    private TagAttribute _actionListener;
    
    /**
	 * @param config
	 */
	public ActionParamHandler(ComponentConfig config) {
		super(config);
		_assignTo = getAttribute("assignTo");
		_converter = getAttribute("converter");
		_actionListener = getAttribute("actionListener");

		if(null != _assignTo) {
            if (_assignTo.isLiteral()) {
                throw new TagAttributeException(this.tag, this._assignTo, Messages.getMessage(Messages.MUST_BE_EXPRESSION_ERROR));
            }
		}
		
		if (null != _actionListener) {
            if (_actionListener.isLiteral()) {
                throw new TagAttributeException(this.tag, this._actionListener, Messages.getMessage(Messages.MUST_BE_EXPRESSION_ERROR));
            }
		}
		// TODO Auto-generated constructor stub
	}
    /*
     * (non-Javadoc)
     * 
     * @see com.sun.facelets.FaceletHandler#apply(com.sun.facelets.FaceletContext,
     *      javax.faces.component.UIComponent)
     */
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        if (parent instanceof ActionSource)
        {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Application application = facesContext.getApplication();
            if (_assignTo != null) {
                UIActionParameter al = (UIActionParameter)c;
                // TODO - in jsf 1.2 use ELValueExpression
//                al.setAssignToBinding(application.createValueBinding(_assignTo.getValue()));
//                if (_converter != null) {
//                    Converter converter = application.createConverter(_converter.getValue(ctx));
//                    al.setConverter(converter);
//                }
                ((ActionSource)parent).addActionListener(al);
            }
        }
    }

	private static final ActionParamMetaRule actionParamMetaRule = new ActionParamMetaRule();

	/* (non-Javadoc)
	 * @see org.ajax4jsf.tag.AjaxComponentHandler#createMetaRuleset(java.lang.Class)
	 */
	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRules = super.createMetaRuleset(type);
		metaRules.addRule(actionParamMetaRule);
		return metaRules;
	}

}
