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
package org.richfaces.sandbox.select.pickList;

import com.google.common.collect.Iterators;
import org.richfaces.ui.select.AbstractSelectManyComponent;
import org.richfaces.ui.select.ClientSelectItem;
import org.richfaces.ui.select.SelectManyHelper;
import org.richfaces.ui.select.SelectManyRendererBase;
import org.richfaces.util.HtmlUtil;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIColumn;
import java.util.Iterator;
import java.util.List;

/**
 * Base class for the pickList renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 *
 */
public abstract class PickListRendererBase extends SelectManyRendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.select.PickListRenderer";

    protected Iterator<ClientSelectItem> getSourceSelectItems(List<ClientSelectItem> clientSelectItems) {
        return Iterators.filter(clientSelectItems.iterator(), SelectManyHelper.UNSELECTED_PREDICATE);
    }

    protected Iterator<ClientSelectItem> getTargetSelectItems(List<ClientSelectItem> clientSelectItems) {
        return Iterators.filter(clientSelectItems.iterator(), SelectManyHelper.SELECTED_PREDICATE);
    }
}
