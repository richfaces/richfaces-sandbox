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
package org.richfaces.cdk.resource.scan.impl;

import java.util.Collection;

import org.richfaces.cdk.ResourceKey;
import org.richfaces.cdk.resource.scan.ResourcesScanner;
import org.richfaces.cdk.resource.util.ResourceUtil;
import org.richfaces.cdk.vfs.VirtualFile;

import com.google.common.collect.Sets;

/**
 * @author Nick Belaevski
 * 
 */
public class StaticResourcesScanner implements ResourcesScanner {

    private Collection<ResourceKey> resources = Sets.newHashSet();

    private Collection<VirtualFile> resourceRoots;
    
    public StaticResourcesScanner(Collection<VirtualFile> resourceRoots) {
        this.resourceRoots = resourceRoots;
    }

    private void scanResourcesRoot(VirtualFile file) {
        if (file == null) {
            return;
        }
        
        Collection<VirtualFile> children = file.getChildren();
        for (VirtualFile child : children) {
            if (child.isFile()) {
                resources.add(new ResourceKey(child.getName()));
            } else {
                String libraryName = child.getName();
                VirtualFile libraryDir = ResourceUtil.getLatestVersion(child, true);
                if (libraryDir != null) {
                    scanLibrary(libraryName, libraryDir);
                }
            }
        }
    }

    private void scanLibrary(String libraryName, VirtualFile dir) {
        Collection<VirtualFile> children = dir.getChildren();
        for (VirtualFile child : children) {
            if (child.isFile()) {
                resources.add(new ResourceKey(child.getName(), libraryName));
            } else {
                VirtualFile resource = ResourceUtil.getLatestVersion(child, false);
                if (resource != null) {
                    resources.add(new ResourceKey(child.getName(), libraryName));
                }
            }
        }
    }

    @Override
    public void scan() throws Exception {
        for (VirtualFile resourceRoot : resourceRoots) {
            scanResourcesRoot(resourceRoot);
        }
    }
    
    public Collection<ResourceKey> getResources() {
        return resources;
    }
    
}