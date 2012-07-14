package org.richfaces.bootstrap.less;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;

public class LessResponseWriter extends ResponseWriterWrapper {

    private ResponseWriter wrapped;
    private boolean insideLink = false;
    private LessStylesheetDetector detector;

    public LessResponseWriter(ResponseWriter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResponseWriter cloneWithWriter(Writer writer) {
        return new LessResponseWriter(super.cloneWithWriter(writer));
    }

    @Override
    public void startElement(String name, UIComponent component) throws IOException {
        super.startElement(name, component);

        if ("link".equals(name)) {
            insideLink = true;
            detector = new LessStylesheetDetector();
        } else {
            insideLink = false;
        }
    }

    private void writeAttributesFinally(ResponseWriter writer) throws IOException {
        if (insideLink) {
            detector.writeAttributes(writer);
        }
    }

    @Override
    public void endElement(String name) throws IOException {
        writeAttributesFinally(wrapped);

        super.endElement(name);

        if ("link".equals(name)) {
            insideLink = false;
        }
    }

    /**
     * An override which checks if an attribute of <code>type="text"</code> is been written by an {@link UIInput} component and
     * if so then check if the <code>type</code> attribute isn't been explicitly set by the developer and if so then write it.
     * 
     * @throws IllegalArgumentException When the <code>type</code> attribute is not supported.
     */
    @Override
    public void writeAttribute(String name, Object value, String property) throws IOException {
        if (insideLink) {
            detector.putAttribute(name, value, property);
        } else {
            super.writeAttribute(name, value, property);
        }
    }

    @Override
    public void writeURIAttribute(String name, Object value, String property) throws IOException {
        if (insideLink) {
            detector.putURIAttribute(name, value, property);
        } else {
            super.writeURIAttribute(name, value, property);
        }
    }

    @Override
    public ResponseWriter getWrapped() {
        return wrapped;
    }

    @Override
    public void writeText(char[] text, int off, int len) throws IOException {
        writeAttributesFinally(wrapped);
        super.writeText(text, off, len);
    }

    @Override
    public void writeText(Object text, String property) throws IOException {
        writeAttributesFinally(wrapped);
        super.writeText(text, property);
    }

    @Override
    public void writeText(Object text, UIComponent component, String property) throws IOException {
        writeAttributesFinally(wrapped);
        super.writeText(text, component, property);
    }

    @Override
    public void writeComment(Object comment) throws IOException {
        writeAttributesFinally(wrapped);
        super.writeComment(comment);
    }

    private static class LessStylesheetDetector {

        private Map<String, ValuePropertyPair> attributes = new LinkedHashMap<String, ValuePropertyPair>();

        private void putAttribute(String name, Object value, String property) {
            ValuePropertyPair pair = new ValuePropertyPair(property, value, false);
            attributes.put(name, pair);
        }

        private void putURIAttribute(String name, Object value, String property) {
            ValuePropertyPair pair = new ValuePropertyPair(property, value, true);
            attributes.put(name, pair);
        }

        private boolean isLessStylesheet() {
            ValuePropertyPair typePair = attributes.get("type");
            ValuePropertyPair relPair = attributes.get("rel");
            ValuePropertyPair hrefPair = attributes.get("href");

            if (typePair == null || relPair == null || hrefPair == null) {
                return false;
            }

            return typePair.valueMatches("text/css") && relPair.valueMatches("stylesheet") && hrefPair.valueContains(".less");
        }

        private void writeAttributes(ResponseWriter writer) throws IOException {
            boolean isStylesheet = isLessStylesheet();
            
            for (Entry<String, ValuePropertyPair> attribute : attributes.entrySet()) {
                String name = attribute.getKey();
                ValuePropertyPair pair = attribute.getValue();
                Object value = pair.getValue();
                String property = pair.getProperty();
                boolean isUri = pair.isUri();
                if ("rel".equals(name) && !isUri && isStylesheet) {
                    writer.writeAttribute(name, "stylesheet/less", property);
                } else {
                    if (isUri) {
                        writer.writeURIAttribute(name, value, property);
                    } else {
                        writer.writeAttribute(name, value, property);
                    }
                }
            }
        }

        private class ValuePropertyPair {
            private String property;
            private Object value;
            private boolean isUri;

            public ValuePropertyPair(String property, Object value, boolean isUri) {
                this.property = property;
                this.value = value;
                this.isUri = isUri;
            }

            public String getProperty() {
                return property;
            }

            public Object getValue() {
                return value;
            }

            public boolean isUri() {
                return isUri;
            }

            public boolean valueMatches(String expected) {
                if (value == null) {
                    return false;
                }

                return value.toString().equals(expected);
            }

            public boolean valueContains(String expected) {
                if (value == null) {
                    return false;
                }

                return value.toString().contains(expected);
            }
        }
    }
}