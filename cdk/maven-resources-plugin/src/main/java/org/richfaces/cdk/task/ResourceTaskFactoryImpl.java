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
package org.richfaces.cdk.task;

import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.apache.maven.plugin.logging.Log;
import org.richfaces.cdk.Faces;
import org.richfaces.cdk.ResourceKey;
import org.richfaces.cdk.ResourceTaskFactory;
import org.richfaces.cdk.ResourceWriter;
import org.richfaces.cdk.faces.CurrentResourceContext;
import org.richfaces.cdk.resource.handler.impl.DynamicResourceWrapper;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceTaskFactoryImpl implements ResourceTaskFactory {

    private class ResourcesRendererCallable implements Callable<Object> {

        private ResourceKey resourceInfo;
        
        private boolean skinDependent;
        
        private boolean skipped = false;
        
        ResourcesRendererCallable(ResourceKey resourceInfo) {
            this.resourceInfo = resourceInfo;
        }

        private Resource createResource(FacesContext facesContext, ResourceKey resourceInfo) {
            ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
            return resourceHandler.createResource(resourceInfo.getResourceName(), resourceInfo.getLibraryName());
        }
        
        private void renderResource(String skin) {
            try {
                FacesContext facesContext = faces.startRequest();
                
                if (skin != null) {
                    faces.setSkin(skin);
                }
                
                Resource resource = createResource(facesContext, resourceInfo);
                CurrentResourceContext.getInstance(facesContext).setResource(resource);
                //TODO check content type
                resourceWriter.writeResource(skin, resource);
            } catch (Exception e) {
                if (skin != null) {
                    log.error(MessageFormat.format("Exception rendering resorce {0} using skin {1}: {2}", resourceInfo, skin, e.getMessage()), e);
                } else {
                    log.error(MessageFormat.format("Exception rendering resorce {0}: {1}", resourceInfo, e.getMessage()), e);
                }
            } finally {
                faces.setSkin(null);
                faces.stopRequest();
            }
        }

        private void checkResource() {
            try {
                FacesContext facesContext = faces.startRequest();
                faces.setSkin("DEFAULT");
                
                Resource resource = createResource(facesContext, resourceInfo);
                if (resource == null) {
                    //TODO log null resource
                    skipped = true;
                    return;
                }

                if (!filter.apply(resource)) {
                    skipped = true;
                    return;
                }
                
                String contentType = resource.getContentType();
                if (contentType == null) {
                    //TODO log null content type
                    skipped = true;
                    return;
                }
                
                //TODO hack
                skinDependent = (resource instanceof DynamicResourceWrapper);
            } finally {
                faces.setSkin(null);
                faces.stopRequest();
            }
        }
        
        public Object call() throws Exception {
            checkResource();
            if (!skipped) {
                if (skinDependent) {
                    for (String skin : skins) {
                        renderResource(skin);
                    }
                } else {
                    renderResource(null);
                }
            }
            return null;
        }
        
    }
    
    private Log log;
    
    private Faces faces;
    
    private ResourceWriter resourceWriter;
    
    private CompletionService<Object> completionService;
    
    private String[] skins = new String[0];
    
    private Predicate<Resource> filter = Predicates.alwaysTrue();
    
    public ResourceTaskFactoryImpl(Faces faces) {
        super();
        this.faces = faces;
    }

    public void setLog(Log log) {
        this.log = log;
    }
    
    public void setResourceWriter(ResourceWriter resourceWriter) {
        this.resourceWriter = resourceWriter;
    }
    
    public void setSkins(String[] skins) {
        this.skins = skins;
    }

    public void setCompletionService(CompletionService<Object> completionService) {
        this.completionService = completionService;
    }
    
    public void setFilter(Predicate<Resource> filter) {
        this.filter = filter;
    }
    
    public void submit(Iterable<ResourceKey> locators) {
        for (ResourceKey locator : locators) {
            completionService.submit(new ResourcesRendererCallable(locator));
        }
    }
}
