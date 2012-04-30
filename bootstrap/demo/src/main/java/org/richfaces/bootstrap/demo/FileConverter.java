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
package org.richfaces.bootstrap.demo;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.Scanner;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@FacesConverter(value = "FileConverter")
public class FileConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return null;  // this is a one-way converter
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        String filePath = value.toString();
        BufferedInputStream inputStream = null;
        String string;
        try {
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            inputStream = new BufferedInputStream(servletContext.getResourceAsStream(filePath));
            string = new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            throw new ConverterException(e);
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (IOException ignored) { }
        }
        return string;
    }
}
