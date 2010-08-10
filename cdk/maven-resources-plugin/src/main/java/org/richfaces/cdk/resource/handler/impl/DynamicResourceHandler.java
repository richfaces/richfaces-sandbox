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

import java.io.IOException;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

import org.richfaces.resource.ResourceFactory;
import org.richfaces.resource.ResourceFactoryImpl;
import org.richfaces.resource.StateHolderResource;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

/**
 * @author Nick Belaevski
 * 
 */
public class DynamicResourceHandler extends AbstractResourceHandler  {

    private ResourceFactory resourceFactory;
    
    private ResourceHandler staticResourceHandler;
    
    public DynamicResourceHandler(ResourceHandler staticResourceHandler) {
        this.staticResourceHandler = staticResourceHandler;
        this.resourceFactory = new ResourceFactoryImpl(staticResourceHandler);
    }
    
    @Override
    public Resource createResource(String resourceName, String libraryName, String contentType) {
        Resource result = resourceFactory.createResource(resourceName, libraryName, null);
        
        if (result != null) {
            if (result instanceof StateHolderResource) {
                StateHolderResource stateHolderResource = (StateHolderResource) result;
                ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
                try {
                    stateHolderResource.writeState(FacesContext.getCurrentInstance(), dataOutput);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                byte[] bs = dataOutput.toByteArray();
                //TODO use collected data
            } else if (result instanceof StateHolder) {
                StateHolder stateHolder = (StateHolder) result;
                if (!stateHolder.isTransient()) {
                    Object savedData = stateHolder.saveState(FacesContext.getCurrentInstance());
                    //TODO use collected data
                }
            }

            result = new DynamicResourceWrapper(result);
        } else {
            result = staticResourceHandler.createResource(resourceName, libraryName);
        }
        
        return result;
    }
}
