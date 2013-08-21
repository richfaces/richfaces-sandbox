package org.richfaces.renderkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractPage;
import org.richfaces.component.LayoutPosition;
import org.richfaces.skin.SkinFactory;
import org.richfaces.skin.Theme;
import org.richfaces.ui.common.HtmlConstants;

@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "page.ecss", target = "head")
})
public abstract class AbstractPageRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.PageRenderer";

    private static final Map<String, String[]> doctypes;

// -------------------------- STATIC METHODS --------------------------

    static {
        // Fill doctype, content-type and namespace map for different formats.
        doctypes = new HashMap<String, String[]>();
        doctypes
                .put(
                        "html-transitional",
                        new String[]{
                                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n",
                                "text/html", null});
        doctypes.put("html", new String[]{
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"\n"
                        + "\"http://www.w3.org/TR/html4/strict.dtd\">\n",
                "text/html", null});
        doctypes.put("html-frameset", new String[]{
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\"\n"
                        + "\"http://www.w3.org/TR/html4/frameset.dtd\">\n",
                "text/html", null});
        doctypes
                .put(
                        "xhtml",
                        new String[]{
                                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n"
                                        + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n",
                                "application/xhtml+xml",
                                "http://www.w3.org/1999/xhtml"});
        doctypes
                .put(
                        "xhtml-transitional",
                        new String[]{
                                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
                                        + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n",
                                "application/xhtml+xml",
                                "http://www.w3.org/1999/xhtml"});
        doctypes
                .put(
                        "xhtml-frameset",
                        new String[]{
                                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Frameset//EN\"\n"
                                        + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd\">\n",
                                "application/xhtml+xml",
                                "http://www.w3.org/1999/xhtml"});
        doctypes.put("html-3.2", new String[]{
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n",
                "text/html", null});
    }

