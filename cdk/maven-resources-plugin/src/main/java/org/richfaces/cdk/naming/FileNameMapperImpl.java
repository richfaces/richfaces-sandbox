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
package org.richfaces.cdk.naming;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.richfaces.cdk.FileNameMapper;

import com.google.common.collect.Lists;

/**
 * @author Nick Belaevski
 * 
 */
public class FileNameMapperImpl implements FileNameMapper {

    private static final class Mapping {

        private Pattern pattern;

        private String replacement;

        public Mapping(Pattern pattern, String replacement) {
            super();
            this.pattern = pattern;
            this.replacement = replacement;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public String getReplacement() {
            return replacement;
        }
    }

    private List<Mapping> fileNameMappings;

    public FileNameMapperImpl(Map<String, String> fileNameMappings) {
        super();
        this.fileNameMappings = compileMappings(fileNameMappings);
    }

    private static List<Mapping> compileMappings(Map<String, String> mappings) {
        List<Mapping> result = Lists.newArrayList();

        for (Entry<String, String> entry: mappings.entrySet()) {
            Pattern pattern = Pattern.compile((String) entry.getKey());
            String replacement = entry.getValue();

            result.add(new Mapping(pattern, replacement));
        }

        return result;
    }

    @Override
    public String createName(String resourcePath) {
        if (resourcePath == null) {
            return resourcePath;
        }

        for (Mapping mapping : fileNameMappings) {
            Matcher matcher = mapping.getPattern().matcher(resourcePath);
            if (matcher.find()) {
                return matcher.replaceAll(mapping.getReplacement());
            }
        }

        return resourcePath;
    }

}
