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
package org.richfaces.cdk.vfs.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.richfaces.cdk.vfs.VFSRoot;
import org.richfaces.cdk.vfs.VFSType;

/**
 * @author Nick Belaevski
 * 
 */
public class FileVFSRoot extends FileVFSFile implements VFSRoot {

    public FileVFSRoot(File dir) {
        super(dir, null);
        
        assert dir.isDirectory() && dir.exists();
    }

    @Override
    public VFSType getType() {
        return VFSType.file;
    }
    
    @Override
    public void close() throws IOException {
        //nothing to close
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new IOException("Stream is not available");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void initialize() throws IOException {
        //do nothing
    }

    @Override
    public URL toURL() throws MalformedURLException {
        return getFile().toURI().toURL();
    }

}
