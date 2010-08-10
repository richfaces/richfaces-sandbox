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

import org.codehaus.plexus.util.IOUtil;

/**
 * @author Anton Belevich
 *
 */
public class ScriptAssembler implements ResourceAssembler {
    private StringBuilder builder = new StringBuilder();
    protected Log log;

    public ScriptAssembler(Log log) {
        this.log = log;
    }

    public void assembly(URL resource) {
        InputStream inputStream = null;

        try {
            inputStream = resource.openStream();
            builder.append(IOUtil.toString(inputStream));
            builder.append("\n");
        } catch (IOException e) {
            log.error("Error read resource: " + resource.getFile());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    public void writeToFile(File file) {
        if (builder.length() > 0) {
            try {
                FileWriter fileWriter = new FileWriter(file);

                try {
                    log.info("write result to the:  " + file.getPath());
                    IOUtil.copy(builder.toString(), fileWriter);
                } finally {
                    fileWriter.close();
                }
            } catch (IOException e) {
                log.error("Error write file: " + file.getAbsolutePath(), e);
            }
        }
    }
}
