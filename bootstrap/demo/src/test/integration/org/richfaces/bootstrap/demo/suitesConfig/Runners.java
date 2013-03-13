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
package org.richfaces.bootstrap.demo.suitesConfig;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.builders.JUnit4Builder;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class Runners {

    public static class IntegrationTestsRunner extends Suite {

        public IntegrationTestsRunner(Class<?> klass, RunnerBuilder builder) throws InitializationError {
            super(builder, klass, getClasses("org.richfaces.bootstrap.demo.ftest", true));
        }
    }

    public static class UnitTestsRunner extends Suite {

        public UnitTestsRunner(Class<?> testClass) throws InitializationError {
            super(new JUnit4Builder(), getClasses("org.richfaces.bootstrap.demo", false));
        }
    }

    /**
     * @see <a href="http://dzone.com/snippets/get-all-classes-within-package">Get all classes within package</a>
     */
    private static Class<?>[] getClasses(String packageName, boolean inSubpackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }

            //get all classes in package/subpackages
            ArrayList<Class> classes = new ArrayList<Class>();
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName, inSubpackage));
            }

            //filtering of classes, which have no test method
            Iterable<Class> filtered = Iterables.filter(classes, new Predicate<Class>() {
                @Override
                public boolean apply(Class input) {
                    boolean hasTestAnnotation, hasIgnoredAnnotation;
                    for (Method method : input.getDeclaredMethods()) {
                        hasTestAnnotation = method.getAnnotation(Test.class) != null;
                        hasIgnoredAnnotation = method.getAnnotation(Ignore.class) != null;
                        if (hasTestAnnotation && !hasIgnoredAnnotation) {
                            return true;
                        }
                    }
                    return false;
                }
            });
            return Iterables.toArray(filtered, Class.class);
        } catch (IOException e) {
            Logger.getLogger("JUnit Test Runners").log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
            Logger.getLogger("JUnit Test Runners").log(Level.SEVERE, e.getMessage());
        }
        return new Class[]{};
    }

    private static List<Class> findClasses(File directory, String packageName, boolean inSubpackage) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory() && inSubpackage) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName(), inSubpackage));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
