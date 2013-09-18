/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.richfaces.sandbox.select.orderingList;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.ui.select.ClientSelectItem;
import org.richfaces.ui.select.SelectManyHelper;
import org.richfaces.ui.select.SelectManyRendererBase;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(name = "jquery.position.js"), @ResourceDependency(name = "richfaces-event.js"),
        @ResourceDependency(name = "richfaces-utils.js"), @ResourceDependency(name = "richfaces-selection.js"),
        @ResourceDependency(library = "org.richfaces", name = "inputBase.js"),
        @ResourceDependency(library = "org.richfaces", name = "popup.js"),
        @ResourceDependency(library = "org.richfaces", name = "select/list.js"),
        @ResourceDependency(library = "org.richfaces", name = "select/listMulti.js"),
        @ResourceDependency(library = "org.richfaces", name = "select/orderingList/orderingList.js"),
        @ResourceDependency(library = "org.richfaces", name = "select/orderingList/orderingList.ecss")})
public class OrderingListRendererBase extends SelectManyRendererBase {
    public static String CSS_PREFIX = "rf-ord";

    public void encodeHeader(FacesContext facesContext, UIComponent component) throws IOException {
        SelectManyHelper.encodeHeader(facesContext, component, this, "rf-ord-hdr", "rf-ord-hdr-c");
    }

    public void encodeRows(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        SelectManyHelper.encodeRows(facesContext, component, this, clientSelectItems.iterator(), CSS_PREFIX);
    }

    public void encodeItems(FacesContext facesContext, UIComponent component, List<ClientSelectItem> clientSelectItems) throws IOException {
        SelectManyHelper.encodeItems(facesContext, component, clientSelectItems.iterator(), CSS_PREFIX);
    }

    public String getButtonClass(UIComponent component, String buttonClass) {
        return getButtonClass(component, CSS_PREFIX, buttonClass);
    }
}
