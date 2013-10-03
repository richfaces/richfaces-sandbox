/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

package org.richfaces.componentName;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Model
public class CapitalsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    private Capitals capitals;
    private List<Capital> capitalList;

    public CapitalsBean() {
    }

    @PostConstruct
    public void init() {
        capitalList = new ArrayList<Capital>();
        for (Capital capital : capitals.getList()) {
            if (capital.getId() % 2 == 0) {
                capitalList.add(capital);
            }
        }
    }

    public List<Capital> getCapitalList() {
        return capitalList;
    }

    public void setCapitalList(List<Capital> capitalList) {
        this.capitalList = capitalList;
    }

    public List<Capital> getCapitalsAvailable() {
        return capitals.getList();
    }
}
