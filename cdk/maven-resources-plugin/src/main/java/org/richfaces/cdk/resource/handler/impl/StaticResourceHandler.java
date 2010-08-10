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
package org.richfaces.cdk.resource.handler.impl;

import java.util.Collection;
import java.util.Collections;

import javax.activation.MimetypesFileTypeMap;
import javax.faces.application.Resource;

import org.richfaces.cdk.resource.ResourceUtil;
import org.richfaces.cdk.vfs.VirtualFile;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 * 
 */
public class StaticResourceHandler extends AbstractResourceHandler {

    private Collection<VirtualFile> roots;
    
    public StaticResourceHandler(Collection<VirtualFile> roots) {
        super();
        this.roots = roots;
    }

    private VirtualFile findLibrary(String libraryName) {
        for (VirtualFile file : roots) {
            VirtualFile child = file.getChild(libraryName);
            if (child == null) {
                continue;
            }
            
            VirtualFile libraryDir = ResourceUtil.getLatestVersion(child, true);
            if (libraryDir != null) {
                return libraryDir;
            }
        }
        
        return null;
    }

    private VirtualFile findResource(Collection<VirtualFile> libraryDirs, String resourceName) {
        for (VirtualFile libraryDir : libraryDirs) {
            VirtualFile child = libraryDir.getChild(resourceName);
            if (child != null) {
                VirtualFile resource = ResourceUtil.getLatestVersion(child, false);
                if (resource != null) {
                    return resource;
                }
            }
        }
        
        return null;
    }
    
    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Collection<VirtualFile> libraryDirs = Collections.emptyList();
        if (!Strings.isNullOrEmpty(libraryName)) {
            VirtualFile libraryDir = findLibrary(libraryName);
            if (libraryDir != null) {
                libraryDirs = Collections.singletonList(libraryDir);
            }
        } else {
            libraryDirs = roots;
        }
        
        VirtualFile resource = findResource(libraryDirs, resourceName);
        if (resource != null) {
            Resource result = new VFSResource(resource, resource.getRelativePath());
            
            result.setResourceName(resourceName);
            result.setLibraryName(libraryName);
            
            if (Strings.isNullOrEmpty(contentType)) {
                result.setContentType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(resourceName));
            } else {
                result.setContentType(contentType);
            }
            
            
            return result;
        }
        
        return null;
    }

}
