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
package org.richfaces.sandbox.view.facelets.html;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

import org.richfaces.event.ItemChangeEvent;
import org.richfaces.event.ItemChangeSource;
import org.richfaces.sandbox.event.MethodExpressionItemChangeListener;

/**
 * @author akolonitsky
 * @since 2010-08-13
 */
public class TogglePanelTagHandler extends ComponentHandler {
    private static final MetaRule META_RULE = new TogglePanelMetaRule();

    public TogglePanelTagHandler(ComponentConfig config) {
        super(config);
    }

    @Override
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset metaRuleset = super.createMetaRuleset(type);
        metaRuleset.addRule(META_RULE);
        return metaRuleset;
    }

    private static class TogglePanelMetaRule extends MetaRule {
        @Override
        public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget meta) {
            if (meta.isTargetInstanceOf(ItemChangeSource.class)) {
                if ("itemChangeListener".equals(name)) {
                    return new ItemChangeExpressionMetadata(attribute);
                }
            }
            return null;
        }
    }

    private static final class ItemChangeExpressionMetadata extends Metadata {
        private static final Class<?>[] ITEM_CHANGE_SIG = new Class[] { ItemChangeEvent.class };
        private final TagAttribute attr;

        ItemChangeExpressionMetadata(TagAttribute attr) {
            this.attr = attr;
        }

        @Override
        public void applyMetadata(FaceletContext ctx, Object instance) {
            ((ItemChangeSource) instance).addItemChangeListener(new MethodExpressionItemChangeListener(this.attr
                .getMethodExpression(ctx, null, ITEM_CHANGE_SIG)));
        }
    }
}
