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
package org.richfaces.slidfast.component;


import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponentBase;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@JsfComponent(
        type = "org.richfaces.slidfast.PageChanger",
        family = "org.richfaces.Output",
        renderer = @JsfRenderer(type = "org.richfaces.slidfast.PageChangerRenderer"), tag = @Tag(name="pageChanger"))
abstract public class AbstractPageChanger extends UIComponentBase {
    private static final String ACTIVE_PAGE = "activePage";
    public static final String REQUEST_PARAM = "org.richfaces.slidfast.activePage";

    /**
     * Ids of components that will participate in the "render" portion of the Request Processing Lifecycle.
     * Can be a single id, a space or comma separated list of Id's, or an EL Expression evaluating to an array or Collection.
     * Any of the keywords "@this", "@form", "@all", "@none", "@region" may be specified in the identifier list.
     * Some components make use of additional keywords
     */
    @Attribute(defaultValue = "@form")
    abstract public String getRender();

    /**
     * The value holder for the active page.
     */
    @Attribute(generate = false)
    public ValueExpression getActivePage(){
        return getValueExpression(ACTIVE_PAGE);
    }

    public void setActivePage(ValueExpression ve) {
        setValueExpression(ACTIVE_PAGE, ve);
    }

}
