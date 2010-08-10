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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton Belevich
 *
 */
public class Component implements Comparable<Component> {
    private List<String> scripts = new ArrayList<String>();
    private List<String> styles = new ArrayList<String>();
    private String componentName;

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public void addScript(String script) {
        scripts.add(script);
    }

    public void addStyle(String style) {
        styles.add(style);
    }

    public List<String> getStyles() {
        return this.styles;
    }

    public List<String> getScripts() {
        return this.scripts;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((componentName == null) ? 0 : componentName.hashCode());
        result = prime * result + ((scripts == null) ? 0 : scripts.hashCode());
        result = prime * result + ((styles == null) ? 0 : styles.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Component other = (Component) obj;

        if (componentName == null) {
            if (other.componentName != null) {
                return false;
            }
        } else if (!componentName.equals(other.componentName)) {
            return false;
        }

        if (scripts == null) {
            if (other.scripts != null) {
                return false;
            }
        } else if (!scripts.equals(other.scripts)) {
            return false;
        }

        if (styles == null) {
            if (other.styles != null) {
                return false;
            }
        } else if (!styles.equals(other.styles)) {
            return false;
        }

        return true;
    }

    public int compareTo(Component o) {
        return componentName.compareToIgnoreCase(o.getComponentName());
    }

    @Override
    public String toString() {
        return "Component: " + componentName;
    }
}
