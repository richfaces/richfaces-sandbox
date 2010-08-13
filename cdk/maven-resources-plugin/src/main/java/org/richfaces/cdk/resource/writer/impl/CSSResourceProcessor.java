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
package org.richfaces.cdk.resource.writer.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.yahoo.platform.yui.compressor.CssCompressor;

/**
 * @author Nick Belaevski
 * 
 */
public class CSSResourceProcessor extends CharResourceProcessor {

    @Override
    public boolean isSupportedFile(String name) {
        return name.endsWith(".css");
    }

    @Override
    protected void doActualProcess(String resourceName, Reader in, Writer out) throws IOException {
        new CssCompressor(in).compress(out, 0);
    }
    
}
