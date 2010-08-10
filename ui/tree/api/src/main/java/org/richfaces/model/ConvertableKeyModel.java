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

package org.richfaces.model;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Models that can handle keys conversion should implement this interface
 * 
 * For internal use only
 * 
 * @author Nick Belaevski
 * @since 3.2
 */

public interface ConvertableKeyModel {

    /**
     * Converts {@link String} to model object key
     * 
     * @param context
     * @param key
     * @param component
     * @param converter
     * @return
     */
    public Object getKeyAsObject(FacesContext context, String key, 
	    UIComponent component, Converter converter);

}
