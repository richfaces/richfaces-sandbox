/**
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
 **/
package org.richfaces.bootstrap.demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.richfaces.bootstrap.demo.jaxb.javaee.FaceletTaglibTagAttributeType;
import org.richfaces.bootstrap.demo.jaxb.javaee.FaceletTaglibTagType;
import org.richfaces.bootstrap.demo.jaxb.javaee.FaceletTaglibType;

import com.google.common.collect.ImmutableList;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ManagedBean
@ApplicationScoped
public class TaglibReader {
    private FaceletTaglibType taglib;
    private List<FaceletTaglibTagType> tags;
    private HashMap<String, FaceletTaglibTagType> tagMap;
    
    // Attributes separation
    // One list == one tab on VDL
    private Map<String, List<FaceletTaglibTagAttributeType>> defaultAttributes = new HashMap<String, List<FaceletTaglibTagAttributeType>>();
    private Map<String, List<FaceletTaglibTagAttributeType>> globalAttributes = new HashMap<String, List<FaceletTaglibTagAttributeType>>();
    private Map<String, List<FaceletTaglibTagAttributeType>> onEventAttributes = new HashMap<String, List<FaceletTaglibTagAttributeType>>();
    
    private ImmutableList<String> globalAttributeNames = ImmutableList.of("binding", "id", "rendered");

    public FaceletTaglibType getTaglib() {
        return taglib;
    }

    public HashMap<String, FaceletTaglibTagType> getTagMap() {
        return tagMap;
    }

    public TaglibReader() {
        try {
            taglib = parseTablib();
        } catch (JAXBException e) {
            throw new FacesException(e);
        }
        tagMap = new HashMap<String, FaceletTaglibTagType>();
        for (Object tagOrFunction :taglib.getTagOrFunction()) {
            if (tagOrFunction instanceof FaceletTaglibTagType) {
                FaceletTaglibTagType tag = (FaceletTaglibTagType) tagOrFunction;
                tagMap.put(tag.getTagName().getValue(), tag);
            }
        }
    }

    FaceletTaglibType parseTablib() throws JAXBException {
        InputStream stream = getStream();
        JAXBContext jaxbContext = JAXBContext.newInstance(FaceletTaglibType.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        JAXBElement<FaceletTaglibType> element = (JAXBElement<FaceletTaglibType>) jaxbUnmarshaller.unmarshal(stream);
        FaceletTaglibType taglib = element.getValue();
        return taglib;
    }

    InputStream getStream() {
        return this.getClass().getResourceAsStream("/META-INF/bootstrap.taglib.xml");
    }
    
    public List<FaceletTaglibTagAttributeType> getDefaultAttributes(String tagName) {
        if(defaultAttributes.get(tagName) == null) {
            initAttributesForTagName(tagName);
        }
        return defaultAttributes.get(tagName);
    }
    
    public List<FaceletTaglibTagAttributeType> getGlobalAttributes(String tagName) {
        if(globalAttributes.get(tagName) == null) {
            initAttributesForTagName(tagName);
        }
        return globalAttributes.get(tagName);
    }
    
    public List<FaceletTaglibTagAttributeType> getOnEventAttributes(String tagName) {
        if(onEventAttributes.get(tagName) == null) {
            initAttributesForTagName(tagName);
        }
        return onEventAttributes.get(tagName);
    }
    
    private List<FaceletTaglibTagAttributeType> getAllAttributesFromTagName(String tagName) {
        return tagMap.get(tagName).getAttribute();
    }
    
    private void initAttributesForTagName(String tagName) {
        List<FaceletTaglibTagAttributeType> allAttributes = getAllAttributesFromTagName(tagName);
        
        List<FaceletTaglibTagAttributeType> defaultAttributesForTagName = new ArrayList<FaceletTaglibTagAttributeType>();
        List<FaceletTaglibTagAttributeType> globalAttributesForTagName = new ArrayList<FaceletTaglibTagAttributeType>();
        List<FaceletTaglibTagAttributeType> onEventAttributesForTagName = new ArrayList<FaceletTaglibTagAttributeType>();
        
        if(allAttributes != null) {
            for(FaceletTaglibTagAttributeType attribute : allAttributes) {
                String attributeName = attribute.getName().getValue();
                if(attributeName.startsWith("on")) {
                    onEventAttributesForTagName.add(attribute);
                } else if(globalAttributeNames.contains(attributeName)) {
                    globalAttributesForTagName.add(attribute);
                } else {
                    defaultAttributesForTagName.add(attribute);
                }
            }
            
            defaultAttributes.put(tagName, defaultAttributesForTagName);
            globalAttributes.put(tagName, globalAttributesForTagName);
            onEventAttributes.put(tagName, onEventAttributesForTagName);
        }
    }
    
    public String getMainType(String fullType) {
        int index = fullType.lastIndexOf(".");
        
        if(index > 1) {
            return fullType.substring(index+1);
        } else {
            return fullType;
        }
    }
}
