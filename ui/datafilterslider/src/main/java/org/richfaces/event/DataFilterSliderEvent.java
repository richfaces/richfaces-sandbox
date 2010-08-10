/**
 * License Agreement.
 *
 *  JBoss RichFaces - Ajax4jsf Component Library
 *
 * Copyright (C) 2007  Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.richfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

import org.ajax4jsf.event.AjaxEvent;

/**
 * @author Wesley Hales
 */
public class DataFilterSliderEvent extends AjaxEvent {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer  oldSliderVal;
    private Integer  newSliderVal;

    /**
    * Creates a new SliderEvent.
    *
    * @param component         the source of the event
    * @param thisOldSliderVal  the previously showing item identifier
    * @param thisNewSliderVal  the currently showing item identifier
    */
    public DataFilterSliderEvent(UIComponent component, Integer thisOldSliderVal, Integer thisNewSliderVal) {
        super(component);
        setPhaseId(PhaseId.INVOKE_APPLICATION);
        oldSliderVal = thisOldSliderVal;
        newSliderVal = thisNewSliderVal;
    }

    public Integer getOldSliderVal() {
        return oldSliderVal;
    }

    public Integer getNewSliderVal() {
        return newSliderVal;
    }

    public boolean isAppropriateListener(FacesListener listener){
        return (listener instanceof DataFilterSliderListener);
    }

    /**
    * Delivers this event to the SliderListener.
    *
    * @param listener  the slider listener
    */
    public void processListener(FacesListener listener){
        ((DataFilterSliderListener) listener).processSlider(this);
    }
}
