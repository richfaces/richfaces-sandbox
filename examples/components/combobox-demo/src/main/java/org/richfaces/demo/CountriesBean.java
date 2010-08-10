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

package org.richfaces.demo;

import java.net.URL;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author nick
 *
 */
@ManagedBean(eager = true)
@ApplicationScoped
public class CountriesBean {

    private List<Country> countries;

    @XmlRootElement(name = "countries", namespace = Country.NAMESPACE)
    private static class Countries {

        @XmlElement(name = "country", namespace = Country.NAMESPACE)
        private List<Country> countries;

        public List<Country> getCountries() {
            return countries;
        }
    }

    public CountriesBean() {
    }

    @PostConstruct
    public void initialize() {
        try {
            JAXBContext countryContext = JAXBContext.newInstance(Countries.class);
            Unmarshaller unmarshaller = countryContext.createUnmarshaller();

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL dataUrl = classLoader.getResource("org/richfaces/demo/countries.xml");

            countries = ((Countries) unmarshaller.unmarshal(dataUrl)).getCountries();
        } catch (JAXBException e) {
            throw new FacesException(e.getMessage(), e);
        }
    }

    public List<Country> getCountries() {
        return countries;
    }
}
