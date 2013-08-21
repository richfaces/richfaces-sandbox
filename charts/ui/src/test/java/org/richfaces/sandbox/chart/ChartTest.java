package org.richfaces.sandbox.chart;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunAsClient
@RunWith(Arquillian.class)
public class ChartTest {

	private static final String WEBAPP_PATH = "src/test/webapp";
	private static final String INDEX_PAGE = "faces/index.xhtml";

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL deploymentUrl;

	
	@Deployment(testable=false)
	public static WebArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class,
				"this-component.jar");
		jar.merge(
				ShrinkWrap.create(GenericArchive.class)
						.as(ExplodedImporter.class)
						.importDirectory("target/classes/")
						.as(GenericArchive.class), "/", Filters.includeAll());

		WebArchive arch = ShrinkWrap
				.create(WebArchive.class)
				.addClass(ChartTest.class)
				.addClass(EventBean.class)
				.addAsWebResource(new File(WEBAPP_PATH, "index.xhtml"))
				.addAsWebInfResource(
						new StringAsset("<faces-config version=\"2.0\"/>"),
						"faces-config.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsLibraries(
						Maven.resolver().loadPomFromFile("pom.xml")
								.resolve(
										"org.richfaces:richfaces:5.0.0-SNAPSHOT").withClassPathResolution(false)
								.withTransitivity().asFile())
				.addAsLibraries(jar)
				.setWebXML(new File(WEBAPP_PATH, "web.xml"));

		return arch;

	}
	
	@Test
	public void Empty() {
		assertTrue(true);
	}

}