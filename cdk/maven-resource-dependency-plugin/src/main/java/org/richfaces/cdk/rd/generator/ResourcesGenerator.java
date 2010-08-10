/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.cdk.rd.generator;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.Method;

import java.net.URL;

import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Anton Belevich
 *
 */
public class ResourcesGenerator {
    private ResourceAssembler assembler;
    private File assemblyFile;
    private List<String> includesAfter;
    private List<String> includesBefore;
    private Log log;
    private Collection<String> resources;

    public ResourcesGenerator(Log log) {
        this.log = log;
    }

    public void doAssembly() {
        if (resources != null) {
            if (includesBefore != null && !includesBefore.isEmpty()) {
                iterate(includesBefore);
            }

            if (resources != null && !resources.isEmpty()) {
                iterate(resources);
            }

            if (includesAfter != null && !includesAfter.isEmpty()) {
                iterate(includesAfter);
            }
        }
    }

    private void iterate(Collection<String> resources) {
        for (String resourceName : resources) {
            URL resource = getResourceURL(resourceName);

            log.info("concatenate resource: " + resource);

            if (resource != null) {
                if (assembler != null) {
                    assembler.assembly(resource);
                }
            }
        }
    }

    private URL getResourceURL(String resourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(resourceName);

        try {
            if (resource == null) {

                // resolve framework script path
                Class clazz = classLoader.loadClass(resourceName);
                Object obj = clazz.newInstance();
                Method method = clazz.getMethod("getPath", new Class[0]);
                String path = (String) method.invoke(obj, new Object[0]);

                resource = classLoader.getResource(path);
            }
        } catch (Exception e) {
            log.error("Error process: " + resourceName + "\n" + e.getMessage(), e);
        }

        return resource;
    }

    public void writeToFile() {
        if (assemblyFile != null) {
            if (assemblyFile.exists()) {
                assemblyFile.delete();
            }

            try {
                assemblyFile.createNewFile();
            } catch (IOException e) {
                log.error("Error create assembly File: " + assemblyFile.getPath(), e);
            }

            assembler.writeToFile(assemblyFile);
        }
    }

    public File getAssemblyFile() {
        return assemblyFile;
    }

    public void setAssemblyFile(File assemblyFile) {
        this.assemblyFile = assemblyFile;
    }

    public Collection<String> getResources() {
        return resources;
    }

    public void setResources(Collection<String> resources) {
        this.resources = resources;
    }

    public List<String> getIncludesBefore() {
        return includesBefore;
    }

    public void setIncludesBefore(List<String> includesBefore) {
        this.includesBefore = includesBefore;
    }

    public List<String> getIncludesAfter() {
        return includesAfter;
    }

    public void setIncludesAfter(List<String> includesAfter) {
        this.includesAfter = includesAfter;
    }

    public ResourceAssembler getAssembler() {
        return assembler;
    }

    public void setAssembler(ResourceAssembler assembler) {
        this.assembler = assembler;
    }
}
