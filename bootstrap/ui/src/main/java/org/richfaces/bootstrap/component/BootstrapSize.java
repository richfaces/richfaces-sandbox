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
    
    private BootstrapSize(String buttonStyleClass, String inputStyleClass) {
        if(buttonStyleClass == null) {
            this.buttonStyleClass = "";
        } else if(buttonStyleClass.equals("")) {
            this.buttonStyleClass = BootstrapSeverity.BUTTON_PREFIX + this.toString();
        } else {
            this.buttonStyleClass = BootstrapSeverity.BUTTON_PREFIX + buttonStyleClass;
        }
        
        if(inputStyleClass == null) {
            this.inputStyleClass = "";
        } else if(inputStyleClass.equals("")) {
            this.inputStyleClass = "input-" + this.toString();
        } else {
            this.inputStyleClass = "input-" + inputStyleClass;
        }
    }
    
    private String buttonStyleClass;
    private String inputStyleClass;
    
    public String getButtonStyleClass() {
        return buttonStyleClass;
    }
    public String getInputStyleClass() {
        return inputStyleClass;
    }
}
