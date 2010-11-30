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
package org.richfaces.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

/**
 * @author Konstantin Mishin
 * 
 */
@ManagedBean
@SessionScoped
public class FileUploadBean {
    
    private String acceptedTypes;
    private boolean enabled = true;
    private boolean noDuplicate = false;
    private UploadItem item;
    
    public UploadItem getItem() {
        return item;
    }

//    public void paint(OutputStream stream, Object object) throws IOException {
//        stream.write(item.getData());
//    }
    
    public void listener(UploadEvent event) throws Exception {
        item = event.getUploadItem();
        if (item != null) {
            item.getFile().delete();
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setNoDuplicate(boolean noDuplicate) {
        this.noDuplicate = noDuplicate;
    }

    public boolean isNoDuplicate() {
        return noDuplicate;
    }

    public void setAcceptedTypes(String acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    public String getAcceptedTypes() {
        return acceptedTypes;
    }  

}
