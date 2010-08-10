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

import static org.richfaces.cdk.strings.Constants.SLASH_JOINER;
import static org.richfaces.cdk.strings.Constants.SLASH_SPLITTER;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.richfaces.cdk.vfs.VirtualFile;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 * 
 */
public class ZipVFSFile implements VirtualFile {

    private ZipFile zipFile;
    
    private ZipNode zipNode;
    
    private String relativePath;
    
    public ZipVFSFile(ZipFile zipFile, ZipNode zipNode, String relativePath) {
        super();
        this.zipFile = zipFile;
        this.zipNode = zipNode;
        this.relativePath = relativePath;
    }

    @Override
    public boolean isFile() {
        return !isDirectory();
    }

    @Override
    public boolean isDirectory() {
        return zipNode.isDirectory();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        ZipEntry entry = zipNode.getZipEntry();
        if (entry != null) {
            return zipFile.getInputStream(entry);
        }

        throw new IOException("Input stream isn't available!");
    }

    @Override
    public Collection<VirtualFile> getChildren() {
        Iterable<ZipNode> children = zipNode.listChildren();
        
        List<VirtualFile> result = Lists.newArrayList();
        
        for (ZipNode child: children) {
            result.add(new ZipVFSFile(zipFile, child, SLASH_JOINER.join(getRelativePath(), child.getName())));
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
        ZipNode node = zipNode;
        for (String pathSeg : split) {
            node = node.getChild(pathSeg);
            if (node == null) {
                return null;
            }
        }
        
        String relativePath = null;
        if (!chrooted) {
            relativePath = SLASH_JOINER.join(getRelativePath(), path);
        }
        
        return new ZipVFSFile(zipFile, node, relativePath);
    }

    @Override
    public String getName() {
        return zipNode.getName();
    }

    @Override
    public String getRelativePath() {
        return relativePath;
    }
    
    protected ZipFile getZipFile() {
        return zipFile;
    }
    
    protected ZipNode getZipNode() {
        return zipNode;
    }
}
