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

import org.richfaces.bootstrap.demo.jaxb.javaee.FaceletTaglibTagType;
import org.richfaces.bootstrap.demo.jaxb.javaee.FaceletTaglibType;

import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ManagedBean
@ApplicationScoped
public class TaglibReader {
    private FaceletTaglibType taglib;
    private List<FaceletTaglibTagType> tags;
    private HashMap<String, FaceletTaglibTagType> tagMap;

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
}
