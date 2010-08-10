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
package org.richfaces.cdk.faces;

import java.util.Collections;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import org.ajax4jsf.context.InitParametersStorage;
import org.richfaces.application.DependencyInjectionServiceImpl;
import org.richfaces.application.DependencyInjector;
import org.richfaces.application.Module;
import org.richfaces.application.ServiceTracker;
import org.richfaces.application.ServicesFactory;
import org.richfaces.application.ServicesFactoryImpl;
import org.richfaces.cdk.Faces;
import org.richfaces.cdk.FileNameMapper;
import org.richfaces.cdk.skin.SkinFactoryImpl;
import org.richfaces.skin.SkinFactory;

/**
 * @author Nick Belaevski
 * 
 */
public class FacesImpl implements Faces {

    private String webroot;
    
    private FileNameMapper fileNameMapper;

    private ResourceHandler resourceHandler;
    
    public FacesImpl(String webroot, FileNameMapper fileNameMapper, ResourceHandler resourceHandler) {
        super();
        this.webroot = webroot;
        this.fileNameMapper = fileNameMapper;
        this.resourceHandler = resourceHandler;
    }

    public void start() {
        final ServicesFactoryImpl serviceFactory = new ServicesFactoryImpl();
        Module module = new Module() {
            
            public void configure(ServicesFactory factory) {
                serviceFactory.setInstance(SkinFactory.class, new SkinFactoryImpl());
                serviceFactory.setInstance(InitParametersStorage.class, new InitParametersStorage());
                serviceFactory.setInstance(FileNameMapper.class, fileNameMapper);
                serviceFactory.setInstance(DependencyInjector.class, new DependencyInjectionServiceImpl());
                serviceFactory.setInstance(ResourceHandler.class, resourceHandler);
            }
        };
        serviceFactory.init(Collections.singleton(module));
        ServiceTracker.setFactory(serviceFactory);
    }

    public void stop() {
        ServiceTracker.release();
    }

    public void setSkin(String skinName) {
        SkinFactoryImpl.setSkinName(skinName);
    }

    public FacesContext startRequest() {
        FacesContextImpl facesContextImpl = new FacesContextImpl();
        facesContextImpl.getExternalContext().setWebRoot(webroot);
        assert FacesContext.getCurrentInstance() != null;
        
        return facesContextImpl;
    }

    public void stopRequest() {
        FacesContext.getCurrentInstance().release();
        assert FacesContext.getCurrentInstance() == null;
    }

}
