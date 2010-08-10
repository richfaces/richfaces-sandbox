import java.io.IOException;

import javax.faces.component.UIComponent;

import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

import junit.framework.Test;
import junit.framework.TestSuite;


public class JSFUnitTest extends org.apache.cactus.ServletTestCase
{
   public static Test suite()
   {
      return new TestSuite( JSFUnitTest.class );
   }
   
   public void testInitialPage() throws IOException
   {
      // Send an HTTP request for the initial page
      JSFSession jsfSession = new JSFSession("/home.jsf");
      
      // A JSFClientSession emulates the browser and lets you test HTML
      JSFClientSession client = jsfSession.getJSFClientSession();
      
      // A JSFServerSession gives you access to JSF state      
      JSFServerSession server = jsfSession.getJSFServerSession();

      // Test navigation to initial viewID
      assertEquals("/home.xhtml", server.getCurrentViewID());

      // Assert that the prompt component is in the component tree and rendered
      UIComponent prompt = server.findComponent("msgPanel");
      assertTrue(prompt.isRendered());

      // Test a managed bean
      assertNotNull(server.getManagedBeanValue("#{bean}"));
   }
}
