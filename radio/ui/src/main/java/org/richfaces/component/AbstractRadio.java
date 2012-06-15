package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;
import org.richfaces.cdk.annotations.TagType;
import org.richfaces.renderkit.html.SingleRadioRenderer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

@JsfComponent(tag = @Tag(name = "radio", type = TagType.Facelets),
    renderer = @JsfRenderer(family = AbstractRadio.COMPONENT_FAMILY, type = SingleRadioRenderer.RENDERER_TYPE))
public abstract class AbstractRadio extends UIOutput implements ClientBehaviorHolder {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Radio";

    public static final String COMPONENT_TYPE = "org.richfaces.Radio";

// -------------------------- STATIC METHODS --------------------------

    /**
     * <p>Return a List of {@link javax.faces.model.SelectItem}
     * instances representing the available options for this component,
     * assembled from the set of {@link javax.faces.component.UISelectItem}
     * and/or {@link javax.faces.component.UISelectItems} components that are
     * direct children of this component.  If there are no such children, an
     * empty <code>Iterator</code> is returned.</p>
     *
     * @param context   The {@link javax.faces.context.FacesContext} for the current request.
     *                  If null, the UISelectItems behavior will not work.
     * @param component the component
     * @return a List of the select items for the specified component
     * @throws IllegalArgumentException if <code>context</code>
     *                                  is <code>null</code>
     */
    public static List<SelectItem> getSelectItems(FacesContext context, UIComponent component) {
        if (context == null) {
            throw new IllegalArgumentException("Context param must not be null.");
        }

        ArrayList<SelectItem> list = new ArrayList<SelectItem>();
        final SelectItemsIterator iterator = new SelectItemsIterator(context, component);
        while (iterator.hasNext()) {
            final SelectItem next = iterator.next();
            list.add(new SelectItem(next.getValue(), next.getLabel(), next.getDescription(), next.isDisabled(), next.isEscape(), next.isNoSelectionOption()));
        }
        return (list);
    }

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract String getFor();

    @Attribute
    public abstract Integer getIndex();

    @Attribute(events = @EventName(value = "change", defaultEvent = true))
    public abstract String getOnchange();

    public SelectItem getSelectItem(FacesContext context, UIComponent targetComponent) {
        final List<SelectItem> list = getSelectItems(context, targetComponent);
        try {
            return list.get(getIndex());
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException(
                "Component ''" + getId() + "'' has wrong value of index attribute (" + getIndex() + "). Target component ''" + targetComponent.getId()
                    + "'' has only " + list.size() + " items.");
        }
    }

    @Attribute(defaultValue = "true")
    public abstract boolean isLabelVisible();
}
