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

import static org.richfaces.cdk.strings.Constants.SLASH_JOINER;
import static org.richfaces.cdk.strings.Constants.SLASH_SPLITTER;

import java.beans.FeatureDescriptor;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ELResolver;
import javax.el.PropertyNotFoundException;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 * 
 */
public class ResourceELResolver extends ELResolver {

    private void checkBaseAndProperty(Object base, Object property) {
        if (base == null && property == null) {
            throw new PropertyNotFoundException("base & property are null");
        }
    }

    private String skipEqualPathSegments(Iterator<String> path, Iterator<String> basePath) {
        while (path.hasNext()) {
            String pathSeg = path.next();
            if (basePath.hasNext()) {
                String basePathSeg = basePath.next();
                if (basePathSeg.equals(pathSeg)) {
                    continue;
                }
            }

            return pathSeg;
        }
        
        return null;
    }
    
    private String relativize(String path, String basePath) {
        Iterator<String> pathItr = SLASH_SPLITTER.split(path).iterator();
        Iterator<String> basePathItr = SLASH_SPLITTER.split(basePath).iterator();

        List<String> resultPathSegments = Lists.newArrayList();
        String firstNonMatchedSegment = skipEqualPathSegments(pathItr, basePathItr);
        while (basePathItr.hasNext()) {
            basePathItr.next();
            resultPathSegments.add("..");
        }
        
        if (firstNonMatchedSegment != null) {
            resultPathSegments.add(firstNonMatchedSegment);
        }
        
        while (pathItr.hasNext()) {
            resultPathSegments.add(pathItr.next());
        }
        
        return SLASH_JOINER.join(resultPathSegments);
    }

    public Object getValue(ELContext context, Object base, Object property) {
        checkBaseAndProperty(base, property);

        if (base instanceof ResourceHandler) {
            ResourceHandler handler = (ResourceHandler) base;
            String prop = (String) property;
            Resource resource;
            if (!prop.contains(":")) {
                resource = handler.createResource(prop);
            } else {
                String[] parts = prop.split(":");
                if (parts.length != 2) {
                    throw new ELException(MessageFormat.format("Invalid resource format. Property {0} contains more than one colon (:)", prop));
                }
                resource = handler.createResource(parts[1], parts[0]);
            }

            context.setPropertyResolved(true);

            if (resource != null) {
                String requestPath = resource.getRequestPath();
                FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
                Resource contextResource = CurrentResourceContext.getInstance(facesContext).getResource();
                if (contextResource != null) {
                    requestPath = relativize(requestPath, contextResource.getRequestPath());
                }

                return requestPath;
            }
        }
        return null;
    }

    public Class<?> getType(ELContext context, Object base, Object property) {
        checkBaseAndProperty(base, property);
        return null;
    }

    public void setValue(ELContext context, Object base, Object property, Object value) {
        checkBaseAndProperty(base, property);
    }

    public boolean isReadOnly(ELContext context, Object base, Object property) {
        checkBaseAndProperty(base, property);

        return true;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return String.class;
    }

}
