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

/**
 * Enum containing the different size supported by Bootstrap
 * 
 * @author <a href="http://www.pauldijou.fr">Paul Dijou</a>
 *
 */
public enum BootstrapSize {
    mini("", ""),
    small("", ""),
    medium(null, ""),
    large("", ""),
    xlarge(null, ""),
    xxlarge(null, "");
    
    private BootstrapSize(String buttonSizeClass, String inputSizeClass) {
        if(buttonSizeClass == null) {
            this.buttonSizeClass = "";
        } else if(buttonSizeClass.equals("")) {
            this.buttonSizeClass = BootstrapSeverity.BUTTON_PREFIX + this.toString();
        } else {
            this.buttonSizeClass = BootstrapSeverity.BUTTON_PREFIX + buttonSizeClass;
        }
        
        if(inputSizeClass == null) {
            this.inputSizeClass = "";
        } else if(inputSizeClass.equals("")) {
            this.inputSizeClass = "input-" + this.toString();
        } else {
            this.inputSizeClass = "input-" + inputSizeClass;
        }
    }
    
    private String buttonSizeClass;
    private String inputSizeClass;
    
    public String getButtonSizeClass() {
        return buttonSizeClass;
    }
    public String getInputSizeClass() {
        return inputSizeClass;
    }
}
