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
package org.richfaces.cdk.resource;

import static org.richfaces.cdk.strings.Constants.COLON_JOINER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.Resource;

import org.richfaces.application.ServiceTracker;
import org.richfaces.cdk.FileNameMapper;
import org.richfaces.cdk.ResourceWriter;
import org.richfaces.resource.ResourceFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceWriterImpl implements ResourceWriter {

    private File resourceContentsDir;
    
    private File resourceMappingDir;
    
    private Map<String, String> processedResources = Maps.newConcurrentMap();
    
    public ResourceWriterImpl(File resourceContentsDir, File resourceMappingDir) {
        this.resourceContentsDir = resourceContentsDir;
        this.resourceMappingDir = resourceMappingDir;
        resourceContentsDir.mkdirs();
    }

    private String getResourceQualifier(Resource resource) {
        return COLON_JOINER.join(resource.getLibraryName(), resource.getResourceName());
    }
    
    private FileNameMapper getFileNameMapper() {
        return ServiceTracker.getService(FileNameMapper.class);
    }
    
    private File getRoot(String rootName) {
        if (!Strings.isNullOrEmpty(rootName)) {
            return new File(resourceContentsDir, rootName);
        } else {
            return resourceContentsDir;
        }
    }
    
    private String addSkinPrefix(String s) {
        return "%skin%/" + s;
    }
    
    public void writeResource(String skinName, Resource resource) throws IOException {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            is = resource.getInputStream();
            String mappedName = getFileNameMapper().createName(resource);
            File outFile = new File(getRoot(skinName), mappedName);
            outFile.getParentFile().mkdirs();
            outFile.createNewFile();
            
            fos = new FileOutputStream(outFile);
            ByteStreams.copy(is, fos);
            
            if (!Strings.isNullOrEmpty(skinName)) {
                mappedName = addSkinPrefix(mappedName);
            }
            
            processedResources.put(getResourceQualifier(resource), mappedName);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void writeProcessedResourceMappings() throws IOException {
        //TODO separate mappings file location
        FileOutputStream fos = null;
        try {
            File mappingsFile = new File(resourceMappingDir, ResourceFactory.STATIC_RESOURCE_MAPPINGS);
            mappingsFile.delete();
            mappingsFile.getParentFile().mkdirs();
            mappingsFile.createNewFile();
            
            fos = new FileOutputStream(mappingsFile);
            Properties properties = new Properties();
            properties.putAll(processedResources);
            properties.store(fos, null);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                // TODO: handle exception
            }
        }
    }

}
