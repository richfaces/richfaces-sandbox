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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

final class ZipNode {
    
    private Map<String, ZipNode> children; 

    private String name;

    private ZipEntry zipEntry;
    
    private boolean directory;
    
    public ZipNode(String name) {
        super();
        this.name = name;
    }
    
    public ZipNode getOrCreateChild(String name) {
        setDirectory(true);
        
        if (children == null) {
            children = new LinkedHashMap<String, ZipNode>();
        }
        
        String lcName = name.toLowerCase();
        
        ZipNode node = children.get(lcName);
        if (node == null) {
            node = new ZipNode(name);
            children.put(lcName, node);
        }
        
        return node;
    }
    
    public ZipNode getChild(String name) {
        if (children == null) {
            return null;
        }
        
        return children.get(name.toLowerCase());
    }
    
    public String getName() {
        return name;
    }
    
    public Iterable<ZipNode> listChildren() {
        if (children == null) {
            return Collections.emptySet();
        }
        return children.values();
    }
    
    private void setDirectory(boolean directory) {
        this.directory = directory;
    }
    
    public boolean isDirectory() {
        return directory;
    }
    
    public void setZipEntry(ZipEntry zipEntry) {
        this.zipEntry = zipEntry;
        setDirectory(zipEntry.isDirectory());
    }
    
    public ZipEntry getZipEntry() {
        return zipEntry;
    }
    
}