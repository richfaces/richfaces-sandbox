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
package org.richfaces.cdk.resource.util;

import static org.richfaces.cdk.strings.Constants.DOT_JOINER;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.richfaces.cdk.vfs.VFSRoot;
import org.richfaces.cdk.vfs.VirtualFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

/**
 * @author Nick Belaevski
 * 
 */
public final class ResourceUtil {

    private static final String CLASSPATH_RESOURCES_LOCATION = "META-INF/resources";

    private static final String WEB_RESOURCES_LOCATION = "resources";

    private static final Pattern LIBRARY_VERSION_PATTERN = Pattern.compile("^(\\d+)(_\\d+)+");
    
    private static final Pattern RESOURCE_VERSION_PATTERN = Pattern.compile("^((?:\\d+)(?:_\\d+)+)[\\.]?(\\w+)?");
    
    public static final class VersionKey {

        static final Ordering<VersionKey> ORDERING = Ordering.from(new Comparator<VersionKey>() {

            @Override
            public int compare(VersionKey o1, VersionKey o2) {
                return Ints.lexicographicalComparator().compare(o1.versionVector, o2.versionVector);
            }
        }).nullsFirst();
        
        private String version;
        
        private int[] versionVector;

        private String extension;
        
        public VersionKey(String version, String extension) throws NumberFormatException {
            this.version = version;
            this.versionVector = parseVersionString(version);
            this.extension = extension;
        }
        
        private static int[] parseVersionString(String s) {
            String[] split = s.split("_");
            int[] result = new int[split.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = Integer.parseInt(split[i]);
            }
            return result;
        }
       
        public String toString() {
            return DOT_JOINER.join(version, extension);
        }
    }
    
    private ResourceUtil() {}
    
    public static VirtualFile getLatestVersion(VirtualFile file, boolean library) {
        VersionKey latestVersionKey = null;
        
        Collection<VirtualFile> children = file.getChildren();
        for (VirtualFile child : children) {
            if (library && child.isDirectory()) {
                Matcher matcher = LIBRARY_VERSION_PATTERN.matcher(child.getName());
                if (matcher.matches()) {
                    latestVersionKey = VersionKey.ORDERING.max(latestVersionKey, new VersionKey(child.getName(), null));
                }
            } else if (!library && child.isFile()) {
                Matcher matcher = RESOURCE_VERSION_PATTERN.matcher(child.getName());
                if (matcher.matches()) {
                    latestVersionKey = VersionKey.ORDERING.max(latestVersionKey, new VersionKey(matcher.group(1), matcher.group(2)));
                }
            }
        }
        
        VirtualFile result;
        
        if (latestVersionKey != null) {
            result = file.getChild(latestVersionKey.toString());
        } else {
            result = file;
        }
        
        if (result != null && (library ^ result.isFile())) {
            return result;
        }
        
        return null;
    }
    
    private static Collection<VirtualFile> getExistingChildren(Iterable<VFSRoot> files, String path) throws URISyntaxException, IOException {
        Collection<VirtualFile> result = Lists.newArrayList();

        for (VirtualFile file: files) {
            VirtualFile child = file.getChild(path, true);
            if (child != null) {
                result.add(child);
            }
        }
        
        return result;
    }
    
    public static Collection<VirtualFile> getResourceRoots(Iterable<VFSRoot> cpRoots, Iterable<VFSRoot> webRoots) throws URISyntaxException, IOException {
        List<VirtualFile> result = Lists.newArrayList();
        
        result.addAll(getExistingChildren(cpRoots, CLASSPATH_RESOURCES_LOCATION));
        result.addAll(getExistingChildren(webRoots, WEB_RESOURCES_LOCATION));
        
        return result;
    }
}
