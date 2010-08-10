/**
 * 
 */
package org.ajax4jsf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

/**
 * @author asmirnov
 * 
 */
public class PagesBean {

	private static final Pattern JSP_PATTERN = Pattern.compile(".*\\.jspx?");

	private static final Pattern XHTML_PATTERN = Pattern.compile(".*\\.xhtml");
	
	private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*)</title>",Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);

	private ServletContext _servletContext;

	private List<PageDescriptionBean> _jspPages;

	private String _path;

	private List<PageDescriptionBean> _xhtmlPages;

	/**
	 * @return the path
	 */
	public String getPath() {
		return _path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		_path = path;
	}

	/**
	 * @return the servletContext
	 */
	public ServletContext getServletContext() {
		return _servletContext;
	}

	/**
	 * @param servletContext
	 *            the servletContext to set
	 */
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	public List<PageDescriptionBean> getJspPages() {
		if (_jspPages == null && null != getServletContext()) {
			_jspPages = getPagesByPattern(JSP_PATTERN);
		}

		return _jspPages;
	}

	public List<PageDescriptionBean> getXhtmlPages() {
		if (_xhtmlPages == null && null != getServletContext()) {
			_xhtmlPages = getPagesByPattern(XHTML_PATTERN);
		}

		return _xhtmlPages;
	}

	/**
	 * 
	 */
	private List<PageDescriptionBean> getPagesByPattern(Pattern pattern) {
		List<PageDescriptionBean> jspPages = new ArrayList<PageDescriptionBean>();
		Set resourcePaths = getServletContext().getResourcePaths(getPath());
		for (Iterator iterator = resourcePaths.iterator(); iterator
				.hasNext();) {
			String page = (String) iterator.next();
			if (pattern.matcher(page).matches()) {
				PageDescriptionBean pageBean = new PageDescriptionBean();
				pageBean.setPath(page);
				InputStream pageInputStream = getServletContext().getResourceAsStream(page);
				if(null != pageInputStream){
					byte[] head = new byte[1024];
					try {
						int readed = pageInputStream.read(head);
						String headString = new String(head,0,readed);
						Matcher titleMatcher = TITLE_PATTERN.matcher(headString);
						if(titleMatcher.find() && titleMatcher.group(1).length()>0){
							pageBean.setTitle(titleMatcher.group(1));
						} else {
							pageBean.setTitle(page);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							pageInputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				jspPages.add(pageBean);
			}
		}
		return jspPages;
	}

}
