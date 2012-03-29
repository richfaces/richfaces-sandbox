/*
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

package org.richfaces.renderkit.html;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractOutputFile;
import org.richfaces.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.*;
import java.net.URL;

@JsfRenderer(family = AbstractOutputFile.COMPONENT_FAMILY, type = OutputFileRenderer.RENDERER_TYPE)
public class OutputFileRenderer extends RendererBase {
// ------------------------------ FIELDS ------------------------------

    public static final String RENDERER_TYPE = "org.richfaces.OutputFileRenderer";

// -------------------------- OTHER METHODS --------------------------

    @Override
    public void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        if (!(component instanceof AbstractOutputFile)) {
            return;
        }
        AbstractOutputFile outputFile = (AbstractOutputFile) component;
        String contents = null;
        Object value = outputFile.getValue();
        if (value instanceof File) {
            contents = getContents((File) value);
        } else if (value instanceof InputStream) {
            contents = getContents((InputStream) value);
        } else if (value != null) {
            String resourcePath = value.toString();
            URL resource = context.getExternalContext().getResource(resourcePath);
            if (resource == null) {
                throw new FileNotFoundException(resourcePath);
            }
            contents = getContents(resource.openStream());
        }
        if (contents != null) {
            if (outputFile.isEscape()) {
                writer.writeText(contents, null);
            } else {
                writer.write(contents);
            }
        }
    }

    private String getContents(File value) throws IOException {
        return getContents(new FileInputStream(value));
    }

    private String getContents(InputStream value) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();
        boolean firstLine = true;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(value));
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    builder.append("\n");
                }
                builder.append(line);
            }
        } finally {
            value.close();
        }
        return builder.toString();
    }
}
