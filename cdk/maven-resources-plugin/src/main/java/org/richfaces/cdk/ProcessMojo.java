/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.richfaces.cdk;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.reflections.util.ClasspathHelper;
import org.richfaces.cdk.concurrent.CountingExecutorCompletionService;
import org.richfaces.cdk.faces.FacesImpl;
import org.richfaces.cdk.naming.FileNameMapperImpl;
import org.richfaces.cdk.resource.handler.impl.DynamicResourceHandler;
import org.richfaces.cdk.resource.handler.impl.StaticResourceHandler;
import org.richfaces.cdk.resource.scan.ResourcesScanner;
import org.richfaces.cdk.resource.scan.impl.DynamicResourcesScanner;
import org.richfaces.cdk.resource.scan.impl.StaticResourcesScanner;
import org.richfaces.cdk.resource.util.ResourceUtil;
import org.richfaces.cdk.resource.writer.ResourceProcessor;
import org.richfaces.cdk.resource.writer.impl.CSSResourceProcessor;
import org.richfaces.cdk.resource.writer.impl.JavaScriptResourceProcessor;
import org.richfaces.cdk.resource.writer.impl.ResourceWriterImpl;
import org.richfaces.cdk.task.ResourceTaskFactoryImpl;
import org.richfaces.cdk.util.MoreConstraints;
import org.richfaces.cdk.util.MorePredicates;
import org.richfaces.cdk.vfs.VFS;
import org.richfaces.cdk.vfs.VFSRoot;
import org.richfaces.cdk.vfs.VirtualFile;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Constraints;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
/**
 * @goal process
 * @requiresDependencyResolution compile
 * @phase process-classes
 */
public class ProcessMojo extends AbstractMojo {

    private static final URL[] EMPTY_URL_ARRAY = new URL[0];

    private static final Function<String, Predicate<CharSequence>> REGEX_CONTAINS_BUILDER_FUNCTION = new Function<String, Predicate<CharSequence>>() {
        public Predicate<CharSequence> apply(String from) {
            Predicate<CharSequence> containsPredicate = Predicates.containsPattern(from);
            return Predicates.and(Predicates.notNull(), containsPredicate);
        };
    };

    private static final Function<Resource, String> CONTENT_TYPE_FUNCTION = new Function<Resource, String>() {
        public String apply(Resource from) {
            return from.getContentType();
        };
    };

    private final Function<String, URL> filePathToURL = new Function<String, URL>() {
        public URL apply(String from) {
            try {
                File file = new File(from);
                if (file.exists()) {
                    return file.toURI().toURL();
                }
            } catch (MalformedURLException e) {
                getLog().error("Bad URL in classpath", e);
            }
            
            return null;
        };
    };
    
    /**
     * @parameter
     * @required
     */
    private String outputDir;

    /**
     * @parameter
     * @required
     */
    // TODO handle base skins
    private String[] skins;

    /**
     * @parameter expression="${project}"
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter
     */
    private List<String> includedContentTypes;

    /**
     * @parameter
     */
    private List<String> excludedContentTypes;

    /**
     * @parameter
     */
    // TODO review usage of properties?
    private Properties fileNameMappings = new Properties();

    /**
     * @parameter
     */
    private ProcessMode processMode = ProcessMode.embedded;

    /**
     * @parameter expression="${basedir}/src/main/webapp"
     */
    private String webRoot;
    
    //TODO handle resource locales
    private Locale resourceLocales;
    
    private Collection<ResourceKey> foundResources = Sets.newHashSet();
    
    private Collection<ResourceProcessor> resourceProcessors = Arrays.<ResourceProcessor>asList(
        new JavaScriptResourceProcessor(getLog()), 
        new CSSResourceProcessor());
    
    // TODO executor parameters
    private static ExecutorService createExecutorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    private Predicate<Resource> createResourcesFilter() {
        Predicate<CharSequence> contentTypePredicate = MorePredicates.compose(includedContentTypes,
            excludedContentTypes, REGEX_CONTAINS_BUILDER_FUNCTION);
        return Predicates.compose(contentTypePredicate, CONTENT_TYPE_FUNCTION);
    }

    private URL resolveWebRoot() throws MalformedURLException {
        File result = new File(webRoot);
        if (!result.exists()) {
            result = new File(project.getBasedir(), webRoot);
        }
        if (!result.exists()) {
            return null;
        }
        
        return result.toURI().toURL();
    }
    
