/**
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
package org.richfaces.slidfast.renderkit;

import javax.faces.component.UIComponent;

import org.richfaces.slidfast.component.AbstractPageContainer;
import org.richfaces.ui.common.DivPanelRenderer;

/**
 * Base class for the pageContainer renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public abstract class PageContainerRendererBase extends DivPanelRenderer {
    public static final String RENDERER_TYPE = "org.richfaces.slidfast.PageContainerRenderer";

    // A workaround for RF-11668
    public AbstractPageContainer castComponent(UIComponent component) {
        return (AbstractPageContainer) component;
    }
}
