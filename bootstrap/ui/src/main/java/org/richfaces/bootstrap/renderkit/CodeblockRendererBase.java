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
package org.richfaces.bootstrap.renderkit;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.richfaces.renderkit.RendererBase;

/**
 * Base class for the codeblock renderer
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
@ResourceDependencies({
        @ResourceDependency(library = "org.richfaces", name = "ajax.reslib"),
        @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
        @ResourceDependency(library = "bootstrap/css", name = "bootstrap.css"),
        @ResourceDependency(library = "com.googlecode/prettify", name = "prettify.css"),
        @ResourceDependency(library = "com.googlecode/prettify", name = "prettify.js"),
        @ResourceDependency(library = "org.richfaces", name = "codeblock.js")})
public abstract class CodeblockRendererBase extends RendererBase {
    public static final String RENDERER_TYPE = "org.richfaces.bootstrap.CodeblockRenderer";
}
