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
package org.richfaces.sandbox.composite;

import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@FacesComponent("org.richfaces.sandbox.composite.UIDatepicker")
public class UIDatepicker extends UINamingContainer {

    protected enum Properties {
        buttonImageOnly,
        dateFormat,
        showOn
    }

    public String getButtonImageOnly() {
        String value = (String) getStateHelper().eval(Properties.buttonImageOnly);
        return value;
    }

    public void setButtonImageOnly(String buttonImageOnly) {
        getStateHelper().put(Properties.buttonImageOnly, buttonImageOnly);
    }


    public String getDateFormat() {
        String value = (String) getStateHelper().eval(Properties.dateFormat);
        return value;
    }

    public void setDateFormat(String dateFormat) {
        getStateHelper().put(Properties.dateFormat, dateFormat);
    }


    public String getShowOn() {
        String value = (String) getStateHelper().eval(Properties.showOn);
        return value;
    }

    public void setShowOn(String showOn) {
        getStateHelper().put(Properties.showOn, showOn);
    }
}