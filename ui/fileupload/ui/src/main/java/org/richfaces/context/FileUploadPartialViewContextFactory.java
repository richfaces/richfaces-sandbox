/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
package org.richfaces.context;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.request.MultipartRequest;

/**
 * @author Konstantin Mishin
 * 
 */
public class FileUploadPartialViewContextFactory extends PartialViewContextFactory {

    private static final Logger LOGGER = RichfacesLogger.CONTEXT.getLogger();

    private static final Pattern AMPERSAND = Pattern.compile("&+");

    private static final String UID_KEY = "rf_fu_uid";

    private PartialViewContextFactory parentFactory;

    /** Flag indicating whether a temporary file should be used to cache the uploaded file */
    private boolean createTempFiles = false;

    private String tempFilesDirectory;

    /** The maximum size of a file upload request. 0 means no limit. */
    private int maxRequestSize = 0;

    public FileUploadPartialViewContextFactory(PartialViewContextFactory parentFactory) {
        this.parentFactory = parentFactory;
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        String param = context.getInitParameter("createTempFiles");
        if (param != null) {
            this.createTempFiles = Boolean.parseBoolean(param);
        } else {
            this.createTempFiles = true;
        }

        this.tempFilesDirectory = context.getInitParameter("tempFilesDirectory");

        param = context.getInitParameter("maxRequestSize");
        if (param != null) {
            this.maxRequestSize = Integer.parseInt(param);
        }
    }

    @Override
    public PartialViewContext getPartialViewContext(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        Map<String, String> queryParamMap = parseQueryString(request.getQueryString());
        String uid = queryParamMap.get(UID_KEY);
        if (uid != null) {
            if (maxRequestSize != 0 && externalContext.getRequestContentLength() > maxRequestSize) {
                printResponse(facesContext, "<html id=\"" + UID_KEY + uid + ":size_restricted\"/>");
            } else if (!checkFileCount(externalContext, queryParamMap.get("id"))) {
                printResponse(facesContext, "<html id=\"" + UID_KEY + uid + ":forbidden\"/>");
            } else {
                MultipartRequest multipartRequest = new MultipartRequest(request, createTempFiles,
                    tempFilesDirectory, maxRequestSize, uid);
                try {
                    multipartRequest.parseRequest();
                    if (!multipartRequest.isDone()) {
                        printResponse(facesContext, "<html id=\"" + UID_KEY + uid + ":stopped\"/>");
                    } else {
                        externalContext.setRequest(multipartRequest);
                    }
                } catch (FileUploadException e) {
                    printResponse(facesContext, "<html id=\"" + UID_KEY + uid + ":server_error\"/>");
                    throw e; // TODO remove it
                } finally {
                    multipartRequest.clearRequestData();
                }
            }
        }
        return parentFactory.getPartialViewContext(facesContext);
    }

    private boolean checkFileCount(ExternalContext externalContext, String idParameter) {
        // TODO implement this method
        // HttpSession session = externalContext.getSession(false);
        //
        // if (session != null) {
        // Map<String, Integer> map = (Map<String, Integer>) session
        // .getAttribute(FileUploadConstants.UPLOADED_COUNTER);
        //
        // if (map != null) {
        // String id = idParameter;
        // if (id != null) {
        // Integer i = map.get(id);
        // if (i != null && i == 0) {
        // return false;
        // }
        // }
        // }
        // }
        return true;
    }

    private Map<String, String> parseQueryString(String queryString) {
        if (queryString != null) {
            Map<String, String> parameters = new HashMap<String, String>();
            String[] nvPairs = AMPERSAND.split(queryString);
            for (String nvPair : nvPairs) {
                if (nvPair.length() == 0) {
                    continue;
                }

                int eqIdx = nvPair.indexOf('=');
                if (eqIdx >= 0) {
                    try {
                        String name = URLDecoder.decode(nvPair.substring(0, eqIdx), "UTF-8");
                        if (!parameters.containsKey(name)) {
                            String value = URLDecoder.decode(nvPair.substring(eqIdx + 1), "UTF-8");

                            parameters.put(name, value);
                        }
                    } catch (UnsupportedEncodingException e) {
                        // log warning and skip this parameter
                        LOGGER.warn(e.getLocalizedMessage(), e);
                    }
                }
            }
            return parameters;
        } else {
            return Collections.emptyMap();
        }
    }

    private void printResponse(FacesContext facesContext, String message) {
        facesContext.responseComplete();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseStatus(HttpServletResponse.SC_OK);
        externalContext.setResponseContentType(MultipartRequest.TEXT_HTML);
        try {
            Writer writer = externalContext.getResponseOutputWriter();
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
