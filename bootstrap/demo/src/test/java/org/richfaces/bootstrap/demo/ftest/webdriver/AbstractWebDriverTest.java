/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.bootstrap.demo.ftest.webdriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.test.selenium.support.pagefactory.StaleReferenceAwareFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.richfaces.bootstrap.demo.ftest.AbstractBootsrapDemoTest;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractWebDriverTest<Page extends BootstrapDemoPage> extends AbstractBootsrapDemoTest {

    private static final String PREFIX_TEST_URL = "component/";
    private static final int WD_NUMBER_OF_TRIES = 10;
    private static final int WD_WAIT_TIME = 5;//s
    private FieldDecorator fieldDecorator;
    protected Page page;
    @Drone
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void initializeWebDriver() {
        driver.manage().timeouts().implicitlyWait(WD_WAIT_TIME, TimeUnit.SECONDS);
    }

    @BeforeMethod(alwaysRun = true, dependsOnMethods = { "initializeWebDriver" })
    public void initializePage() {
        PageFactory.initElements(getFieldDecorator(), getPage());
    }

    /**
     * @throws MalformedURLException
     */
    @BeforeMethod(alwaysRun = true, dependsOnMethods = { "initializePage" })
    public void initializePageUrl() throws MalformedURLException {
        driver.get(getPathURL().toString());
    }

    private URL getPathURL() throws MalformedURLException {
        return new URL(deploymentURL, getTestURL());
    }

    private FieldDecorator getFieldDecorator() {
        if (fieldDecorator == null) {
            fieldDecorator = new StaleReferenceAwareFieldDecorator(new DefaultElementLocatorFactory(driver), WD_NUMBER_OF_TRIES);
        }
        return fieldDecorator;
    }

    protected Page getPage() {
        if (page == null) {
            page = createPage();
        }
        return page;
    }

    private String getTestURL() {
        return PREFIX_TEST_URL + page.getComponentName();
    }

    protected abstract Page createPage();
}
