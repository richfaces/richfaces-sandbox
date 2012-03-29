/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.sandbox.component;

import com.google.common.base.Strings;
import org.ajax4jsf.model.DataVisitResult;
import org.ajax4jsf.model.DataVisitor;
import org.richfaces.application.FacesMessages;
import org.richfaces.application.MessageFactory;
import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.component.MetaComponentEncoder;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.component.SwitchType;
import org.richfaces.component.UIRepeat;
import org.richfaces.component.util.MessageUtil;
import org.richfaces.context.ExtendedVisitContext;
import org.richfaces.context.ExtendedVisitContextMode;
import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeListener;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.renderkit.MetaComponentRenderer;
import org.richfaces.renderkit.util.RendererUtils;

import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UpdateModelException;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PostValidateEvent;
import javax.faces.event.PreValidateEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>The &lt;rich:togglePanel&gt; component is used as a base for the other switchable components, the
 * &lt;rich:accordion&gt; component and the &lt;rich:tabPanel&gt; component. It provides an abstract switchable
 * component without any associated markup. As such, the &lt;rich:togglePanel&gt; component could be customized to
 * provide a switchable component when neither an accordion component or a tab panel component is appropriate.</p>
 *
 * @author akolonitsky
 */
@JsfComponent(tag = @Tag(type = TagType.Facelets, handler = "org.richfaces.sandbox.view.facelets.html.TogglePanelTagHandler"), renderer = @JsfRenderer(type = "org.richfaces.sandbox.TogglePanelRenderer"), attributes = {
        "core-props.xml", "events-mouse-props.xml", "i18n-props.xml" })
public abstract class AbstractTogglePanel extends UIRepeat implements AbstractDivPanel, ItemChangeSource, MetaComponentResolver, MetaComponentEncoder {
    public static final String ACTIVE_ITEM_META_COMPONENT = "activeItem";
    public static final String COMPONENT_TYPE = "org.richfaces.sandbox.TogglePanel";
    public static final String COMPONENT_FAMILY = "org.richfaces.sandbox.TogglePanel";
    public static final String META_NAME_FIRST = "@first";
    public static final String META_NAME_PREV = "@prev";
    public static final String META_NAME_NEXT = "@next";
    public static final String META_NAME_LAST = "@last";
    // TODO What is MessageId ?
    public static final String UPDATE_MESSAGE_ID = "javax.faces.component.UIInput.UPDATE";
    private String submittedActiveItem = null;

    private enum PropertyKeys {
        activeItem, localActiveItemSet, required, valid, immediate, switchType
    }

    protected AbstractTogglePanel() {
        setRendererType("org.richfaces.sandbox.TogglePanelRenderer");
        decodeVisitor = new ComponentVisitor() {

            @Override
            public void processComponent(FacesContext context, UIComponent c, Object argument)
            {
                if (isActiveItem(c) || getSwitchType() == SwitchType.client) {
                    c.processDecodes(context);
                }
            }
        };

        validateVisitor = new ComponentVisitor() {

            @Override
            public void processComponent(FacesContext context, UIComponent c, Object argument)
            {
                if (isActiveItem(c) || getSwitchType() == SwitchType.client) {
                    c.processValidators(context);
                }
            }
        };
        updateVisitor = new ComponentVisitor() {

            @Override
            public void processComponent(FacesContext context, UIComponent c, Object argument)
            {
                if (isActiveItem(c) || getSwitchType() == SwitchType.client) {
                    c.processUpdates(context);
                }
            }
        };
    }

    // -------------------------------------------------- Editable Value Holder

    public String getSubmittedActiveItem() {
        return this.submittedActiveItem;
    }

    public void resetValue() {
        this.setValue(null);
        this.setSubmittedActiveItem(null);
        this.setLocalActiveItemSet(false);
        this.setValid(true);
    }

    public void setSubmittedActiveItem(Object submittedValue) {
        this.submittedActiveItem = String.valueOf(submittedValue);
    }

    public boolean isLocalActiveItemSet() {
        return (Boolean) getStateHelper().eval(PropertyKeys.localActiveItemSet, false);
    }

    public void setLocalActiveItemSet(boolean localValueSet) {
        getStateHelper().put(PropertyKeys.localActiveItemSet, localValueSet);
    }

    public boolean isValid() {
        return (Boolean) getStateHelper().eval(PropertyKeys.valid, true);
    }

    public void setValid(boolean valid) {
        getStateHelper().put(PropertyKeys.valid, valid);
    }

    public boolean isRequired() {
        return (Boolean) getStateHelper().eval(PropertyKeys.required, false);
    }

