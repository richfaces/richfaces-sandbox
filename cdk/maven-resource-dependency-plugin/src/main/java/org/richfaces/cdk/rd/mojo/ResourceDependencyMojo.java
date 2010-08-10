/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package org.richfaces.cdk.rd.mojo;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.digester.Digester;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.MavenMetadataSource;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.velocity.VelocityComponent;

import org.richfaces.cdk.rd.Components;
import org.richfaces.cdk.rd.generator.ResourceAssembler;
import org.richfaces.cdk.rd.generator.ResourcesGenerator;
import org.richfaces.cdk.rd.generator.ScriptAssembler;
import org.richfaces.cdk.rd.generator.StyleAssembler;
import org.richfaces.cdk.rd.handler.ComponentsHandler;
import org.richfaces.cdk.rd.utils.PluginUtils;

/**
 * @author Anton Belevich
 * @goal assembly-resources
 * @phase generate-resources
 *
 */
public class ResourceDependencyMojo extends AbstractMojo {

    /**
     * afterScriptIncludes
     * @parameter
     */
    private List<String> afterScriptIncludes;

    /**
     * afterStyleIncludes
     * @parameter
     */
    private List<String> afterStyleIncludes;

    /**
     * beforeScriptIncludes
     * @parameter
     */
    private List<String> beforeScriptIncludes;

    /**
     * beforeStyleIncludes
     * @parameter
     */
    private List<String> beforeStyleIncludes;

    /**
     * componentExcludes
     * @parameter
     */
    private String[] componentExcludes;

    /**
     * componentIncludes
     * @parameter
     */
    private String[] componentIncludes;

    /**
     * Used to look up Artifacts in the remote repository.
     *
     * @component
     */
    private ArtifactFactory factory;

    /**
     * The local repository.
     *
     * @parameter expression="${localRepository}"
     */
    private ArtifactRepository localRepository;

    /**
     *
     * @component
     */
    private ArtifactMetadataSource metadataSource;

    /**
     * namespaceExcludes
     * @parameter
     */
    private String[] namespaceExcludes;

    /**
     * namespaceIncludes
     * @parameter
     */
    private String[] namespaceIncludes;

    /**
     * outputResourceDirectory
     * @parameter expression="${project.build.directory}/generated-resources
     *
     */
    private File outputResourceDirectory;

    /**
     * Top maven project.
     *
     * @parameter expression="${project}"
     * @readonly
     */
    MavenProject project;

    /**
     * Used to look up Artifacts in the remote repository.
     *
     * @component
     */
    private ArtifactResolver resolver;

    /**
     * scriptExcludes
     * @parameter
     */
    private String[] scriptExcludes;

    /**
     * scriptFilePath
     * @parameter expression="custom-dependencies"
     */
    private String scriptFilePath;

    /**
     * scriptIncludes
     * @parameter
     */
    private String[] scriptIncludes;

    /**
     * styleExcludes
     * @parameter
     */
    private String[] styleExcludes;

    /**
     * styleFilePath
     * @parameter  expression="custom-dependencies"
     */
    private String styleFilePath;

    /**
     * styleIncludes
     * @parameter
     */
    private String[] styleIncludes;

    /**
     * @component
     */
    protected VelocityComponent velocity;

    /**
     * webSourceDirectory
     *
     * @parameter expression="${basedir}/src/main/webapp"
     */
    private File webSourceDirectory;

    /**
     * xhtmlExcludes
     * @parameter
     */
    private String[] xhtmlExcludes;

    /**
     * xhtmlIncludes
     * @parameter
     */
    private String[] xhtmlIncludes;

    /**
     * xmlConfigPatterns
     * @parameter
     */
    private String[] xmlConfigPatterns;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Set<Artifact> artifacts = resolveDependenciesArtifacts();
            Digester defaultDigester = createDigester();
            Map<String, Components> components = new HashMap<String, Components>();

            if (xmlConfigPatterns == null) {
                xmlConfigPatterns = PluginUtils.DEFAULT_CONFIG_PATTERNS;
            }

