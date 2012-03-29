/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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

import org.richfaces.cdk.annotations.*;
import org.richfaces.renderkit.html.SyntaxHighlighterRenderer;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.render.Renderer;

@JsfComponent(tag = @Tag(name = "syntaxhighlighter", type = TagType.Facelets),
        renderer = @JsfRenderer(family = AbstractSyntaxHighlighter.COMPONENT_FAMILY, type = SyntaxHighlighterRenderer.RENDERER_TYPE), attributes = "core-props.xml")
public abstract class AbstractSyntaxHighlighter extends UIComponentBase implements SystemEventListener, ComponentSystemEventListener {
// ------------------------------ FIELDS ------------------------------

    public static final String COMPONENT_FAMILY = "org.richfaces.Syntaxhighlighter";

    public static final String COMPONENT_TYPE = "org.richfaces.Syntaxhighlighter";

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ComponentSystemEventListener ---------------------

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        super.processEvent(event);
        if (event instanceof PreRenderViewEvent) {
            Renderer renderer = getRenderer(getFacesContext());
            ((ComponentSystemEventListener) renderer).processEvent(new PreRenderComponentEvent(this));
        }
    }

// --------------------- Interface SystemEventListener ---------------------

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PreRenderViewEvent) {
            Renderer renderer = getRenderer(getFacesContext());
            if (renderer instanceof ComponentSystemEventListener) {
                ((ComponentSystemEventListener) renderer).processEvent(new PreRenderComponentEvent(this));
            }
        }
    }

    @Override
    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

// -------------------------- OTHER METHODS --------------------------

    @Attribute
    public abstract String getCopyToClipboardConfirmationLabel();

    @Attribute
    public abstract String getCopyToClipboardLabel();

    @Attribute
    public abstract String getExpandSourceLabel();

    @Attribute(defaultValue = "1")
    public abstract int getFirstLine();

    @Attribute
    public abstract String getHelpLabel();

    @Attribute
    public abstract Object getHighlight();

    @Attribute(defaultValue = "plain")
    public abstract String getLanguage();

    @Attribute
    public abstract String getPrintLabel();

    @Attribute(defaultValue = "4")
    public abstract int getTabSize();

    @Attribute(defaultValue = "Default")
    public abstract String getTheme();

    @Attribute
    public abstract String getViewSourceLabel();

    @Attribute(defaultValue = "true")
    public abstract boolean isAutoLinks();

    @Attribute(defaultValue = "false")
    public abstract boolean isBloggerMode();

    @Attribute(defaultValue = "false")
    public abstract boolean isCollapsed();

    @Attribute(defaultValue = "true")
    public abstract boolean isGutter();

    @Attribute(defaultValue = "false")
    public abstract boolean isHtmlScript();

    @Attribute(defaultValue = "true")
    public abstract boolean isSmartTabs();

    @Attribute(defaultValue = "false")
    public abstract boolean isStripBrs();

    @Attribute(defaultValue = "true")
    public abstract boolean isToolbar();

    public void subscribeToPreRenderViewEvent() {
        FacesContext.getCurrentInstance().getViewRoot().subscribeToViewEvent(PreRenderViewEvent.class, this);
    }
}
