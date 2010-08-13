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
package org.richfaces.cdk.resource.writer.impl;

import static org.richfaces.cdk.strings.Constants.COLON_JOINER;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import javax.faces.application.Resource;

import org.richfaces.cdk.ResourceWriter;
import org.richfaces.cdk.resource.writer.ResourceProcessor;
import org.richfaces.cdk.strings.Constants;
import org.richfaces.resource.ResourceFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceWriterImpl implements ResourceWriter {

    private File resourceContentsDir;
    
    private File resourceMappingDir;
    
    private Map<String, String> processedResources = Maps.newConcurrentMap();
    
    private Iterable<ResourceProcessor> resourceProcessors;
    
    public ResourceWriterImpl(File resourceContentsDir, File resourceMappingDir, Iterable<ResourceProcessor> resourceProcessors) {
        this.resourceContentsDir = resourceContentsDir;
        this.resourceMappingDir = resourceMappingDir;
        this.resourceProcessors = Iterables.concat(resourceProcessors, Collections.singleton(ThroughputResourceProcessor.INSTANCE));
        resourceContentsDir.mkdirs();
    }

    private String getResourceQualifier(Resource resource) {
        return COLON_JOINER.join(resource.getLibraryName(), resource.getResourceName());
    }
    
    private File createOutputFile(String path) throws IOException {
        File outFile = new File(resourceContentsDir, path);
        outFile.getParentFile().mkdirs();
        outFile.createNewFile();

        return outFile;
    }
    
    public void writeResource(String skinName, Resource resource) throws IOException {
        String requestPath = resource.getRequestPath();
        String requestPathWithSkin = requestPath;
        
        if (requestPath.startsWith(ResourceFactory.SKINNED_RESOURCE_PREFIX)) {
            requestPathWithSkin = Constants.SLASH_JOINER.join(skinName, 
                requestPath.substring(ResourceFactory.SKINNED_RESOURCE_PREFIX.length()));
        }
        
        for (ResourceProcessor resourceProcessor : resourceProcessors) {
            if (resourceProcessor.isSupportedFile(requestPath)) {
                File outFile = createOutputFile(requestPathWithSkin); 
                resourceProcessor.process(requestPathWithSkin, resource.getInputStream(), new FileOutputStream(outFile));
                processedResources.put(getResourceQualifier(resource), requestPath);
                return;
            }
        }
    }

    @Override
    public void writeProcessedResourceMappings() throws IOException {
        //TODO separate mappings file location
        FileOutputStream fos = null;
        try {
            File mappingsFile = new File(resourceMappingDir, ResourceFactory.STATIC_RESOURCE_MAPPINGS);
            //TODO merge properties
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