    /**
     * <p>
     * Set the "required field" state for this component.
     * </p>
     *
     * @param required The new "required field" state
     */
    public void setRequired(boolean required) {
        getStateHelper().put(PropertyKeys.required, required);
    }

    /**
     * Flag indicating that this component's value must be converted and validated immediately (that is, during Apply Request
     * Values phase), rather than waiting until Process Validations phase.
     */
    @Attribute
    public boolean isImmediate() {
        return (Boolean) getStateHelper().eval(PropertyKeys.immediate, false);
    }

    public void setImmediate(boolean immediate) {
        getStateHelper().put(PropertyKeys.immediate, immediate);
    }

    // ----------------------------------------------------- UIComponent Methods

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        AbstractTogglePanelItemInterface item = null;
        String activeItem = getActiveItem();

        if (!Strings.isNullOrEmpty(activeItem)) {
            item = this.getItem(activeItem);
        }

        if (item == null || !((UIComponent) item).isRendered()) {
            if (getValue() != null) {
                try {
                    DataVisitor visitor = new AbstractTogglePanelItemVisitor(this, new AbstractTogglePanelItemVisitor.TabVisitorCallback() {
                        @Override
                        public DataVisitResult visit(AbstractTogglePanelItemInterface item)
                        {
                                setActiveItem(item.getName());
                                return DataVisitResult.STOP;
                        }
                    });
                    walk(context, visitor, null);
                } finally {
                    setRowKey(context, null);
                }
            } else {
                List<AbstractTogglePanelItemInterface> renderedItems = this.getRenderedItems();
                if (!renderedItems.isEmpty()) {
                    setActiveItem(renderedItems.get(0).getName());
                }
            }
        }

