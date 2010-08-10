/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ajax4jsf.taglib.html.jsp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspWriter;

import org.ajax4jsf.io.FastBufferOutputStream;



/**
 * ServletResponseWrapper used by the JSP 'include' action.
 * 
 * This wrapper response object is passed to RequestDispatcher.include(), so
 * that the output of the included resource is appended to that of the including
 * page.
 * 
 * @author Pierre Delisle
 */

public class ServletResponseWrapperInclude extends HttpServletResponseWrapper {

	/**
	 * PrintWriter which appends to the JspWriter of the including page.
	 */
	private PrintWriter _printWriter;
	
	private FastBufferOutputStream _bytes ;
	
	private ServletOutputStream _servletStream;

	private JspWriter _jspWriter;
	
	private boolean useWriter = false;
	
	private boolean useStream = false;

	public ServletResponseWrapperInclude(ServletResponse response,
			JspWriter jspWriter) {
		super((HttpServletResponse) response);
		this._printWriter = new PrintWriter(jspWriter);
		this._jspWriter = jspWriter;
	}

	/**
	 * Returns a wrapper around the JspWriter of the including page.
	 */
	public PrintWriter getWriter() throws IOException {
		if (useStream) {
			throw new IllegalStateException();
		}
		useWriter = true;
		return _printWriter;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if(useWriter){
			throw new IllegalStateException();
		}
		if (!useStream) {
			_bytes = new FastBufferOutputStream(getBufferSize());
			_servletStream = new ServletOutputStream(){

				/* (non-Javadoc)
				 * @see java.io.OutputStream#flush()
				 */
				public void flush() throws IOException {
					// TODO Auto-generated method stub
					super.flush();
				}

				/* (non-Javadoc)
				 * @see java.io.OutputStream#write(byte[], int, int)
				 */
				public void write(byte[] b, int off, int len) throws IOException {
					// TODO Auto-generated method stub
					_bytes.write(b, off, len);
				}

				/* (non-Javadoc)
				 * @see java.io.OutputStream#write(byte[])
				 */
				public void write(byte[] b) throws IOException {
					// TODO Auto-generated method stub
					_bytes.write(b);
				}

				/* (non-Javadoc)
				 * @see java.io.OutputStream#write(int)
				 */
				public void write(int b) throws IOException {
					_bytes.write(b);					
				}
				
			};
			useStream = true;
		}
		return _servletStream;
	}

	/**
	 * Clears the output buffer of the JspWriter associated with the including
	 * page.
	 */
	public void resetBuffer() {
		try {
			_jspWriter.clearBuffer();
			useWriter = false;
			useStream = false;
			_bytes = null;
		} catch (IOException ioe) {
		}
	}
	
	public void reset() {
		// TODO Auto-generated method stub
		resetBuffer();
	}
	
	public void flushBuffer() throws IOException {
		if (useStream) {
			// TODO - detect encoding ?
			_bytes.writeTo(_jspWriter, getCharacterEncoding());
			_bytes = new FastBufferOutputStream(getBufferSize());
		}
	}
	
	// Override ignored methods.
	public void setBufferSize(int arg0) {
	}
	
	public void setContentLength(int arg0) {
	}
	public void setContentType(String arg0) {
	}
	public void setStatus(int arg0) {
	}
	public void setLocale(Locale arg0) {
	}
	public void sendRedirect(String arg0) throws IOException {
		throw new IllegalStateException();
	}
//	public void setStatus(int arg0, String arg1) {
//	}
	
}
