/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
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



package org.richfaces.cdk.rd.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.velocity.VelocityComponent;

import org.richfaces.builder.config.ParsingException;
import org.richfaces.builder.xml.XMLBody;

/**
 * @author Anton Belevich
 *
 */
public class StyleAssembler implements ResourceAssembler {
    protected Log log = new SystemStreamLog();
    private StringBuilder builder = new StringBuilder();
    private VelocityComponent velocityComponent;

    public StyleAssembler(Log log) {
        this.log = log;
    }

    public void assembly(URL resource) {
        String file = resource.getFile();

        if (log.isDebugEnabled()) {
            log.debug("process resource:  " + file);
        }

        try {
            InputStream resourceInputStream = resource.openStream();

            try {
                if (file.endsWith(".xcss")) {
                    XMLBody xmlBody = new XMLBody();

                    try {
                        xmlBody.loadXML(resourceInputStream, true);
                        builder.append(xmlBody.getContent());
                    } catch (ParsingException e) {
                        log.error("Error process xcss: " + e.getMessage(), e);
                    }
                } else {
                    builder.append(IOUtil.toString(resourceInputStream));
                }
            } finally {
                resourceInputStream.close();
            }
        } catch (IOException e) {
            log.error("Error process css file " + file + " : " + e.getMessage(), e);
        }
    }

    public VelocityComponent getVelocityComponent() {
        return velocityComponent;
    }

    public void setVelocityComponent(VelocityComponent velocityComponent) {
        this.velocityComponent = velocityComponent;
    }

    public void writeToFile(File file) {
        try {
            if (builder.length() > 0 && velocityComponent != null) {
                VelocityContext context = new VelocityContext();

                context.put("content", builder);

                VelocityEngine engine = velocityComponent.getEngine();
                FileWriter fileWriter = new FileWriter(file);

                log.info("write result to the: " + file.getPath());

                try {
                    Template velocityTemplate = engine.getTemplate("templates12/xcss.vm");

                    velocityTemplate.merge(context, fileWriter);
                    fileWriter.flush();
                } finally {
                    fileWriter.close();
                }
            }
        } catch (Exception e) {
            log.error("Error write file: " + file.getAbsolutePath(), e);
        }
    }
}