    private void scanDynamicResources(Collection<VFSRoot> cpFiles) throws Exception {
        ResourcesScanner scanner = new DynamicResourcesScanner(cpFiles);
        scanner.scan();
        foundResources.addAll(scanner.getResources());
    }
    
    private void scanStaticResources(Collection<VirtualFile> resourceRoots) throws Exception {
        ResourcesScanner scanner = new StaticResourcesScanner(resourceRoots);
        scanner.scan();
        foundResources.addAll(scanner.getResources());
    }

    private Collection<VFSRoot> fromUrls(Iterable<URL> urls) throws URISyntaxException, IOException {
        Collection<VFSRoot> result = Lists.newArrayList();
        
        for (URL url : urls) {
            if (url == null) {
                continue;
            }
            
            VFSRoot vfsRoot = VFS.getRoot(url);
            vfsRoot.initialize();
            result.add(vfsRoot);
        }

        return result;
    }
    
    private Collection<VFSRoot> getClasspathVfs() throws URISyntaxException, IOException {
        return fromUrls(ClasspathHelper.getUrlsForCurrentClasspath());
    }
    
    private Collection<VFSRoot> getWebrootVfs() throws URISyntaxException, IOException {
        return fromUrls(Collections.singletonList(resolveWebRoot()));
    }
    
    protected URL[] getProjectClasspath() {
        try {
            List<String> classpath = Constraints.constrainedList(Lists.<String>newArrayList(), MoreConstraints.cast(String.class));
            classpath.addAll((List<String>) project.getCompileClasspathElements());
            classpath.add(project.getBuild().getOutputDirectory());

            URL[] urlClasspath = filter(transform(classpath, filePathToURL), notNull()).toArray(EMPTY_URL_ARRAY);            
            return urlClasspath;
        } catch (DependencyResolutionRequiredException e) {
            getLog().error("Dependencies not resolved ", e);
        }

        return new URL[0];
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        Faces faces = null;
        ExecutorService executorService = null;
        
        Collection<VFSRoot> webResources = null;
        Collection<VFSRoot> cpResources = null;
        
        try {
            URL[] projectCP = getProjectClasspath();
            ClassLoader projectCL = new URLClassLoader(projectCP, contextCL);
            Thread.currentThread().setContextClassLoader(projectCL);

            webResources = getWebrootVfs();
            cpResources = getClasspathVfs();

            Collection<VirtualFile> resourceRoots = ResourceUtil.getResourceRoots(cpResources, webResources);
            
            scanDynamicResources(cpResources);
            scanStaticResources(resourceRoots);
            
            File resourceOutputDir = new File(outputDir);
            if (!resourceOutputDir.exists()) {
                resourceOutputDir = new File(project.getBuild().getDirectory(), outputDir);
            }

            File resourceMappingDir = new File(project.getBuild().getOutputDirectory());
            
            ResourceHandler resourceHandler = new DynamicResourceHandler(new StaticResourceHandler(resourceRoots));
            
            // TODO set webroot
            faces = new FacesImpl(null, new FileNameMapperImpl(Maps.fromProperties(fileNameMappings)), resourceHandler);
            faces.start();
            
            ResourceWriterImpl resourceWriter = new ResourceWriterImpl(resourceOutputDir, resourceMappingDir, resourceProcessors);
            ResourceTaskFactoryImpl taskFactory = new ResourceTaskFactoryImpl(faces);
            taskFactory.setResourceWriter(resourceWriter);

            executorService = createExecutorService();
            CompletionService<Object> completionService = new CountingExecutorCompletionService<Object>(executorService);
            taskFactory.setCompletionService(completionService);
            taskFactory.setSkins(skins);
            taskFactory.setLog(getLog());
            taskFactory.setFilter(createResourcesFilter());
            taskFactory.submit(foundResources);

            Future<Object> future = null;
            while (true) {
                future = completionService.take();
                if (future != null) {
                    try {
                        future.get();
                    } catch (ExecutionException e) {
                        // TODO: handle exception
                        e.getCause().printStackTrace();
                    }
                } else {
                    break;
                }
            }
            
            resourceWriter.writeProcessedResourceMappings();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {
            
            if (cpResources != null) {
                for (VFSRoot vfsRoot : cpResources) {
                    try {
                        vfsRoot.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
            if (webResources != null) {
                for (VFSRoot vfsRoot : webResources) {
                    try {
                        vfsRoot.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            
            // TODO review finally block
            if (executorService != null) {
                executorService.shutdown();
            }
            if (faces != null) {
                faces.stop();
            }
            Thread.currentThread().setContextClassLoader(contextCL);
        }
    }
}
