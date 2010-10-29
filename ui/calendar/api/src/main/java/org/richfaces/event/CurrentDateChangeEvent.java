/*
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
 */

package org.richfaces.event;

import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

/**
 * @author amarkhel
 *
 */
public class CurrentDateChangeEvent extends FacesEvent {

    private static final long serialVersionUID = -8169207286087810907L;
    private Date currentDate = null;
    private String currentDateString = null;

    public CurrentDateChangeEvent(UIComponent component, String curentDateString) {
        super(component);
        this.currentDateString = curentDateString;
    }

    public boolean isAppropriateListener(FacesListener listener) {
        return (listener instanceof CurrentDateChangeListener);
    }

    public void processListener(FacesListener listener) {
        ((CurrentDateChangeListener)listener).processCurrentDateChange(this);
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentDateString() {
        return currentDateString;
    }

}
