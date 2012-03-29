package org.richfaces.taglib;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.view.facelets.*;
import java.util.Locale;

public class PageTagHandler extends ComponentHandler {
// ------------------------------ FIELDS ------------------------------

    private TagAttribute contentType;
    private TagAttribute encoding;

    private TagAttribute locale;

// -------------------------- STATIC METHODS --------------------------

    public static Locale getLocale(FaceletContext ctx, TagAttribute attr)
            throws TagAttributeException {
        Object obj = attr.getObject(ctx);
        if (obj instanceof Locale) {
            return (Locale) obj;
        }
        if (obj instanceof String) {
            String s = (String) obj;
            if (s.length() == 2) {
                return new Locale(s);
            }
            if (s.length() == 5) {
                return new Locale(s.substring(0, 2), s.substring(3, 5)
                        .toUpperCase());
            }
            if (s.length() >= 7) {
                return new Locale(s.substring(0, 2), s.substring(3, 5).toUpperCase(), s.substring(6, s.length()));
            }
            throw new TagAttributeException(attr, "Invalid Locale Specified: " + s);
        } else {
            throw new TagAttributeException(attr, "Attribute did not evaluate to a String or Locale: " + obj);
        }
    }

    private static UIViewRoot getViewRoot(FaceletContext ctx, UIComponent parent) {
        UIComponent c = parent;
        do {
            if (c instanceof UIViewRoot) {
                return (UIViewRoot) c;
            } else {
                c = c.getParent();
            }
        } while (c != null);
        return ctx.getFacesContext().getViewRoot();
    }

// --------------------------- CONSTRUCTORS ---------------------------

    public PageTagHandler(ComponentConfig config) {
        super(config);
        this.locale = this.getAttribute("locale");
        this.contentType = this.getAttribute("contentType");
        this.encoding = this.getAttribute("encoding");
    }

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void onComponentPopulated(FaceletContext ctx, UIComponent c,
                                     UIComponent parent) {
        super.onComponentPopulated(ctx, c, parent);

        UIViewRoot root = getViewRoot(ctx, parent);
        if (root != null) {
            if (this.locale != null) {
                root.setLocale(getLocale(ctx, this.locale));
            }
            if (this.contentType != null) {
                String v = this.contentType.getValue(ctx);
                ctx.getFacesContext().getExternalContext().getRequestMap().put("facelets.ContentType", v);
                root.getAttributes().put("contentType", v);
            }
            if (this.encoding != null) {
                String v = this.encoding.getValue(ctx);
                ctx.getFacesContext().getExternalContext().getRequestMap().put("facelets.Encoding", v);
                root.getAttributes().put("encoding", v);
            }
        }
    }
}
