package org.richfaces.renderkit.html.scripts;

import org.ajax4jsf.renderkit.RendererUtils;
import org.richfaces.resource.AbstractCacheableResource;
import org.richfaces.resource.DynamicResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@DynamicResource
public class ScheduleMessages extends AbstractCacheableResource {

    public static final String BUNDLE_NAME = "org.richfaces.component.UIScheduleMessages";
    private static final String MESSAGE_KEY_BASE = "org.richfaces.component.UISchedule.";
    private static final Logger LOG = LoggerFactory.getLogger(ScheduleMessages.class);

    public ScheduleMessages() {
        setContentType(RendererUtils.HTML.JAVASCRIPT_TYPE);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        ClassLoader loader = getClassLoader();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        StringBuilder out = new StringBuilder();
        out.append("RichFaces.Schedule.prototype.messages=jQuery.extend(RichFaces.Schedule.prototype.messages,{");
        Iterator<Locale> supportedLocales = application.getSupportedLocales();
        while (supportedLocales.hasNext()) {
            Locale locale = supportedLocales.next();
            ResourceBundle applicationBundle = ResourceBundle.getBundle(application.getMessageBundle(), locale, loader);
            ResourceBundle stockBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale, loader);
            String[] months = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST",
                "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
            String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
            out.append("'").append(locale.toString()).append("':{");
            out.append("allDayText:'").append(escape(getMessageFromBundle(MESSAGE_KEY_BASE + "allDay",
                applicationBundle, stockBundle))).append("',");
            appendArray(out, applicationBundle, stockBundle, "monthNames", "monthNames", months);
            out.append(",");
            appendArray(out, applicationBundle, stockBundle, "monthNamesShort", "monthNamesShort", months);
            out.append(",");
            appendArray(out, applicationBundle, stockBundle, "dayNames", "dayNames", days);
            out.append(",");
            appendArray(out, applicationBundle, stockBundle, "dayNamesShort", "dayNamesShort", days);
            out.append(",");
            appendMap(out, applicationBundle, stockBundle, "buttonText", "buttonTexts", new String[]{"prev", "next",
                "prevYear", "nextYear", "today", "month", "day", "week"});
            out.append("},");
        }
        out.delete(out.length() - 1, out.length());
        out.append("})");
        try {
//            TODO where to get encoding from? It should match properties file's encoding, but probably be converted to response encoding
            return new ByteArrayInputStream(out.toString().getBytes(application.getViewHandler()
                .calculateCharacterEncoding(facesContext)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendArray(StringBuilder out, ResourceBundle applicationBundle, ResourceBundle stockBundle,
                             String jsPropertyName, String prefix, String[] keys) {
        String key;
        out.append(jsPropertyName).append(":[");
        for (int i = 0; i < keys.length; i++) {
            key = MESSAGE_KEY_BASE + prefix + "." + keys[i];
            out.append("'").append(escape(getMessageFromBundle(key, applicationBundle, stockBundle))).append("'");
            if (i + 1 < keys.length) {
                out.append(",");
            }
        }
        out.append("]");
    }

    private void appendMap(StringBuilder out, ResourceBundle applicationBundle, ResourceBundle stockBundle,
                           String jsPropertyName, String prefix, String[] keys) {
        String key;
        out.append(jsPropertyName).append(":{");
        for (int i = 0; i < keys.length; i++) {
            key = MESSAGE_KEY_BASE + prefix + "." + keys[i];
            out.append(keys[i]).append(":").append("'").append(escape(
                getMessageFromBundle(key, applicationBundle, stockBundle))
            ).append("'");
            if (i + 1 < keys.length) {
                out.append(",");
            }
        }
        out.append("}");
    }

    private String getMessageFromBundle(String key, ResourceBundle applicationBundle, ResourceBundle stockBundle) {
        try {
            return applicationBundle.getString(key);
        } catch (MissingResourceException e) {
            try {
                return stockBundle.getString(key);
            } catch (MissingResourceException e1) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Cannot find resource " + e1.getKey() + " in bundle " + e1.getClassName());
                }
                return "";
            }
        }
    }

    private String escape(String message) {
        return message.replaceAll("'", "\\\\'");
    }
}
