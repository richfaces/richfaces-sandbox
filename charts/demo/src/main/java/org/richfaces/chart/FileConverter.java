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
package org.richfaces.chart;

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
            
            // Suppress useless tags <ui:composition> and </ui:composition>
            // in order to save space and focus on what is really important
            if(string.startsWith("<ui:composition")) {
                int indexOfGt = string.indexOf(">");
                int indexOfLt = string.lastIndexOf("<");
                string = string.substring(indexOfGt+1, indexOfLt);
                
                // Correct indentation
                string = string.replaceAll("\n    ", "\n");
            }
            
            // Removing useless empty lines at the beginning and at the end
            // of the code source
            int indexOfNewline = string.indexOf("\n");
            while(indexOfNewline == 0) {
                string = string.substring(1);
                indexOfNewline = string.indexOf("\n");
            }
            
            int lastIndexOfNewline = string.lastIndexOf("\n");
            int length = string.length();
            while(lastIndexOfNewline+1 == length) {
                string = string.substring(0, lastIndexOfNewline);
                lastIndexOfNewline = string.lastIndexOf("\n");
                length = string.length();
            }
        } catch (java.util.NoSuchElementException e) {
            throw new ConverterException(e);
        } finally {
            if (inputStream != null) try { inputStream.close(); } catch (IOException ignored) { }
        }
        return string;
    }
}