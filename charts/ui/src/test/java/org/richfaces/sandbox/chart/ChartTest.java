package org.richfaces.sandbox.chart;

import java.io.File;
import java.net.URL;




import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class ChartTest{
	
	private static final String WEBAPP_PATH = "src/test/webapp";
	private static final String INDEX_PAGE = "faces/index.xhtml";
	
	@Drone
	WebDriver browser;
	
	@ArquillianResource
	URL deploymentUrl;
	
	@Deployment
	public static WebArchive createDeployment(){
		return ShrinkWrap
				.create(WebArchive.class)
				.addClass(ChartTest.class)
				.addClass(EventBean.class)
				.addAsWebResource(new File(WEBAPP_PATH, "index.xhtml"))
				.addAsWebInfResource(
						new StringAsset("<faces-config version=\"2.0\"/>"),
						"faces-config.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsLibraries(
						DependencyResolvers
								.use(MavenDependencyResolver.class)
								.artifact(
										"org.richfaces.ui:richfaces-components-ui:4.3.1-SNAPSHOT")
								.artifact(
										"org.richfaces.core:richfaces-core-impl:4.3.1-SNAPSHOT")
								.artifact(
										"org.richfaces.sandbox:charts-ui:5.0.0-SNAPSHOT")
								.resolveAsFiles())
				.setWebXML(new File(WEBAPP_PATH, "web.xml"));
	}
	
	@Test
	public void Empty(){
		Assert.assertTrue(true);
	}
	
}