        super.encodeBegin(context);
    }

    /**
     * <p>
     * Specialized decode behavior on top of that provided by the superclass. In addition to the standard
     * <code>processDecodes</code> behavior inherited from {@link javax.faces.component.UIComponentBase}, calls
     * <code>processValue()</code> if the the <code>immediate</code> property is true; if the component is invalid afterwards or
     * a <code>RuntimeException</code> is thrown, calls {@link FacesContext#renderResponse}.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processDecodes(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all facets and children of this component
        Iterator<UIComponent> kids = getFacetsAndChildren();
        String activeItem = getActiveItemValue();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (isActiveItem(kid, activeItem) || this.getSwitchType() == SwitchType.client) {
                kid.processDecodes(context);
            }
        }
        super.processDecodes(context);
        // Process this component itself
        try {
            decode(context);
        } catch (RuntimeException e) {
            context.renderResponse();
            throw e;
        } finally {
            popComponentFromEL(context);
        }

        ItemChangeEvent event = createItemChangeEvent(context);
        if (event != null) {
            event.queue();
        }
    }

    /**
     * <p>
     * In addition to the standard <code>processValidators</code> behavior inherited from
     * {@link javax.faces.component.UIComponentBase}, calls <code>processValue()</code> if the <code>immediate</code> property
     * is false (which is the default); if the component is invalid afterwards, calls {@link FacesContext#renderResponse}. If a
     * <code>RuntimeException</code> is thrown during validation processing, calls {@link FacesContext#renderResponse} and
     * re-throw the exception.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processValidators(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        Application app = context.getApplication();
        app.publishEvent(context, PreValidateEvent.class, this);

        // Process all the facets and children of this component
        Iterator<UIComponent> kids = getFacetsAndChildren();
        String activeItem = getActiveItemValue();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (isActiveItem(kid, activeItem) || this.getSwitchType() == SwitchType.client) {
                kid.processValidators(context);
            }
        }
//        TODO process validators
        super.processValidators(context);
        app.publishEvent(context, PostValidateEvent.class, this);
        popComponentFromEL(context);
    }

    /**
     * <p>
     * In addition to the standard <code>processUpdates</code> behavior inherited from
     * {@link javax.faces.component.UIComponentBase}, calls <code>updateModel()</code>. If the component is invalid afterwards,
     * calls {@link FacesContext#renderResponse}. If a <code>RuntimeException</code> is thrown during update processing, calls
     * {@link FacesContext#renderResponse} and re-throw the exception.
     * </p>
     *
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void processUpdates(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Skip processing if our rendered flag is false
        if (!isRendered()) {
            return;
        }

        pushComponentToEL(context, null);

        // Process all facets and children of this component
        Iterator<UIComponent> kids = getFacetsAndChildren();
        String activeItem = getActiveItemValue();
        while (kids.hasNext()) {
            UIComponent kid = kids.next();
            if (isActiveItem(kid, activeItem) || this.getSwitchType() == SwitchType.client) {
                kid.processUpdates(context);
            }
        }
//        TODO processUpdates
           super.processUpdates(context);
        popComponentFromEL(context);

        if (!isValid()) {
            context.renderResponse();
        }
    }

    /**
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void decode(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        // Force validity back to "true"
        setValid(true);
        super.decode(context);
    }

    public void updateModel(FacesContext context) {

        if (context == null) {
            throw new NullPointerException();
        }

        if (!isValid() || !isLocalActiveItemSet()) {
            return;
        }

        ValueExpression ve = getValueExpression("activeItem");
        if (ve == null) {
            return;
        }

        Throwable caught = null;
        FacesMessage message = null;
        try {
            ve.setValue(context.getELContext(), getLocalValue());
            setActiveItem(null);
            setLocalActiveItemSet(false);
        } catch (ELException e) {
            caught = e;
            String messageStr = e.getMessage();
            Throwable result = e.getCause();
            while (null != result && result.getClass().isAssignableFrom(ELException.class)) {
                messageStr = result.getMessage();
                result = result.getCause();
            }

            if (messageStr == null) {
                message = ServiceTracker.getService(MessageFactory.class).createMessage(context, FacesMessage.SEVERITY_ERROR,
                        FacesMessages.UIINPUT_UPDATE, MessageUtil.getLabel(context, this));
            } else {
                message = new FacesMessage(FacesMessage.SEVERITY_ERROR, messageStr, messageStr);
            }
            setValid(false);
        } catch (Exception e) {
            caught = e;
            // message = MessageFactory.getMessage(context, UPDATE_MESSAGE_ID,
            // MessageFactory.getHeader(context, this));
            setValid(false);
        }

        if (caught != null) {
            assert message != null;

            @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
            UpdateModelException toQueue = new UpdateModelException(message, caught);
            ExceptionQueuedEventContext eventContext = new ExceptionQueuedEventContext(context, toQueue, this,
                    PhaseId.UPDATE_MODEL_VALUES);
            context.getApplication().publishEvent(context, ExceptionQueuedEvent.class, eventContext);
        }
    }

    @Attribute
    public abstract Object getLocalValue();

    private ItemChangeEvent createItemChangeEvent(FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }

        // Submitted value == null means "the component was not submitted at all".
        String activeItem = getSubmittedActiveItem();
        if (activeItem == null) {
            return null;
        }

        String previous = getActiveItem();
        if (previous == null || !previous.equalsIgnoreCase(activeItem)) {
            UIComponent prevComp = null;
            UIComponent actvComp = null;

            if (previous != null) {
                prevComp = (UIComponent) getItem(previous);
            }
            if (activeItem != null) {
                actvComp = (UIComponent) getItem(activeItem);
            }

            return new ItemChangeEvent(this, previous, prevComp, activeItem, actvComp);
        }
        return null;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        if ((event instanceof ItemChangeEvent) && (event.getComponent() == this)) {
            setEventPhase((ItemChangeEvent) event);
        }
        super.queueEvent(event);
    }

    protected void setEventPhase(ItemChangeEvent event) {
        if (isImmediate()
                || (event.getNewItem() != null && RendererUtils.getInstance().isBooleanAttribute(event.getNewItem(),
                        "immediate"))) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
        }
    }

    protected void setEventPhase(FacesEvent event) {
        if (isImmediate()) {
            event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (event instanceof ItemChangeEvent) {
            setActiveItem(((ItemChangeEvent) event).getNewItemName());
            setSubmittedActiveItem(null);
            if (event.getPhaseId() == PhaseId.UPDATE_MODEL_VALUES) {
                try {
                    updateModel(context);
                } catch (RuntimeException e) {
                    context.renderResponse();
                    throw e;
                }
            } else {
                context.renderResponse();
            }
        }
        super.broadcast(event);
    }

    // -------------------------------------------------- Panel Items Managing

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    private String getActiveItemValue() {
        String value = getActiveItem();
        if (value == null) {
            value = getSubmittedActiveItem();
        }
        return value;
    }

    protected boolean isActiveItem(UIComponent kid) {
        return isActiveItem(kid, getActiveItemValue());
    }

    protected boolean isActiveItem(UIComponent kid, String value) {
        if (kid == null || value == null) {
            return false;
        }

        return getChildName(kid).equals(value);
    }

    private static String getChildName(UIComponent item) {
        if (item == null) {
            return null;
        }

        if (!(item instanceof AbstractTogglePanelItemInterface)) {
            throw new IllegalArgumentException();
        }

        return ((AbstractTogglePanelItemInterface) item).getName();
    }

    public AbstractTogglePanelItemInterface getItemByIndex(final int index) {
//        TODO rewrite this method
        if(getValue() != null) {
//            TODO find out smarter way to calculate number of tabs
            final Map<Integer,Object> index2rowKey = new HashMap<Integer,Object>();
            final int[] sizeHolder = new int[1];
            DataVisitor visitor = new AbstractTogglePanelItemVisitor(this, new AbstractTogglePanelItemVisitor.TabVisitorCallback() {
                @Override
                public DataVisitResult visit(AbstractTogglePanelItemInterface item)
                {
                    index2rowKey.put(sizeHolder[0],getRowKey());
                    sizeHolder[0]++;
                    return DataVisitResult.CONTINUE;
                }
            });
            walk(FacesContext.getCurrentInstance(), visitor, null);
            if (index < 0 || index >= sizeHolder[0]) {
                return null;
            }
            int size = getRenderedItems().size();
            setRowKey(index2rowKey.get((size + index) % size));
            List<AbstractTogglePanelItemInterface> children = getRenderedItems();
            int innerIndex = index % size;
            if (isCycledSwitching()) {
                size = children.size();
                return children.get((size + innerIndex) % size);
            } else {
                return children.get(innerIndex);
            }
        } else {
            List<AbstractTogglePanelItemInterface> children = getRenderedItems();
            if (index < 0 || index >= children.size()) {
                return null;
            } else if (isCycledSwitching()) {
                int size = getRenderedItems().size();
                return children.get((size + index) % size);
            } else {
                return children.get(index);
            }
        }
    }

    public List<AbstractTogglePanelItemInterface> getRenderedItems() {
        return getItems(false);
    }

    public List<AbstractTogglePanelItemInterface> getItems(boolean isRendered) {
        List<AbstractTogglePanelItemInterface> res = new ArrayList<AbstractTogglePanelItemInterface>(getChildCount());
        for (UIComponent child : getChildren()) {
            if ((isRendered || child.isRendered()) && child instanceof AbstractTogglePanelItemInterface) {
                res.add((AbstractTogglePanelItemInterface) child);
            }
        }

        return res;
    }

    public AbstractTogglePanelItemInterface getItem(String name) {
        if (META_NAME_FIRST.equals(name)) {
            return getFirstItem();
        } else if (META_NAME_PREV.equals(name)) {
            return getPrevItem();
        } else if (META_NAME_NEXT.equals(name)) {
            return getNextItem();
        } else if (META_NAME_LAST.equals(name)) {
            return getLastItem();
        } else {
            return getItemByIndex(getChildIndex(name));
        }
    }

    public AbstractTogglePanelItemInterface getFirstItem() {
        return getItemByIndex(0);
    }

    public AbstractTogglePanelItemInterface getPrevItem() {
        return getPrevItem(getActiveItem());
    }

    public AbstractTogglePanelItemInterface getPrevItem(String name) {
        return getItemByIndex(getChildIndex(name) - 1);
    }

    public AbstractTogglePanelItemInterface getNextItem() {
        return getNextItem(getActiveItem());
    }

    public AbstractTogglePanelItemInterface getNextItem(String name) {
        return getItemByIndex(getChildIndex(name) + 1);
    }

    public AbstractTogglePanelItemInterface getLastItem() {
        return getItemByIndex(getRenderedItems().size() - 1);
    }

    public int getChildIndex(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required parameter.");
        }
//        TODO rewrite this
        if(getValue()!=null) {
            final int[] indexHodler = new int[] {Integer.MIN_VALUE};
            final int[] iHodler = new int[1];
            DataVisitor visitor = new AbstractTogglePanelItemVisitor(this, new AbstractTogglePanelItemVisitor.TabVisitorCallback() {
                @Override
                public DataVisitResult visit(AbstractTogglePanelItemInterface item)
                {
                        if (name.equals(item.getName())) {
                            indexHodler[0] = iHodler[0];
                            return DataVisitResult.STOP;
                        }
                    iHodler[0]++;
                    return DataVisitResult.CONTINUE;
                }
            });
            walk(FacesContext.getCurrentInstance(), visitor, null);
            return indexHodler[0];
        } else {
            List<AbstractTogglePanelItemInterface> items = getRenderedItems();
            for (int ind = 0; ind < items.size(); ind++) {
                if (name.equals(items.get(ind).getName())) {
                    return ind;
                }
            }
        }

        return Integer.MIN_VALUE;
    }

    // ------------------------------------------------

   public void setSubmittedActiveItem(String submittedActiveItem) {
        this.submittedActiveItem = submittedActiveItem;
    }

    // ------------------------------------------------ Properties



    /**
     * Holds the active panel name. This name is a reference to the name identifier of the active child
     * &lt;rich:togglePanelItem&gt; component.
     */
    @Attribute(generate = false)
    public  String getActiveItem() {
        return (String) getStateHelper().eval(PropertyKeys.activeItem, null);
    }

    public void setActiveItem(String activeItem) {
        getStateHelper().put(PropertyKeys.activeItem, activeItem);
        setLocalActiveItemSet(true);
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        if ("activeItem".equals(name)) {
            super.setValueExpression("value", binding);
        } else {
            super.setValueExpression(name, binding);
        }
    }

    /**
     * The switch mode when a panel is activated. One of: "client", "server", "ajax". Default: "ajax"
     */
    @Attribute(generate = false)
    public SwitchType getSwitchType() {
        SwitchType switchType = (SwitchType) getStateHelper().eval(PropertyKeys.switchType);
        if (switchType == null) {
            switchType = SwitchType.DEFAULT;
        }
        return switchType;
    }

    public void setSwitchType(SwitchType switchType) {
        getStateHelper().put(PropertyKeys.switchType, switchType);
    }

    @Attribute(hidden = true)
    public abstract boolean isLimitRender();

    /**
     * Applicable when cycling through the tabs. If "true", then when the last tab is active, cycling to next will activate the
     * first tab, if "false", cycling to next will have not effect. The inverse applies for the first tab, and cycling to
     * previous. Whether to Default: false
     */
    @Attribute
    public abstract boolean isCycledSwitching();

    @Attribute(hidden = true)
    public abstract Object getData();

    @Attribute(hidden = true)
    public abstract String getStatus();

    @Attribute(hidden = true)
    public abstract Object getExecute();

    @Attribute(hidden = true)
    public abstract Object getRender();

    /**
     * Occurs on the server side when an item is changed through Ajax using the server mode
     */
    @Attribute
    public abstract MethodExpression getItemChangeListener();

    /**
     * The client-side script method to be called after the item is changed.
     */
    @Attribute(events = @EventName("itemchange"))
    public abstract String getOnitemchange();

    /**
     * The client-side script method to be called before the item is changed.
     */
    @Attribute(events = @EventName("beforeitemchange"))
    public abstract String getOnbeforeitemchange();

    // ------------------------------------------------ Event Processing Methods

    public void addItemChangeListener(ItemChangeListener listener) {
        addFacesListener(listener);
    }

    public ItemChangeListener[] getItemChangeListeners() {
        return (ItemChangeListener[]) getFacesListeners(ItemChangeListener.class);
    }

    public void removeItemChangeListener(ItemChangeListener listener) {
        removeFacesListener(listener);
    }

    public String resolveClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        if (ACTIVE_ITEM_META_COMPONENT.equals(metaComponentId)) {
            return getClientId(facesContext) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + metaComponentId;
        }
        return null;
    }

    public String substituteUnresolvedClientId(FacesContext facesContext, UIComponent contextComponent, String metaComponentId) {
        return null;
    }

    public void encodeMetaComponent(FacesContext context, String metaComponentId) throws IOException {
        ((MetaComponentRenderer) getRenderer(context)).encodeMetaComponent(context, this, metaComponentId);
    }

    @Override
    public boolean visitTree(VisitContext context, VisitCallback callback) {
        if (!isVisitable(context)) {
            return false;
        }

        FacesContext facesContext = context.getFacesContext();
        pushComponentToEL(facesContext, null);

        try {
            VisitResult result = context.invokeVisitCallback(this, callback);

            if (result == VisitResult.COMPLETE) {
                return true;
            }

            if (result == VisitResult.ACCEPT) {
                if (context instanceof ExtendedVisitContext) {
                    ExtendedVisitContext extendedVisitContext = (ExtendedVisitContext) context;
                    if (extendedVisitContext.getVisitMode() == ExtendedVisitContextMode.RENDER) {

                        result = extendedVisitContext.invokeMetaComponentVisitCallback(this, callback,
                                ACTIVE_ITEM_META_COMPONENT);
                        if (result == VisitResult.COMPLETE) {
                            return true;
                        }
                    }
                }
            }

            if (result == VisitResult.ACCEPT) {
                if (getValue() != null) {
                    return super.visitTree(context, callback);
                } else {
                    Iterator<UIComponent> kids = this.getFacetsAndChildren();

                    while (kids.hasNext()) {
                        boolean done = kids.next().visitTree(context, callback);

                        if (done) {
                            return true;
                        }
                    }
                }
            }
        } finally {
            popComponentFromEL(facesContext);
        }

        return false;
    }
}
