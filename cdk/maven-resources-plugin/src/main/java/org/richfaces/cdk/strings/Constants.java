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
package org.richfaces.cdk.strings;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * @author Nick Belaevski
 * 
 */
public final class Constants {

    public static final Joiner SLASH_JOINER = Joiner.on('/').skipNulls();

    public static final Splitter SLASH_SPLITTER = Splitter.on('/').omitEmptyStrings();
    
    public static final Joiner DOT_JOINER = Joiner.on('.').skipNulls();

    public static final Joiner DASH_JOINER = Joiner.on('-').skipNulls();

    public static final Joiner COLON_JOINER = Joiner.on(':').skipNulls();

    private Constants() {
    }

}
