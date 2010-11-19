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
package org.richfaces.request;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.richfaces.exception.FileUploadException;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;
import org.richfaces.model.UploadItem;
import org.richfaces.request.ByteSequenceMatcher.BytesHandler;

public class MultipartRequest extends HttpServletRequestWrapper {

    public static final String TEXT_HTML = "text/html";

    private static final BytesHandler NOOP_HANDLER = new BytesHandler() {
        public void handle(byte[] bytes, int length) {
            // do nothing
        }
    };

    /** Session bean name where request size will be stored */
    private static final String REQUEST_SIZE_BEAN_NAME = "_richfaces_request_size";

    /** Session bean name where progress bar's percent map will be stored */
    private static final String PERCENT_BEAN_NAME = "_richfaces_upload_percents";

    private static final String PARAM_NAME = "name";
    private static final String PARAM_FILENAME = "filename";
    private static final String PARAM_CONTENT_TYPE = "Content-Type";

    private static final int BUFFER_SIZE = 2048;
    private static final int CHUNK_SIZE = 1024;
    private static final int MAX_HEADER_SIZE = 32768;

    private static final Logger LOGGER = RichfacesLogger.APPLICATION.getLogger();

    private static final byte CR = 0x0d;
    private static final byte LF = 0x0a;
    private static final byte[] CR_LF = { CR, LF };
    private static final byte[] HYPHENS = { 0x2d, 0x2d }; // '--'

    private static final Pattern PARAM_VALUE_PATTERN = Pattern.compile("^\\s*([^\\s=]+)\\s*[=:]\\s*(.+)\\s*$");

    private static final Pattern FILE_NAME_PATTERN = Pattern.compile(".*filename=\"(.*)\"");

    private boolean createTempFiles;

    private String tempFilesDirectory;

    private String uid;

    private String encoding = null;

    private Integer contentLength = 0;

    private int bytesRead = 0;

    // we shouldn't allow to stop until request reaches PhaseListener because of portlets
    private volatile boolean canStop = false;

    private Map<String, Param> parameters = null;

    private Map<String, Object> percentMap = null;

    private Map<String, Integer> requestSizeMap = null;

    private List<String> keys = new ArrayList<String>();

    private byte[] boundaryMarker;

    private ByteSequenceMatcher sequenceMatcher;

    private boolean shouldStop = false;
    private boolean canceled;

    private boolean initialized = false;

    private HeadersHandler headersHandler = null;

    public MultipartRequest(HttpServletRequest request, boolean createTempFiles, String tempFilesDirectory,
        int maxRequestSize, String uid) {
        super(request);
        this.createTempFiles = createTempFiles;
        this.tempFilesDirectory = tempFilesDirectory;
        this.uid = uid;

        String contentLengthStr = request.getHeader("Content-Length");
        this.contentLength = Integer.parseInt(contentLengthStr);
        if (contentLengthStr != null && maxRequestSize > 0 && contentLength > maxRequestSize) {
            // TODO : we should make decision if can generate exception in this
            // place
            // throw new FileUploadException(
            // "Multipart request is larger than allowed size");
        }
    }

    private class ControlledProgressInputStream extends FilterInputStream {

        protected ControlledProgressInputStream(InputStream in) {
            super(in);
        }

        @Override
        public int read() throws IOException {
            int read = super.read();
            if (read >= 0) {
                bytesRead++;
                fillProgressInfo();
            }
            return read;
        }

        @Override
        public int read(byte[] b) throws IOException {
            int read = super.read(b);
            if (read > 0) {
                bytesRead += read;
                fillProgressInfo();
            }
            return read;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            int read = super.read(b, off, len);
            if (read > 0) {
                bytesRead += read;
                fillProgressInfo();
            }
            return read;
        }
    }

    private String decodeFileName(String name) {
        String fileName = null;
        try {
            StringBuffer buffer = new StringBuffer();
            String[] codes = name.split(";");
            if (codes != null) {
                for (String code : codes) {
                    if (code.startsWith("&")) {
                        String sCode = code.replaceAll("[&#]*", "");
                        Integer iCode = Integer.parseInt(sCode);
                        buffer.append(Character.toChars(iCode));
                    } else {
                        buffer.append(code);
                    }
                }
                fileName = buffer.toString();
            }
        } catch (Exception e) {
            fileName = name;
        }

        return fileName;
    }

