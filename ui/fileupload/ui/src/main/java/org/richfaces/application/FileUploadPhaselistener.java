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
package org.richfaces.application;

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
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
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
public class FileUploadPhaselistener implements PhaseListener {

    private static final long serialVersionUID = 138000954953175986L;

    /** Multipart request start */
    private static final String MULTIPART = "multipart/";

    private static final Pattern AMPERSAND = Pattern.compile("&+");
    
    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();
    
    /** Flag indicating whether a temporary file should be used to cache the uploaded file */
    private boolean createTempFiles = false;

    private String tempFilesDirectory;
    
    /** The maximum size of a file upload request. 0 means no limit. */
    private int maxRequestSize = 0;

    public FileUploadPhaselistener() {
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

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        Map<String, String> queryParamMap = parseQueryString(request.getQueryString());
        String uid = queryParamMap.get(MultipartRequest.UPLOAD_FILES_ID);
        if (uid != null && isMultipartRequest(request)) {
            if (maxRequestSize != 0 && request.getContentLength() > maxRequestSize) {
                boolean sendError = Boolean.parseBoolean(queryParamMap.get(MultipartRequest.SEND_HTTP_ERROR));
                if (sendError) {
                    printResponse(facesContext, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, null);
                } else {
                    printResponse(facesContext, HttpServletResponse.SC_OK,
                        "<html id=\"_richfaces_file_upload_size_restricted\"></html>");
                }
            } else if (!checkFileCount(request, queryParamMap.get("id"))) {
                printResponse(facesContext, HttpServletResponse.SC_OK,
                    "<html id=\"_richfaces_file_upload_forbidden\"></html>");
            } else {
                MultipartRequest multipartRequest = new MultipartRequest(request, createTempFiles,
                    tempFilesDirectory, maxRequestSize, uid);
                try {
                    multipartRequest.parseRequest();
                    if (!multipartRequest.isDone()) {
                        printResponse(facesContext, HttpServletResponse.SC_OK,
                            "<html id=\"_richfaces_file_upload_stopped\"></html>");
                    }
                } catch (FileUploadException e) {
                    printResponse(facesContext, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
                    throw e; //TODO remove it
                } finally {
                    multipartRequest.clearRequestData();
                }
            }
        }
    }

    private boolean checkFileCount(HttpServletRequest request, String idParameter) {
        //TODO implement this method
//        HttpSession session = request.getSession(false);
//        
//        if (session != null) {
//            Map<String, Integer> map = (Map<String, Integer>) session
//        .getAttribute(FileUploadConstants.UPLOADED_COUNTER);
//
//            if (map != null) {
//                String id = idParameter;
//                if (id != null) {
//                    Integer i = map.get(id);
//                    if (i != null && i == 0) {
//                        return false;
//                    }
//                }
//            }
//        }
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
                        //log warning and skip this parameter
                        LOGGER.warn(e.getLocalizedMessage(), e);
                    }
                }
            }
            return parameters;
        } else {
            return Collections.emptyMap();
        }
    }

    private boolean isMultipartRequest(HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }

        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }

        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }

        return false;
    }

    private void printResponse(FacesContext facesContext, int statusCode, String message) {
        facesContext.responseComplete();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseStatus(statusCode);
        if (statusCode == HttpServletResponse.SC_OK) {
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

    /*
     * (non-Javadoc)
     * 
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
