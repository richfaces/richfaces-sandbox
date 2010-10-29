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

import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UpdateModelException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;

import org.ajax4jsf.model.DataComponentState;
import org.ajax4jsf.model.ExtendedDataModel;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.appplication.FacesMessages;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.convert.SequenceRowKeyConverter;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.model.TreeDataModelImpl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
@JsfComponent(
    type = AbstractTree.COMPONENT_TYPE,
    family = AbstractTree.COMPONENT_FAMILY, 
    tag = @Tag(name = "tree"),
    renderer = @JsfRenderer(type = "org.richfaces.TreeRenderer")
)
public abstract class AbstractTree extends UIDataAdaptor {

    public static final String COMPONENT_TYPE = "org.richfaces.Tree";

    public static final String COMPONENT_FAMILY = "org.richfaces.Tree";

    private static final Predicate<UIComponent> RENDERED_UITREE_NODE = new Predicate<UIComponent>() {
        public boolean apply(UIComponent input) {
            return (input instanceof AbstractTreeNode) && input.isRendered();
        };
    };

    private static final Converter ROW_KEY_CONVERTER = new SequenceRowKeyConverter();

    private enum PropertyKeys {
        expanded
    }

    private transient UIComponent decoderHelper = null;
    
    public AbstractTree() {
        setRendererType("org.richfaces.TreeRenderer");
    }

    public abstract Object getValue();

    public abstract boolean isImmediate();

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Attribute(defaultValue = "SwitchType.DEFAULT")
    public abstract SwitchType getToggleMode();

    @SuppressWarnings("unchecked")
    protected Boolean getLocalExpandedValue(FacesContext facesContext) {
        Map<String, Object> stateMap = (Map<String, Object>) getStateHelper().get(PropertyKeys.expanded);
        if (stateMap == null) {
            return null;
        }

        String key = this.getClientId(facesContext);
        return (Boolean) stateMap.get(key);
    }

    public boolean isExpanded() {
        FacesContext context = getFacesContext();
        Boolean localExpandedValue = getLocalExpandedValue(context);
        if (localExpandedValue != null) {
            return localExpandedValue.booleanValue();
        }

        ValueExpression ve = getValueExpression(PropertyKeys.expanded.toString());
        if (ve != null) {
            return Boolean.TRUE.equals(ve.getValue(context.getELContext()));
        }

        return false;
    }

    public void setExpanded(boolean newValue) {
        getStateHelper().put(PropertyKeys.expanded, this.getClientId(getFacesContext()), newValue);
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.UIDataAdaptor#createExtendedDataModel()
     */
    @Override
    protected ExtendedDataModel<?> createExtendedDataModel() {
        TreeDataModelImpl model = new TreeDataModelImpl();
        model.setWrappedData(getValue());
        return model;
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.UIDataAdaptor#createComponentState()
     */
    @Override
    protected DataComponentState createComponentState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Converter getRowKeyConverter() {
        Converter converter = super.getRowKeyConverter();
        if (converter == null) {
            converter = ROW_KEY_CONVERTER;
        }
        return converter;
    }

    public Iterator<Object> getChildrenIterator(FacesContext faces, Object rowKey) {
        return ((TreeDataModelImpl) getExtendedDataModel()).getChildrenIterator(faces, rowKey);
    }

    public AbstractTreeNode getTreeNodeComponent() {
        if (getChildCount() == 0) {
            return null;
        }

        Iterator<UIComponent> iterator = Iterators.filter(getChildren().iterator(), RENDERED_UITREE_NODE);
        if (iterator.hasNext()) {
            return (AbstractTreeNode) iterator.next();
        }

        return null;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        if (event instanceof TreeToggleEvent) {
            TreeToggleEvent toggleEvent = (TreeToggleEvent) event;
            boolean newExpandedValue = toggleEvent.isExpanded();

            FacesContext context = getFacesContext();
            ValueExpression expression = getValueExpression(PropertyKeys.expanded.toString());
            if (expression != null) {
                ELContext elContext = context.getELContext();
                Exception caught = null;
                FacesMessage message = null;
                try {
                    expression.setValue(elContext, newExpandedValue);
                } catch (ELException e) {
                    caught = e;
                    String messageStr = e.getMessage();
                    Throwable result = e.getCause();
                    while (null != result &&
                        result.getClass().isAssignableFrom(ELException.class)) {
                        messageStr = result.getMessage();
                        result = result.getCause();
                    }
                    if (null == messageStr) {
                        MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);
                        message =
                            messageFactory.createMessage(context, FacesMessages.UIINPUT_UPDATE,
                                MessageUtil.getLabel(context, this));
                    } else {
                        message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            messageStr,
                            messageStr);
                    }
                } catch (Exception e) {
                    caught = e;
                    MessageFactory messageFactory = ServiceTracker.getService(MessageFactory.class);
                    message =
                        messageFactory.createMessage(context, FacesMessages.UIINPUT_UPDATE,
                            MessageUtil.getLabel(context, this));
                }
                if (caught != null) {
                    assert(message != null);
                    UpdateModelException toQueue = new UpdateModelException(message, caught);
                    ExceptionQueuedEventContext eventContext =
                        new ExceptionQueuedEventContext(context,
                            toQueue,
                            this,
                            PhaseId.UPDATE_MODEL_VALUES);
                    context.getApplication().publishEvent(context,
                        ExceptionQueuedEvent.class,
                        eventContext);
                }
            } else {
                setExpanded(newExpandedValue);
            }
        }
    }
    
    @Override
    public void decode(FacesContext context) {
        try {
            decoderHelper = new TreeDecoderHelper(this);
            super.decode(context);
        } finally {
            decoderHelper = null;
        }
    }
    
    @Override
    protected Iterator<UIComponent> dataChildren() {
        if (decoderHelper != null) {
            return Iterators.concat(super.dataChildren(), Iterators.<UIComponent>singletonIterator(decoderHelper));
        } else {
            return super.dataChildren();
        }
    }
}