    public void cancel() {
        this.canceled = true;

        if (parameters != null) {
            Iterator<Param> it = parameters.values().iterator();
            while (it.hasNext()) {
                Param p = it.next();
                if (p instanceof FileParam) {
                    ((FileParam) p).deleteFile();
                }
            }
        }
    }

    private void readNext() throws IOException {
        Param p = readHeader();
        if (p != null) {
            try {
                readData(p);
            } finally {
                try {
                    p.complete();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    private Param createParam(Map<String, String> headers) {
        Param param = null;
        String paramName = headers.get(PARAM_NAME);
        if (paramName != null) {
            if (headers.containsKey(PARAM_FILENAME)) {
                FileParam fp = new FileParam(paramName);
                this.keys.add(paramName);

                if (createTempFiles) {
                    fp.createTempFile(tempFilesDirectory);
                }
                fp.setContentType(headers.get(PARAM_CONTENT_TYPE));
                fp.setFilename(decodeFileName(headers.get(PARAM_FILENAME)));
                param = fp;
            } else {
                if (parameters.containsKey(paramName)) {
                    param = parameters.get(paramName);
                } else {
                    param = new ValueParam(paramName, encoding);
                }
            }

            if (!parameters.containsKey(paramName)) {
                parameters.put(paramName, param);
            }
        }

        return param;
    }

    private class HeadersHandler implements BytesHandler {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER_SIZE);

        public void handle(byte[] bytes, int length) throws IOException {
            if (length != 0) {
                if (baos.size() + length > MAX_HEADER_SIZE) {
                    throw new IOException("Header section is too big");
                }

                baos.write(bytes, 0, length);
            }
        }

        public boolean dataEquals(byte[] bytes) {
            return (baos.size() == bytes.length) && Arrays.equals(HYPHENS, baos.toByteArray());
        }

        public String asString() throws UnsupportedEncodingException {
            if (encoding != null) {
                return baos.toString(encoding);
            } else {
                return baos.toString();
            }
        }

        public void reset() {
            baos.reset();
        }

    }

    private Param readHeader() throws IOException {
        if (sequenceMatcher.isEOF()) {
            return null;
        }

        if (headersHandler == null) {
            headersHandler = new HeadersHandler();
        } else {
            headersHandler.reset();
        }

        sequenceMatcher.setBytesHandler(headersHandler);
        sequenceMatcher.findSequence(-1, CR_LF);

        if (sequenceMatcher.isMatchedAndNotEOF() && !headersHandler.dataEquals(HYPHENS)) {
            headersHandler.reset();

            sequenceMatcher.findSequence(-1, CR_LF, CR_LF);

            if (!sequenceMatcher.isMatchedAndNotEOF()) {
                throw new IOException("Request header cannot be read");
            }

            String headersString = headersHandler.asString();
            Map<String, String> headers = new HashMap<String, String>();
            String[] split = headersString.split("\r\n");
            for (String headerString : split) {
                parseParams(headerString, "; ", headers);
            }

            return createParam(headers);
        }

        return null;
    }

    private void readProlog() throws IOException {
        sequenceMatcher.setBytesHandler(NOOP_HANDLER);
        sequenceMatcher.findSequence(-1, HYPHENS, boundaryMarker);
        if (!sequenceMatcher.isMatchedAndNotEOF()) {
            throw new IOException("Request prolog cannot be read");
        }
    }

    private void readData(final Param param) throws IOException {
        sequenceMatcher.setBytesHandler(param);
        sequenceMatcher.findSequence(CHUNK_SIZE, CR_LF, HYPHENS, boundaryMarker);
        if (!this.sequenceMatcher.isMatchedAndNotEOF()) {
            throw new IOException("Request data cannot be read");
        }
    }

    private void initialize() throws IOException {
        if (!initialized) {
            initialized = true;

            this.boundaryMarker = getBoundaryMarker(super.getContentType());
            if (this.boundaryMarker == null) {
                throw new FileUploadException("The request was rejected because " + "no multipart boundary was found");
            }

            if (HYPHENS.length + boundaryMarker.length + CHUNK_SIZE + CR_LF.length > BUFFER_SIZE) {
                throw new FileUploadException("Boundary marker is too long");
            }

            this.encoding = getCharacterEncoding();

            this.parameters = new HashMap<String, Param>();

            InputStream input = new ControlledProgressInputStream(getInputStream());

            this.sequenceMatcher = new ByteSequenceMatcher(input, BUFFER_SIZE);

            setupProgressData();

            readProlog();
        }
    }

    public void parseRequest() {
        canStop = true;

        setupProgressData();

        try {
            initialize();

            while (!sequenceMatcher.isEOF()) {
                readNext();
            }
        } catch (IOException e) {
            this.cancel();

            if (!this.shouldStop) {
                throw new FileUploadException("IO Error parsing multipart request", e);
            }
        } finally {
            canStop = false;
        }
    }

    @SuppressWarnings("unchecked")
    private void setupProgressData() {
        if (percentMap == null || requestSizeMap == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            if (facesContext != null) {
                ExternalContext externalContext = facesContext.getExternalContext();
                if (externalContext != null) {
                    Map<String, Object> sessionMap = externalContext.getSessionMap();
                    if (sessionMap != null) {
                        String uploadId = getUploadId();

                        synchronized (sessionMap) {
                            if (percentMap == null) {
                                percentMap = (Map<String, Object>) sessionMap.get(PERCENT_BEAN_NAME);
                                if (percentMap == null) {
                                    percentMap = new ConcurrentHashMap<String, Object>();
                                    sessionMap.put(PERCENT_BEAN_NAME, percentMap);
                                }
                            }

                            if (requestSizeMap == null) {
                                requestSizeMap = (Map<String, Integer>) sessionMap.get(REQUEST_SIZE_BEAN_NAME);
                                if (requestSizeMap == null) {
                                    requestSizeMap = new ConcurrentHashMap<String, Integer>();
                                    sessionMap.put(REQUEST_SIZE_BEAN_NAME, requestSizeMap);
                                }
                            }
                        }

                        percentMap.put(uploadId, Double.valueOf(0));

                        requestSizeMap.put(uploadId, getSize());
                    }
                }
            }
        }
    }

    private void fillProgressInfo() {
        setupProgressData();

        if (percentMap != null) {
            Double percent = (Double) (100.0 * this.bytesRead / this.contentLength);
            percentMap.put(uid, percent);
            // this.percent = percent;
        }
    }

    private byte[] getBoundaryMarker(String contentType) {
        Map<String, String> params = parseParams(contentType, ";");
        String boundaryStr = (String) params.get("boundary");

        if (boundaryStr == null) {
            return null;
        }

        try {
            return boundaryStr.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            return boundaryStr.getBytes();
        }
    }

    private Map<String, String> parseParams(String paramStr, String separator) {
        Map<String, String> paramMap = new HashMap<String, String>();
        parseParams(paramStr, separator, paramMap);
        return paramMap;
    }

    private void parseParams(String paramStr, String separator, Map<String, String> paramMap) {
        String[] parts = paramStr.split(separator);

        for (String part : parts) {
            Matcher m = PARAM_VALUE_PATTERN.matcher(part);
            if (m.matches()) {
                String key = m.group(1);
                String value = m.group(2);

                // Strip double quotes
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                if (!"filename".equals(key)) {
                    paramMap.put(key, value);
                } else {
                    paramMap.put(key, parseFileName(paramStr));
                }
            }
        }
    }

    private String parseFileName(String parseStr) {
        Matcher m = FILE_NAME_PATTERN.matcher(parseStr);
        if (m.matches()) {
            String name = m.group(1);
            if (name.startsWith("&")) {
                return decodeFileName(name);
            } else {
                return name;
            }
        }
        return null;
    }

    private Param getParam(String name) {
        Param param = null;
        if (parameters != null) {
            param = parameters.get(name);
        }

        if (param == null) {
            if (!canceled) {
                try {
                    initialize();

                    while (param == null && !sequenceMatcher.isEOF()) {
                        readNext();
                        param = parameters.get(name);
                    }
                } catch (IOException e) {
                    this.cancel();
                    throw new FileUploadException("IO Error parsing multipart request", e);
                }
            }
        }

        return param;
    }

    public Integer getSize() {
        return contentLength;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Enumeration getParameterNames() {
        if (parameters == null) {
            parseRequest();
        }

        return Collections.enumeration(parameters.keySet());
    }

    public byte[] getFileBytes(String name) {
        Param p = getParam(name);
        return (p != null && p instanceof FileParam) ? ((FileParam) p).getData() : null;
    }

    public InputStream getFileInputStream(String name) {
        Param p = getParam(name);
        return (p != null && p instanceof FileParam) ? ((FileParam) p).getInputStream() : null;
    }

    public String getFileContentType(String name) {
        Param p = getParam(name);
        return (p != null && p instanceof FileParam) ? ((FileParam) p).getContentType() : null;
    }

    public Object getFile(String name) {
        Param p = getParam(name);
        return (p != null && p instanceof FileParam) ? ((FileParam) p).getFile() : null;
    }

    public String getFileName(String name) {
        Param p = getParam(name);
        return (p != null && p instanceof FileParam) ? ((FileParam) p).getFilename() : null;
    }

    public int getFileSize(String name) {
        Param p = getParam(name);
        return (p != null && p instanceof FileParam) ? ((FileParam) p).getFileSize() : -1;
    }

    @Override
    public String getParameter(String name) {
        Param p = getParam(name);
        if (p != null && p instanceof ValueParam) {
            ValueParam vp = (ValueParam) p;
            if (vp.getValue() instanceof String) {
                return (String) vp.getValue();
            }
        } else if (p != null && p instanceof FileParam) {
            return "---BINARY DATA---";
        } else {
            return super.getParameter(name);
        }

        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        parseRequest();

        Param p = getParam(name);
        if (p != null && p instanceof ValueParam) {
            ValueParam vp = (ValueParam) p;
            if (vp.getValue() instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<String> vals = (List<String>) vp.getValue();
                String[] values = new String[vals.size()];
                vals.toArray(values);
                return values;
            } else {
                return new String[] { (String) vp.getValue() };
            }
        } else {
            return super.getParameterValues(name);
        }
    }

    @Override
    public Map<String, Object> getParameterMap() {
        if (parameters == null) {
            parseRequest();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> params = new HashMap<String, Object>(super.getParameterMap());

        for (String name : parameters.keySet()) {
            Param p = parameters.get(name);
            if (p instanceof ValueParam) {
                ValueParam vp = (ValueParam) p;
                if (vp.getValue() instanceof String) {
                    params.put(name, vp.getValue());
                } else if (vp.getValue() instanceof List) {
                    params.put(name, getParameterValues(name));
                }
            }
        }

        return params;
    }

    public List<UploadItem> getUploadItems() {
        List<UploadItem> uploadItems = new ArrayList<UploadItem>();
        for (String k : keys) {
            uploadItems.add(new UploadItem(getFileName(k), getFileSize(k), getFileContentType(k), getFile(k)));
        }
        return uploadItems;
    }

    public boolean isFormUpload() {
        return "_richfaces_form_upload".equals(uid);
    }

    @Override
    public String getHeader(String name) {
        if (!"Accept".equals(name)) {
            return super.getHeader(name);
        } else {
            return TEXT_HTML;
        }
    }

    public void stop() {
        if (canStop) {
            shouldStop = true;
        }
    }

    public boolean isStopped() {
        return this.shouldStop;
    }

    public boolean isDone() {
        return !(this.shouldStop && (this.canceled || this.contentLength != null
            && this.contentLength.intValue() != this.bytesRead));
    }

    @Override
    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    public String getUploadId() {
        return uid;
    }

    public void clearRequestData() {
        String uploadId = getUploadId();

        if (percentMap != null) {
            percentMap.remove(uploadId);
        }

        if (requestSizeMap != null) {
            requestSizeMap.remove(uploadId);
        }
    }
}