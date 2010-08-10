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

import javax.xml.bind.annotation.XmlElement;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * @author nick
 *
 */
public class Country {

    public static final String NAMESPACE = "http://richfaces.org/demos/countries";

    private String name;

    private String iso;

    private String domain;

    @XmlElement(namespace = Country.NAMESPACE)
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIso() {
        return iso;
    }

    @XmlElement(namespace = Country.NAMESPACE)
    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getDomain() {
        return domain;
    }

    @XmlElement(namespace = Country.NAMESPACE)
    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        ToStringHelper helper = Objects.toStringHelper(this);

        helper.add("name", name).add("iso", iso).add("domain", domain);

        return helper.toString();
    }
}
