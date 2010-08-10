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

import static org.richfaces.cdk.strings.Constants.DASH_JOINER;
import static org.richfaces.cdk.strings.Constants.DOT_JOINER;
import static org.richfaces.cdk.strings.Constants.SLASH_JOINER;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import org.richfaces.resource.VersionedResource;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 * 
 */
public class DynamicResourceWrapper extends Resource {

    /**
     * 
     */
    private static final String ECSS_EXTENSION = ".ecss";

    private Resource resource;

    public DynamicResourceWrapper(Resource resource) {
        super();
        this.resource = resource;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    public String getContentType() {
        return resource.getContentType();
    }

    public void setContentType(String contentType) {
        resource.setContentType(contentType);
    }

    public String getLibraryName() {
        return resource.getLibraryName();
    }

    public void setLibraryName(String libraryName) {
        resource.setLibraryName(libraryName);
    }

    public String getResourceName() {
        return resource.getResourceName();
    }

    public void setResourceName(String resourceName) {
        resource.setResourceName(resourceName);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        throw new UnsupportedOperationException();
    }

    private String getMangledLibraryName() {
        String resourceName = getResourceName();
        if (Strings.isNullOrEmpty(getLibraryName()) && !resourceName.endsWith(ECSS_EXTENSION)) {
            int idx = resourceName.lastIndexOf('.');
            if (idx < 0) {
                return null;
            } else {
                return resourceName.substring(0, idx);
            }
        }

        return getLibraryName();
    }

    private String getMangledResourceName() {
        String resourceName = getResourceName();
        if (resourceName.endsWith(ECSS_EXTENSION)) {
            return resourceName.substring(0, resourceName.length() - ECSS_EXTENSION.length());
        }
        
        if (Strings.isNullOrEmpty(getLibraryName())) {
            int idx = resourceName.lastIndexOf('.');
            if (idx < 0) {
                return resourceName;
            } else {
                return resourceName.substring(idx + 1);
            }
        } else {
            return resourceName;
        }
    }
    
    private String getVersion() {
        String version = null;
        if (resource instanceof VersionedResource) {
            version = ((VersionedResource) resource).getVersion();
        }
        return version;
    }
    
    @Override
    public String getRequestPath() {
        String mangledLibraryName = getMangledLibraryName();
        String mangledResourceName = getMangledResourceName();
        String resourceExtension = getResourceExtension();
        
        String resourceName = DOT_JOINER.join(DASH_JOINER.join(mangledResourceName, getVersion()), resourceExtension);
        
        return SLASH_JOINER.join(mangledLibraryName, resourceName);
    }

    @Override
    public URL getURL() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        throw new UnsupportedOperationException();
    }

    protected String getResourceExtension() {
        String contentType = getContentType();
        if (contentType == null) {
            return null;
        }
        
        if (contentType.startsWith("text/") || contentType.startsWith("image/")) {
            String[] split = contentType.split("/");
            if (split.length == 2) {
                return split[1];
            }
        }
        
        return null;
    }
}