            for (Artifact artifact : artifacts) {
                FileObject jar = resolveArtifact(artifact);

                getLog().info("Process jar: " + jar.getName().getFriendlyURI());

                FileObject[] configs = PluginUtils.resolveConfigsFromJar(jar, xmlConfigPatterns);

                if (configs.length == 0) {
                    getLog().info("no dependecy files found");
                } else {
                    getLog().info("next dependency files found");

                    for (FileObject config : configs) {
                        getLog().info(config.getName().getBaseName());
                    }
                }

                components.putAll(PluginUtils.processConfigs(configs, defaultDigester));
            }

            if (!webSourceDirectory.exists()) {
                webSourceDirectory.mkdirs();
            }

            ComponentsHandler handler = findComponents(webSourceDirectory, components, xhtmlIncludes, xhtmlExcludes);
            ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader contextClassLoader = createClassLoader();

            Thread.currentThread().setContextClassLoader(contextClassLoader);

            if (contextClassLoader != null) {
                Set<String> scripts = handler.getScripts();

                scriptFilePath = scriptFilePath.endsWith(".js") ? scriptFilePath : scriptFilePath + ".js";

                File scriptFile = new File(outputResourceDirectory, scriptFilePath);

                if (!scriptFile.exists()) {
                    File parent = scriptFile.getParentFile();

                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                }

                ScriptAssembler scriptAssembler = new ScriptAssembler(getLog());

                if (!scripts.isEmpty()) {
                    getLog().info("Start merge scripts to the: " + scriptFile.getPath());
                    mergeResources(scriptFile, scriptAssembler, beforeScriptIncludes, afterScriptIncludes, scripts);
                }

                Set<String> styles = handler.getStyles();

                styleFilePath = styleFilePath.endsWith(".xcss") ? styleFilePath : styleFilePath + ".xcss";

                File styleFile = new File(outputResourceDirectory, styleFilePath);
                File parent = styleFile.getParentFile();

                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }

                StyleAssembler styleAssembler = new StyleAssembler(getLog());

                styleAssembler.setVelocityComponent(velocity);

                if (!styles.isEmpty()) {
                    getLog().info("Start merge styles to the: " + styleFile.getPath());
                    mergeResources(styleFile, styleAssembler, beforeStyleIncludes, afterStyleIncludes, styles);
                }

                Resource resource = new Resource();

