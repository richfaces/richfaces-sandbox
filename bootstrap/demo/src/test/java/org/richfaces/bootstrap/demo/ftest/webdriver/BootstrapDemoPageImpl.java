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

/**
 * Extend this class instead of implementing {code}BootstrapDemoPage{code} for
 * implementation off {code}getComponentName{code}. To get it working use page
 * names like PickListPage ([ComponentName][Page]).
 *
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class BootstrapDemoPageImpl implements BootstrapDemoPage {

    private String name;

    @Override
    public String getComponentName() {
        if (name == null) {
            name = this.getClass().getSimpleName().replace("Page", "");
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        return name;
    }
}
