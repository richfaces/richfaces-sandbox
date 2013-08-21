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
package org.richfaces.jqueryui.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;

import org.richfaces.jqueryui.component.AbstractDatepicker;
import org.richfaces.ui.input.InputRendererBase;

@ResourceDependencies({
        @ResourceDependency(library = "javax.faces", name = "jsf.js"),
        @ResourceDependency(name = "jquery.js"),
        @ResourceDependency(library = "com.jqueryui/css/ui-lightness", name = "jquery-ui.custom.css"),
        @ResourceDependency(library = "com.jqueryui/development-bundle/ui", name = "jquery.ui.core.js"),
        @ResourceDependency(library = "com.jqueryui/development-bundle/ui", name = "jquery.ui.widget.js"),
        @ResourceDependency(library = "com.jqueryui/development-bundle/ui", name = "jquery.ui.datepicker.js"),
        @ResourceDependency(library = "org.richfaces", name = "calendar.gif")
})
/**
 * Base class for the datepicker renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class DatepickerRendererBase extends InputRendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.jqueryui.DatepickerRenderer";

    // A workaround for RF-11668
    public AbstractDatepicker castComponent(UIComponent component) {
        return (AbstractDatepicker) component;
    }

}
