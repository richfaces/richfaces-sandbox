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
package org.richfaces.bootstrap.ui.orderingList;

import org.richfaces.component.AbstractSelectManyComponent;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.RendererBase;
import org.richfaces.renderkit.SelectManyRendererBase;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIColumn;

/**
 * Base class for the orderingList renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 * 
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib"),

        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.position.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.core.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.widget.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.mouse.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.button.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.selectable.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.sortable.js"),

        @ResourceDependency(library = "org.richfaces", name = "widget/orderingList.js"),
        @ResourceDependency(library = "org.richfaces", name = "bridge/bootstrap.js"),
        @ResourceDependency(library = "org.richfaces", name = "bridge/orderingList.js")

        })
public abstract class OrderingListRendererBase extends SelectManyRendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.OrderingListRenderer";

    protected String[] getColumnClasses(AbstractSelectManyComponent orderingList) {
        String[] columnClasses;
        if (orderingList.getColumnClasses() != null) {
            columnClasses = orderingList.getColumnClasses().split(",");
        } else {
            columnClasses = new String[0];
        }
        return columnClasses;
    }

    protected String getColumnClass(UIColumn column, String[] columnClasses, int columnCount) {
        String columnClass = (String) column.getAttributes().get("styleClass");
        if (columnClasses != null && columnClasses.length > 0) {
            columnClass = HtmlUtil.concatClasses(columnClasses[columnCount % columnClasses.length], columnClass);
        }
        return columnClass;
    }
}
