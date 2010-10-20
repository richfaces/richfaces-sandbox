/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.request;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.faces.context.FacesContext;

final class MultipartRequestRegistry {

    private static final String REGISTRY_ATTRIBUTE_NAME = MultipartRequestRegistry.class.getName();

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private String registryId = UUID.randomUUID().toString();

    private Map<String, MultipartRequest> requestsMap = new ConcurrentHashMap<String, MultipartRequest>();

    private MultipartRequestRegistry() {

    }

    public static MultipartRequestRegistry getInstance(FacesContext context) {
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
        // MultipartRequestRegistry requestRegistry = (MultipartRequestRegistry) applicationMap
        // .get(REGISTRY_ATTRIBUTE_NAME);
        // if (requestRegistry == null) {
        synchronized (applicationMap) {
            MultipartRequestRegistry requestRegistry = (MultipartRequestRegistry) applicationMap
                .get(REGISTRY_ATTRIBUTE_NAME);
            if (requestRegistry == null) {
                requestRegistry = new MultipartRequestRegistry();
                applicationMap.put(REGISTRY_ATTRIBUTE_NAME, requestRegistry);
            }
            // }
            return requestRegistry;
        }
    }

    public String registerRequest(MultipartRequest request) {
        String key = registryId + ":" + atomicInteger.incrementAndGet();
        requestsMap.put(key, request);

        return key;
    }

    public void removeRequest(String key) {
        requestsMap.remove(key);
    }

    public MultipartRequest getRequest(String key) {
        return requestsMap.get(key);
    }
}
