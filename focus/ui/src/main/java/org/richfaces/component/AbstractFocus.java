package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;
import org.richfaces.renderkit.html.HtmlFocusRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@JsfComponent(tag = @Tag(name = "focus", type = TagType.Facelets),
    renderer = @JsfRenderer(family = AbstractFocus.COMPONENT_FAMILY, type = HtmlFocusRenderer.RENDERER_TYPE))
public abstract class AbstractFocus extends UIComponentBase {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Focus";

    public static final String COMPONENT_TYPE = "org.richfaces.Focus";

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

    public static final String FOCUS_MODIFIER_FACET_NAME = "focusModifier";

    public static final String TIMING_ON_LOAD = "onload";

    private static final Logger LOG = LogFactory.getLogger(AbstractFocus.class);

// -------------------------- STATIC METHODS --------------------------

    public static AbstractFocusModifier findModifier(UIComponent component) {
        if (component instanceof AbstractFocusModifier) {
            return (AbstractFocusModifier) component;
        }
        AbstractFocusModifier modifier = (AbstractFocusModifier) component.getFacet(AbstractFocus.FOCUS_MODIFIER_FACET_NAME);
        if (modifier == null) {
            for (UIComponent child : component.getChildren()) {
                modifier = findModifier(child);
                if (modifier != null) {
                    break;
                }
            }
        }
        return modifier;
    }

// -------------------------- OTHER METHODS --------------------------

    public int calculatePriority(UIComponent component) {
        final AbstractFocusModifier modifier = findModifier(component);
        if (modifier != null && modifier.getPriority() != null) {
            return modifier.getPriority();
        }
        UIComponent parentForm = component.getParent();
        while (parentForm != null && !(parentForm instanceof UIForm)) {
            parentForm = parentForm.getParent();
        }
        if (parentForm != null) {
            return getUIInputChildrenCount(parentForm, component.getId());
        } else {
            return DEFAULT_PRIORITY;
        }
    }

    @Attribute
    public abstract String getFor();

    @Attribute
    public abstract String getName();

    @Attribute
    public abstract Integer getPriority();

    @Attribute
    public abstract String getSuffix();

    @Attribute
    public abstract String getTargetClientId();

    public String getTargetComponentId(FacesContext context) {
        String aFor = getFor();

        if (aFor != null && !"".equals(aFor)) {
            return aFor;
        } else {
            if (!(getParent() instanceof UIInput)) {
                Set<String> allowedClientIds = new HashSet<String>();
                Iterator<String> clientIdsWithMessages = getFacesContext().getClientIdsWithMessages();
                while (clientIdsWithMessages.hasNext()) {
                    final String clientId = clientIdsWithMessages.next();
                    if (clientId != null) {
                        allowedClientIds.add(clientId);
                    }
                }
                final List<UIInput> inputs = new ArrayList<UIInput>();
                getInputs(getParentForm(this), allowedClientIds, inputs);
                UIInput inputWithLowestPriority = null;
                int lowestPriority = Integer.MAX_VALUE;
                for (UIInput input : inputs) {
                    final int priority = calculatePriority(input);
                    if (priority < lowestPriority) {
                        inputWithLowestPriority = input;
                        lowestPriority = priority;
                    }
                }
                return inputWithLowestPriority == null ? null : inputWithLowestPriority.getClientId(context);
            } else {
                return getParent().getClientId(context);
            }
        }
    }

    @Attribute
    public abstract String getTiming();

    private void getInputs(UIComponent parent, Set<String> allowedClientIds, List<UIInput> inputs) {
        FacesContext facesContext = getFacesContext();
        if (isCompositeComponent(parent)) {
            String parentId = parent.getId();
            parent = parent.getFacet(UIComponent.COMPOSITE_FACET_NAME);
            if (parent == null) {
                LOG.warn("Composite component " + parentId + " doesn't have facet " + UIComponent.COMPOSITE_FACET_NAME);
                return;
            }
        }
        for (UIComponent child : parent.getChildren()) {
            if (child instanceof UIInput && (allowedClientIds.size() == 0 || allowedClientIds.contains(child.getClientId(facesContext)))) {
                final AbstractFocusModifier modifier = findModifier(child);
                if (modifier == null || !modifier.isSkipped()) {
                    inputs.add((UIInput) child);
                }
            }
            getInputs(child, allowedClientIds, inputs);
        }
    }

    private UIForm getParentForm(UIComponent component) {
        UIComponent parent = component.getParent();
        if (parent == null) {
            return null;
        }
        if (parent instanceof UIForm) {
            return (UIForm) parent;
        } else {
            return getParentForm(parent);
        }
    }

    private int getUIInputChildrenCount(UIComponent component, String breakOnId) {
        final Holder<Integer> childrenCount = new Holder<Integer>();
        childrenCount.value = 0;
        getUIInputChildrenCount(component, breakOnId, childrenCount);
        return childrenCount.value;
    }

    private boolean getUIInputChildrenCount(UIComponent component, String breakOnId, Holder<Integer> childrenCount) {
        for (UIComponent child : component.getChildren()) {
            if (child.getId().equals(breakOnId)) {
                return true;
            }
            if (child instanceof UIInput) {
                final AbstractFocusModifier modifier = findModifier(child);
                if (modifier == null || !modifier.isSkipped()) {
                    childrenCount.value++;
                }
            } else {
                if (getUIInputChildrenCount(child, breakOnId, childrenCount)) {
                    return true;
                }
            }
        }
        return false;
    }

// -------------------------- INNER CLASSES --------------------------

    private class Holder<T> {
// ------------------------------ FIELDS ------------------------------

        public T value;
    }
}
