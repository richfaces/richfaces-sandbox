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



package org.richfaces.cdk.rd;

import java.io.IOException;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;

import org.codehaus.plexus.util.SelectorUtils;

/**
 * @author Anton Belevich
 *
 */
public class JarResourceScanner {
    private String[] patterns = new String[] {"**"};
    private Set<FileObject> result = new HashSet<FileObject>();
    private FileObject baseFile;

    public String[] getPatterns() {
        return patterns;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    public FileObject getBaseFile() {
        return baseFile;
    }

    public void setBaseFile(FileObject baseFile) {
        this.baseFile = baseFile;
    }

    protected boolean isAcceptable(FileObject fileObject) {
        for (int i = 0; i < patterns.length; i++) {
            if (SelectorUtils.matchPath(patterns[i], fileObject.getName().getURI())) {
                return true;
            }
        }

        return false;
    }

    protected void walk(FileObject file) throws IOException {
        FileObject[] children = file.getChildren();

        for (FileObject child : children) {
            if (child.getType() != FileType.FILE) {
                walk(child);
            } else if (isAcceptable(child)) {
                result.add(child);
            }
        }
    }

    public void doScan() throws IOException {
        if (baseFile != null && baseFile.exists()) {
            walk(baseFile);
        }
    }

    public Set<FileObject> getResult() {
        return result;
    }

    public void setResult(Set<FileObject> result) {
        this.result = result;
    }
}
