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
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.richfaces.bootstrap.demo.ftest.AbstractBootsrapDemoTest;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public abstract class AbstractWebDriverTest extends AbstractBootsrapDemoTest {

    private static final String PREFIX_TEST_URL = "component/";
    //
    @Drone
    protected WebDriver driver;

    abstract protected String getComponentName();

    private URL getPathURL() throws MalformedURLException {
        return new URL(deploymentURL, getTestURL());
    }

    private String getTestURL() {
        return PREFIX_TEST_URL + getComponentName();
    }

    @Before
    public void initialize() throws MalformedURLException {
        initializePageUrl();
    }

    public void initializePageUrl() throws MalformedURLException {
        driver.get(getPathURL().toString());
    }
}
