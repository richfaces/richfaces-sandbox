/**
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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
package org.richfaces.bootstrap.component;

import java.util.ArrayList;
import java.util.List;

public enum BootstrapSeverity {
    primary(null, null, null, null, null),
    success(null, null, null, null, null),
    info(null, null, null, null, null),
    warning(null, null, null, null, null),
    error("danger", null, "important", null, "danger"),
    inverse(null, null, null, null, null);

    private BootstrapSeverity(String buttonClass, String alertClass, String labelClass, String badgeClass, String progressClass) {
        if(buttonClass == null) {
            this.buttonClass = BUTTON_PREFIX + this.toString();
        } else {
            this.buttonClass = BUTTON_PREFIX + buttonClass;
        }
        
        if(alertClass == null) {
            this.alertClass = ALERT_PREFIX + this.toString();
        } else {
            this.alertClass = ALERT_PREFIX + alertClass;
        }
        
        if(labelClass == null) {
            this.labelClass = LABEL_PREFIX + this.toString();
        } else {
            this.labelClass = LABEL_PREFIX + labelClass;
        }
        
        if(badgeClass == null) {
            this.badgeClass = BADGE_PREFIX + this.toString();
        } else {
            this.badgeClass = BADGE_PREFIX + badgeClass;
        }
        
        if(progressClass == null) {
            this.progressClass = PROGRESS_PREFIX + this.toString();
        } else {
            this.progressClass = PROGRESS_PREFIX + progressClass;
        }
    }
    
    public static final String BUTTON_CLASS = "btn";
    public static final String ALERT_CLASS = "alert";
    public static final String LABEL_CLASS = "label";
    public static final String BADGE_CLASS = "badge";
    public static final String PROGRESS_CLASS = "progress";
    
    public static final String BUTTON_PREFIX = "btn-";
    public static final String ALERT_PREFIX = "alert-";
    public static final String LABEL_PREFIX = "label-";
    public static final String BADGE_PREFIX = "badge-";
    public static final String PROGRESS_PREFIX = "progress-";

    private String buttonClass;
    private String alertClass;
    private String labelClass;
    private String badgeClass;
    private String progressClass;
    
    public static List<String> getValues() {
        List<String> values = new ArrayList<String>();
        
        for(BootstrapSeverity severity : BootstrapSeverity.values()) {
            values.add(severity.toString());
        }
        
        return values;
    }
    
    public String getButtonClass() {
        return buttonClass;
    }
    
    public String getFullButtonClass() {
        return BUTTON_CLASS + " " + getButtonClass();
    }
    
    public String getAlertClass() {
        return alertClass;
    }
    
    public String getFullAlertClass() {
        return ALERT_CLASS + " " + getAlertClass();
    }
    
    public String getLabelClass() {
        return labelClass;
    }
    
    public String getFullLabelClass() {
        return LABEL_CLASS + " " + getLabelClass();
    }
    
    public String getBadgeClass() {
        return badgeClass;
    }
    
    public String getFullBadgeClass() {
        return BADGE_CLASS + " " + getBadgeClass();
    }
    
    public String getProgressClass() {
        return progressClass;
    }
    
    public String getFullProgressClass() {
        return PROGRESS_CLASS + " " + getProgressClass();
    }
}