// -------------------------- OTHER METHODS --------------------------

    public void encodeBodyResources(FacesContext context) throws IOException {
        for (UIComponent child : context.getViewRoot().getComponentResources(context, "body")) {
            child.encodeAll(context);
        }
    }

    public void encodeHeadResources(FacesContext context) throws IOException {
        for (UIComponent child : context.getViewRoot().getComponentResources(context, "head")) {
            child.encodeAll(context);
        }
    }

    public Theme getTheme(FacesContext context, UIComponent component) {
        Theme theme = null;
        String themeName = ((AbstractPage) component).getTheme();
        if (null != themeName && themeName.length() > 0) {
            theme = SkinFactory.getInstance().getTheme(context, themeName);
        }
        return theme;
    }

    public boolean hasFacet(UIComponent component, String facet) {
        return null != component.getFacet(facet);
    }

    public boolean hasTitle(FacesContext context, UIComponent component) {
        AbstractPage page = (AbstractPage) component;
        String pageTitle = page.getPageTitle();
        return pageTitle != null && !pageTitle.trim().equals("");
    }

    public void insertFacet(String facetName, FacesContext context, UIComponent component) throws IOException {
        UIComponent indexChildren = component.getFacet(facetName);
        if (null != indexChildren && indexChildren.isRendered()) {
            renderChild(context, indexChildren);
        }
    }

    public void pageStyle(FacesContext context, UIComponent component)
            throws IOException {
        // Write body class.
        AbstractPage page = (AbstractPage) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("style", component);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.CSS_TYPE, null);
        // Calculate page width
        Integer width = page.getWidth();
        if (null != width && width > 0) {
            float nonIeWidth = (width.floatValue() / 13.0f);
            float ieWidth = (width.floatValue() / 13.333f);
            StringBuilder format = new StringBuilder(
                    ".rf-pg{margin:auto;text-align:left;");
            format.append("width:").append(nonIeWidth).append("em;");
            format.append("*width:").append(ieWidth).append("em;\n");
            format.append("}\n");
            writer.write(format.toString());
        } else {
            writer.write(".rf-pg{margin:auto 10px;width:auto;}\n");
        }
        // Calculate sidebar width
        if (component.getFacet("sidebar") != null) {
            LayoutPosition sidebarPosition = page.getSidebarPosition();
            if (LayoutPosition.right.equals(sidebarPosition)) {
                writer
                        .write(".rf-pg-m{float:left;margin-right:-30em;}\n");
            } else {
                writer
                        .write(".rf-pg-m{float:right;margin-left:-30em;}\n");
                sidebarPosition = LayoutPosition.left;
            }
            Integer sidebarWidth = page.getSidebarWidth();
            if (null != sidebarWidth && sidebarWidth > 0) {
                float nonIeWidth = (sidebarWidth.floatValue() / 13.0f);
                float ieWidth = (sidebarWidth.floatValue() / 13.333f);
                StringBuilder format = new StringBuilder(
                        ".rf-pg-sb{float:");
                format.append(sidebarPosition).append(";");
                format.append("width:").append(nonIeWidth).append("em;");
                format.append("*width:").append(ieWidth).append("em;}\n");
                format.append(".rf-pg-bd{margin-");
                format.append(sidebarPosition).append(":").append(nonIeWidth + 1.0f)
                        .append("em;");
                format.append("*margin").append(sidebarPosition).append(":").append(
                        ieWidth + .975f).append("em;}\n");
                writer.write(format.toString());
            }
        } // Cleanup
        writer.write(".rf-pg-bd{float:none;width:auto;}\n");
        writer.endElement("style");
    }

    public String prolog(FacesContext context, UIComponent component)
            throws IOException {
        ResponseWriter out = context.getResponseWriter();
        AbstractPage page = (AbstractPage) component;
        String format = page.getMarkupType();
        String contentType = page.getContentType();
        String namespace = page.getNamespace();
        // String characterEncoding = out.getCharacterEncoding();
        String[] docType = null;
        if (null != format) {
            docType = doctypes.get(format);
        } else {
            contentType = out.getContentType();
            for (String[] types : doctypes.values()) {
                if (types[1].equals(contentType)) {
                    docType = types;
                    break;
                }
            }
        }
        if (null != docType) {
            if (null == contentType) {
                contentType = docType[1];
            }
            // https://jira.jboss.org/jira/browse/RF-7367
            if (null == namespace) {
                namespace = docType[2];
            }
            out.write(docType[0]);
        }
        if (null != contentType) {
            // response.setContentType(contentType /*+ ";charset=" +
            // characterEncoding*/);
        }
        return namespace;
    }

    public void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
        if (!child.isRendered()) {
            return;
        }
        child.encodeBegin(facesContext);
        if (child.getRendersChildren()) {
            child.encodeChildren(facesContext);
        } else {
            renderChildren(facesContext, child);
        }
        child.encodeEnd(facesContext);
    }

    public void themeScript(FacesContext context, UIComponent component) throws IOException {
        Theme theme = getTheme(context, component);
        if (null != theme) {
            String script = theme.getScript();
            if (null != script) {
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement(HtmlConstants.SCRIPT_ELEM, component);
                writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.JAVASCRIPT_TYPE, null);
                script = context.getApplication().getViewHandler().getResourceURL(context, script);
                script = context.getExternalContext().encodeResourceURL(script);
                writer.writeAttribute(HtmlConstants.SRC_ATTRIBUTE, script, null);
                writer.endElement(HtmlConstants.SCRIPT_ELEM);
            }
        }
    }

    public void themeStyle(FacesContext context, UIComponent component) throws IOException {
        Theme theme = getTheme(context, component);
        if (null != theme) {
            String style = theme.getStyle();
            if (null != style) {
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement(HtmlConstants.LINK_ELEMENT, component);
                writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.CSS_TYPE, null);
                writer.writeAttribute(HtmlConstants.REL_ATTR, HtmlConstants.REL_STYLESHEET, null);
                writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, "component", null);
                String library = null, resourceName = null;
                if (style.contains(":")) {
                    StringTokenizer stringTokenizer = new StringTokenizer(style, ":");
                    library = stringTokenizer.nextToken();
                    resourceName = stringTokenizer.nextToken();
                } else {
                    resourceName = style;
                }
                Resource resource = context.getApplication().getResourceHandler().createResource(resourceName, library);
                style = context.getExternalContext().encodeResourceURL(resource.getRequestPath());
                writer.writeAttribute(HtmlConstants.HREF_ATTR, style, null);
                writer.endElement(HtmlConstants.LINK_ELEMENT);
            }
        }
    }
}
