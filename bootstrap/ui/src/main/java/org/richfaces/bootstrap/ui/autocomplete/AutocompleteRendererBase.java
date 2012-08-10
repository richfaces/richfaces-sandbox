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
package org.richfaces.bootstrap.ui.autocomplete;

import java.util.Arrays;
import java.util.Collection;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.richfaces.json.JSONArray;
import org.richfaces.renderkit.RendererBase;

/**
 * Base class for the alert renderer
 * 
 * @author Lukas Fryc
 * 
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-css.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "bootstrap-js.reslib"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.core.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.widget.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.position.js"),
        @ResourceDependency(library = "com.jqueryui", name = "js/jquery.ui.autocomplete.js"),
        @ResourceDependency(library = "org.richfaces", name = "bridge/autocomplete.js")})
public abstract class AutocompleteRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.AutocompleteRenderer";
    
    /**
     * Returns the client ID of component which will be suggestions provided for
     */
    public String getInputId(FacesContext facesContext, AbstractAutocomplete autocomplete) {
        if (autocomplete.getParent() instanceof UIInput) {
            return autocomplete.getParent().getClientId(facesContext);
        } else {
            throw new IllegalStateException("autocomplete component must be nested in " + UIInput.class.getName());
        }
    }
    
    /**
     * Returns JSON array of available suggestions as string
     */
    public JSONArray getSuggestions(FacesContext facesContext, AbstractAutocomplete autocomplete) {
        Object suggestions = autocomplete.getSuggestions();
        Collection<?> collection;
        
        if (suggestions == null) {
            throw new IllegalArgumentException("suggestions attribute is required for autocomplete component");
        }
        
        if (suggestions.getClass().isArray()) {
            collection = Arrays.asList((Object[]) suggestions);
        } else if (suggestions instanceof Collection) {
            collection = (Collection<?>) suggestions;
        } else {
            throw new IllegalArgumentException("Unhandled type (" + suggestions.getClass() + ") of suggestions attribute for autocomplete component");
        }
        
        return new JSONArray(collection);
    }
}
