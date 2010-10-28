package org.richfaces.taglib;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.component.AbstractCalendar;

public class CalendarHandler extends ComponentHandler{
    
    private static final CalendarHandlerMetaRule METARULE = new CalendarHandlerMetaRule();


    public CalendarHandler(ComponentConfig config) {
        super(config);
    }

    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        m.addRule(METARULE);
        return m;
    }

    static class CalendarHandlerMetaRule extends MetaRule {

        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(AbstractCalendar.class) && "currentDataChangeListener".equals(name)) {
                return new CalendarMapper(attribute);
            }
            return null;
        }

    }

    static class CalendarMapper extends Metadata {

        private static final Class[] SIGNATURE = new Class[] { org.richfaces.events.CurrentDateChangeListener.class };

        private final TagAttribute attribute;

        public CalendarMapper(TagAttribute attribute) {
            this.attribute = attribute;
        }

        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((AbstractCalendar) instance).addCurrentDateChangeListener((new MethodExpressionCurrentDateChangeListener(this.attribute.getMethodExpression(ctx, null, SIGNATURE))));
        }
    }
}
