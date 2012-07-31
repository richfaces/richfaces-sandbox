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
package org.richfaces.bootstrap.demo.capitals;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@FacesConverter("CapitalsConverter")
public class CapitalsConverter implements Converter {
    private CapitalsParser capitalsParser;

    public Object getAsObject(FacesContext facesContext, UIComponent component, String s) {
        for (Capital capital : getCapitalsParser(facesContext).getCapitalsList()) {
            if (capital.getName().equals(s)) {
                return capital;
            }
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object o) {
        if (o == null) return null;
        return ((Capital) o).getName();
    }

    private CapitalsParser getCapitalsParser(FacesContext facesContext) {
        if (capitalsParser == null) {
            ELContext elContext = facesContext.getELContext();
            capitalsParser = (CapitalsParser) elContext.getELResolver().getValue(elContext, null, "capitalsParser");
        }
        return capitalsParser;
    }
}