                resource.setDirectory(outputResourceDirectory.getPath());
                project.addResource(resource);
            }

            Thread.currentThread().setContextClassLoader(oldClassLoader);
        } catch (Exception e) {
            getLog().error("Error generate resource", e);

            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    public void mergeResources(File assembly, ResourceAssembler assembler, List<String> beforeIncludes,
                               List<String> afterIncludes, Collection<String> resources) {
        ResourcesGenerator styleGenerator = new ResourcesGenerator(getLog());

        styleGenerator.setAssembler(assembler);
        styleGenerator.setIncludesAfter(afterIncludes);
        styleGenerator.setIncludesBefore(beforeIncludes);
        styleGenerator.setResources(resources);
        styleGenerator.setAssemblyFile(assembly);
        styleGenerator.doAssembly();
        styleGenerator.writeToFile();
    }

    protected Set<Artifact> resolveDependenciesArtifacts() throws Exception {
        ArtifactResolutionResult result = null;
        List<Dependency> dependencies = project.getDependencies();
        Set<Artifact> artifacts = MavenMetadataSource.createArtifacts(factory, dependencies, null, null, project);

        artifacts.add(project.getArtifact());
        result = resolver.resolveTransitively(artifacts, project.getArtifact(), Collections.EMPTY_LIST,
                localRepository, metadataSource);

        return result.getArtifacts();
    }

    public ComponentsHandler findComponents(File webSourceDir, Map<String, Components> components, String[] includes,
            String[] excludes)
            throws Exception {
        if (includes == null) {
            includes = PluginUtils.DEFAULT_PROCESS_INCLUDES;
        }

        if (excludes == null) {
            excludes = new String[0];
        }

        DirectoryScanner scanner = new DirectoryScanner();

        scanner.setBasedir(webSourceDir);
        scanner.setIncludes(includes);
        scanner.setExcludes(excludes);
        scanner.addDefaultExcludes();
        getLog().info("search *.xhtml files");
        scanner.scan();

        String[] collectedFiles = scanner.getIncludedFiles();

        for (String collectedFile : collectedFiles) {
            getLog().info(collectedFile + " found");
        }

        ComponentsHandler handler = new ComponentsHandler(getLog());

        handler.setComponents(components);
        handler.setScriptIncludes(scriptIncludes);
        handler.setScriptExcludes(scriptExcludes);
        handler.setStyleIncludes(styleIncludes);
        handler.setStyleExcludes(styleExcludes);
        handler.setComponentIncludes(componentIncludes);
        handler.setComponentExcludes(componentExcludes);
        handler.setNamespaceIncludes(namespaceIncludes);
        handler.setNamespaceExcludes(namespaceExcludes);

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        saxParserFactory.setNamespaceAware(true);

        Log log = getLog();

        for (String processFile : collectedFiles) {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            File file = new File(webSourceDir, processFile);

            if (file.exists()) {
                if (log.isDebugEnabled()) {
                    log.debug("start process file: " + file.getPath());
                }

                try {
                    saxParser.parse(file, handler);
                } catch (Exception e) {
                    if (log.isDebugEnabled()) {
                        log.error("Error process file: " + file.getPath() + "\n" + e.getMessage(), e);
                    } else {
                        log.error("Error process file: " + file.getPath() + "\n" + e.getMessage());
                    }
                }
            }
        }

        return handler;
    }

    protected FileObject resolveArtifact(Artifact artifact) {
        FileObject jarFileObjects = null;

        if (artifact != null) {
            try {
                resolver.resolve(artifact, Collections.EMPTY_LIST, localRepository);

                if (getLog().isDebugEnabled()) {
                    getLog().debug("artifact " + artifact.getFile().toURI() + " is resolved");
                }
            } catch (ArtifactResolutionException e) {
                getLog().error("Error with resolve artifact " + artifact.getFile().getPath() + "\n" + e.getMessage(),
                               e);
            } catch (ArtifactNotFoundException e) {
                getLog().error("Not found artifact " + artifact.getFile().toURI() + "\n" + e.getMessage(), e);
            }

            File file = artifact.getFile();

            try {
                FileSystemManager manager = VFS.getManager();

                jarFileObjects = manager.resolveFile("jar:" + file.toURI());
            } catch (FileSystemException e) {
                getLog().error("Error during processing file: " + file.toURI() + "\n" + e.getMessage(), e);
            }
        }

        return jarFileObjects;
    }

    public Digester createDigester() {

        // default digester for *.component-dependencies.xml
        return PluginUtils.createDefaultDigester();
    }

    protected ClassLoader createClassLoader() throws Exception {
        ClassLoader classLoader = null;
        Set<Artifact> artifacts = resolveDependenciesArtifacts();

        // create a new classloading space
        ClassWorld world = new ClassWorld();

        // use the existing ContextClassLoader in a realm of the classloading space
        ClassRealm realm = world.newRealm("org.richfaces.cdk", Thread.currentThread().getContextClassLoader());

        // create another realm for the app jars
        ClassRealm childRealm = realm.createChildRealm("jar");

        for (Artifact jar : artifacts) {
            try {
                childRealm.addConstituent(jar.getFile().toURL());
            } catch (MalformedURLException e) {
                getLog().error("Artifact url " + jar.getFile() + " is invalid");
            }
        }

        // add project classes, scripts, styles etc ...
        List<Resource> compileClasspathElements = project.getCompileClasspathElements();

        addResources(childRealm, compileClasspathElements);

        List<Resource> scripts = project.getScriptSourceRoots();

        addResources(childRealm, scripts);

        List<Resource> resources = project.getResources();

        addResources(childRealm, resources);
        childRealm.addConstituent(webSourceDirectory.toURI().toURL());

        // make the child realm the ContextClassLoader
        classLoader = childRealm.getClassLoader();

        return classLoader;
    }

    private void addResources(ClassRealm realm, List resources) {
        if (realm != null && resources != null) {
            for (Object path : resources) {
                URL url = null;
                String formatted = null;

                if (path instanceof String) {
                    formatted = (String) path;
                } else if (path instanceof Resource) {
                    formatted = ((Resource) path).getDirectory();
                }

                if (formatted != null) {
                    File file = new File(formatted);

                    try {
                        url = file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        getLog().error("Resource url " + file.getPath() + " is invalid");
                    }

                    realm.addConstituent(url);
                }
            }
        }
    }
}
