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
package org.richfaces.cdk.vfs.zip;

import static org.richfaces.cdk.strings.Constants.SLASH_SPLITTER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.richfaces.cdk.vfs.VFSRoot;
import org.richfaces.cdk.vfs.VFSType;

/**
 * @author Nick Belaevski
 * 
 */
public class ZipVFSRoot extends ZipVFSFile implements VFSRoot {

    private File file;
    
    public ZipVFSRoot(File file) throws IOException {
        super(new ZipFile(file), new ZipNode(null), null);

        assert file.isFile() && file.exists();
        this.file = file;
    }

    @Override
    public void initialize() throws IOException {
        Enumeration<? extends ZipEntry> entries = getZipFile().entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            
            String entryName = entry.getName();
            Iterable<String> split = SLASH_SPLITTER.split(entryName);
            ZipNode node = getZipNode();
            for (String pathSeg : split) {
                if (".".equals(pathSeg) || "..".equals(pathSeg)) {
                    continue;
                }
                
                node = node.getOrCreateChild(pathSeg);
            }
            
            node.setZipEntry(entry);
        }
    }
    
    @Override
    public void close() throws IOException {
        getZipFile().close();
    }

    @Override
    public VFSType getType() {
        return VFSType.zip;
    }

    public URL toURL() throws MalformedURLException {
        return file.toURI().toURL();
    };
    
    @Override
    public InputStream getInputStream() throws IOException {
        throw new IOException("Stream is not available");
    }

}
