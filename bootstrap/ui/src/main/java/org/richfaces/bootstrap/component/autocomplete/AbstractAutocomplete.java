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
package org.richfaces.bootstrap.component.autocomplete;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.JsfComponent;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.cdk.annotations.Tag;

/**
 * Base class for the autocomplete component
 * 
 * @author Lukas Fryc
 */
@JsfComponent(
        type = AbstractAutocomplete.COMPONENT_TYPE,
        family = AbstractAutocomplete.COMPONENT_FAMILY,
        renderer = @JsfRenderer(type = AutocompleteRendererBase.RENDERER_TYPE),
        tag = @Tag(name="autocomplete"))
public abstract class AbstractAutocomplete extends javax.faces.component.UIOutput {
    public static final String COMPONENT_FAMILY = "org.richfaces.bootstrap.Autocomplete";
    public static final String COMPONENT_TYPE = "org.richfaces.bootstrap.Autocomplete";
    
    /**
     * A collection of suggestions that will be resented to the user
     */
    @Attribute(required = true)
    public abstract Object getSuggestions();
    
    /**
     * Allow a user to enter multiple values separated by specific characters. As the user types, a suggestion will present as
     * normal. When they enter the specified token character, this begins a new suggestion process, and the component will then
     * only use text entered after the token character for suggestions
     */
    @Attribute
    public abstract String getToken();
}
