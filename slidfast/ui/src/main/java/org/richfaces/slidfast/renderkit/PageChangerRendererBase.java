/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
 **/
package org.richfaces.slidfast.renderkit;

import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import org.richfaces.renderkit.RendererBase;
import org.richfaces.slidfast.component.AbstractPageChanger;
import org.richfaces.ui.misc.RichFunction;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "slidfast.js"),
        @ResourceDependency(library = "org.richfaces", name = "pageChanger.js"),
        @ResourceDependency(library = "org.richfaces", name = "slidfast.css")
})
public class PageChangerRendererBase extends RendererBase {
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        AbstractPageChanger pageChanger = (AbstractPageChanger) component;

        // Update the activePage value expression with the value from the request
        ValueExpression updateBinding = pageChanger.getActivePage();
        Object requestValue = context.getExternalContext().getRequestParameterMap().get(AbstractPageChanger.REQUEST_PARAM);
        if (requestValue != null) {
            updateBinding.setValue(context.getELContext(), requestValue);
        }

        // Re-render the page ids as requested by the pageChanger component
        PartialViewContext pvc = context.getPartialViewContext();
        pvc.getRenderIds().add(pageChanger.getRender());

        super.doDecode(context, component);
    }

    public String getClientId(String id) {
        return RichFunction.clientId(id);
    }

}
