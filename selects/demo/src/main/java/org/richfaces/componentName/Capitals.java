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
package org.richfaces.componentName;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ApplicationScoped
@Named
public class Capitals implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<Integer, Capital> map;
    private final List<Capital> list;


    public Capitals() {
        List<Capital> arrayList = new ArrayList<Capital>();
        Map<Integer, Capital> hashMap = new HashMap<Integer, Capital>(arrayList.size(), 1);

        arrayList.add(new Capital(0, "Vancouver", "BC"));
        arrayList.add(new Capital(1, "Edmonton", "AB"));
        arrayList.add(new Capital(2, "Regina", "SK"));
        arrayList.add(new Capital(3, "Winnipeg", "MB"));
        arrayList.add(new Capital(4, "Toronto", "ON"));
        arrayList.add(new Capital(5, "Quebec", "QC"));
        for (Capital capital : arrayList) {
            hashMap.put(capital.getId(), capital);
        }

        list = Collections.unmodifiableList(arrayList);
        map = Collections.unmodifiableMap(hashMap);
    }

    public List<Capital> getList() {
        return list;
    }

    public Map<Integer, Capital> getMap() {
        return map;
    }
}
