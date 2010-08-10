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

import static org.richfaces.cdk.strings.Constants.SLASH_JOINER;
import static org.richfaces.cdk.strings.Constants.SLASH_SPLITTER;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.richfaces.cdk.vfs.VirtualFile;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 * 
 */
public class FileVFSFile implements VirtualFile {

    private File file;

    private String relativePath;
    
    public FileVFSFile(File file, String relativePath) {
        super();
        this.file = file;
        this.relativePath = relativePath;
    }
    
    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean isDirectory() {
        return file.isDirectory();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public Collection<VirtualFile> getChildren() {
        List<VirtualFile> result = Lists.newArrayList();
        
        String[] list = file.list();
        if (list != null) {
            for (String childName : list) {
                File child = new File(file, childName);
                result.add(new FileVFSFile(child, SLASH_JOINER.join(relativePath, childName)));
            }
        }

        return result;
    }

    @Override
    public VirtualFile getChild(String path) {
        return getChild(path, false);
    }
    
    @Override
    public VirtualFile getChild(String path, boolean chrooted) {
        Iterable<String> split = SLASH_SPLITTER.split(path);
        File result = file;
        for (String pathSeg : split) {
            result = new File(result, pathSeg);
            
            if (!result.exists()) {
                break;
            }
        }
        
        if (result.exists()) {
            return new FileVFSFile(result, chrooted ? null : SLASH_JOINER.join(relativePath, result.getName()));
        }
        
        return null;
    }

    protected File getFile() {
        return file;
    }
}
