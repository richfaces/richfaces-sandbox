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

import java.text.MessageFormat;

import org.apache.maven.plugin.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.google.common.base.Strings;

/**
 * @author Nick Belaevski
 * 
 */
final class MavenLogErrorReporter implements ErrorReporter {
    
    private String resourceName;
    
    private Log log;
    
    public MavenLogErrorReporter(Log log, String resourceName) {
        super();
        this.log = log;
        this.resourceName = resourceName;
    }

    private String formatMessage(String message, String sourceName, int line, String lineSource, int lineOffset) {
        String location = MessageFormat.format("{0} (line {1}, col {2})", 
            Strings.isNullOrEmpty(sourceName) ? resourceName : sourceName, lineSource, lineOffset);
        
        return MessageFormat.format("{0}: {1}\n{2}", location, message, lineSource);
    }
    
    @Override
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        log.warn(formatMessage(message, sourceName, line, lineSource, lineOffset));
    }

    @Override
    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
        return new EvaluatorException(message, sourceName, line, lineSource, lineOffset); 
    }

    @Override
    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        log.error(formatMessage(message, sourceName, line, lineSource, lineOffset));
    }
}