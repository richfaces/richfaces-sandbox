/**
 * 
 */
package org.richfaces.test.ajax;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.richfaces.test.AbstractFacesTest;
import org.richfaces.test.LocalWebClient;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import declarativeajax.Bean;

/**
 * @author asmirnov
 *
 */
public class SimpleAjaxTest extends AbstractFacesTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUpTest() throws Exception {
	}

	@Override
	protected void setupWebContent() {
		facesServer.addResource("/WEB-INF/faces-config.xml", "WEB-INF/faces-config.xml");
		facesServer.addResource("/home.xhtml", "home.xhtml");
		facesServer.addResource("/resources/stylesheet.css", "resources/stylesheet.css");
		facesServer.addResource("/resources/anim-star-bkgrnd.gif", "resources/anim-star-bkgrnd.gif");
	}
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDownTest() throws Exception {
	}

	@Test
	public void testRequest() throws Exception {
		WebClient webClient = new LocalWebClient(facesServer,BrowserVersion.FIREFOX_3);
		webClient.setThrowExceptionOnScriptError(true);
		HtmlPage page = webClient.getPage("http://localhost/home.jsf");
		System.out.println(page.asXml());		
		
	}
	
	@Test
	public void BeanTest() throws Exception {
		setupFacesRequest();
		Bean bean = application.evaluateExpressionGet(facesContext, "#{bean}", Bean.class);
		assertNotNull(bean);
	}
}
