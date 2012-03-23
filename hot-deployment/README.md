Quick Turnaround with CDK in Eclipse IDE
========================================

Prerequisities
--------------

* Git
* Maven (3.0.4)
* [JBoss Tools](http://www.jboss.org/tools/download/dev) (3.3.0.M5)
    * Eclipse (3.7)
    * M2E - Maven for Eclipse
* [JRebel](http://zeroturnaround.com/jrebel/) (4.6.1) - requires license
* [JBoss AS 7](http://www.jboss.org/jbossas/downloads) (7.0.2.Final)

Note: The reference versions in brackets.

Check out [CDK](https://github.com/richfaces/cdk) and [Sandbox](https://github.com/richfaces/sandbox) projects
----------------------------------

1. clone sources

        git clone git://github.com/richfaces/cdk.git
        git clone git://github.com/richfaces/sandbox.git

2. checkout feature branches

        cd ~/cdk/	
        git checkout -b feature/cmdline-generator origin/feature/cmdline-generator
        cd ~/sandbox/
        git checkout -b feature/hot-deployment origin/feature/hot-deployment


Build CDK
---------

        cd ~/cdk/
        mvn clean install -DskipTests=true

Built binaries for CDK command-line generator now resides in `~/cdk/cmdline-generator/target/`

        ls cmdln-generator/target/cdk-cmdline-generator.jar


Setup the component project (`hot-ui`)
--------------------------------------


### Import `hot-ui` project to the IDE

File > Import > Existing Maven Projects

Browse > Select `sandbox/hot-deployment/ui`


### Activate maven profile `jrebel`

Note: *Project* = Context Menu on the given project in Project Explorer view

    Project -> Maven > Select Maven Profiles

    Check `jrebel` and confirm


### Build the project with Maven in IDE

This step will generate sources to `target/generated-sources` directory.

    Project > Run As > Maven install

It will also create JRebel configuration specific for your project location:

    ~/sandbox/hot-deployment/ui/src/main/resources/rebel.xml

You need to refresh the project to allow IDE detect newly generated resources.

    Project > Refresh

### Setup classpath

We need to attach the generated sources and templates to the imported project:

    Project > Properties > Java Build Path > Source

    Add Folder...
        src/main/templates
        target/generated-sources/main/java
        target/generated-sources/main/resources


### Setup Builder

At the last step, we are going to attach CDK command-line generator
as custom external builder to allow build on the source change:

    Project > Properties > Builders > New...

    Select type "Program" and confirm

    Configure as follows:

    Name: CDK Generator
    
    Main tab
        
        Location: ~/cdk/cmdln-generator/run.sh
        Arguments: -d -p ${build_project}

    Refresh tab

        Check "Refresh resources upon completion"
        Select "Specific resources"
        Specify Resources...
            hot-ui/target/generated-sources/main/java
            hot-ui/target/generated-sources/main/resources

    Build Options tab

        Uncheck "After a Clean"
        Check "During auto builds"
        Check "Specify working set of relevant resources"
        Specify Resources...
            hot-ui/src/main/java/org/richfaces/component/component
            hot-ui/src/main/java/org/richfaces/component/renderkit
            hot-ui/src/main/templates

Note: if something won't work well later, once we will generate application,
you can add `-d` argument to enable debugging output

Setup the sample project (`hot-demo`)
-------------------------------------

### Import hot-demo project to the IDE
    File > Import > Existing Maven Projects
    Browser > Select "sandbox/hot-deployment/demo"


### Activate "jrebel" Maven profile
    Project -> Maven > Select profiles
    Check "jrebel" and confirm

### Build the project for the first time
    Project > Run As > Maven install
    

Setup the JBoss AS 7
--------------------
    Extract the JBoss AS 7 distribution
    Open the Servers view
    Hit "new server wizard" link
    Select server type "JBoss AS 7.0"
    Hit "Next" button
    
    Hit "Browse..." button
    Select the root of the extracted JBoss AS 7 distribution
    Hit "Finish" button

Start the project in JBoss AS
------------------------------

### Turn on JRebel integration

    Go to the Servers view
    Open overview - click twice on "JBoss AS 7.0 Runtime Server"
    Look for "JRebel integration" section
    Check "Enable JRebel agent"


### Run the `hot-demo` on the server

    Project > Run As > Run on Server
    Select "JBoss 7.0 Runtime Server"
    Finish

Now the project is starting, you should see following output, which indicates that:

* the JRebel integration is active
* `hot-demo` is being deployed
* JRebel monitors changes in the project
* `hot-demo` is successfully deployed

What you should see:

    #############################################################

     JRebel 4.6.1 (201203151508)
     (c) Copyright ZeroTurnaround OU, Estonia, Tartu.


    ...

    14:39:05,278 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-2) JBAS015876: Starting deployment of "hot-demo.war"

    ...

    14:39:07,123 INFO  [stdout] (MSC service thread 1-2) JRebel: Directory '~/sandbox/hot-deployment/demo/target/classes' will be monitored for changes.
    14:39:07,125 INFO  [stdout] (MSC service thread 1-2) JRebel: Directory '~/sandbox/hot-deployment/demo/src/main/webapp' will be monitored for changes.
    14:39:07,126 INFO  [stdout] (MSC service thread 1-2) JRebel: Directory '~/sandbox/hot-deployment/ui/target/classes' will be monitored for changes.

    ...

    14:39:13,562 INFO  [org.jboss.as.server] (DeploymentScanner-threads - 2) JBAS018559: Deployed "hot-demo.war"


### Application is running

Check that the application is running fine:

[http://localhost:8080/hot-demo/](http://localhost:8080/hot-demo/)

You should see following output on the page:

    Hot Component
    Content: some complex renderer logic content
    Title: some title

That's the output from our sample component!

Everything is now prepared for using quick turnaround.
Let's try do modifications to the project in order to employ CDK!

Trying Quick Turnaround
-----------------------

Let's modify some CDK resources from `hot-ui`
to start the CDK generator and then use JRebel to hot-reload resources.

### Trying hot generation & deployment

#### Modifying renderer base

1. Open `HotComponentRendererBase` class in the `hot-ui` project
2. Change return value of `generateContent()` method
3. Save the file

The CDK Generator is now fired and you should see following output
in  the Console view in Eclipse:

    [generate: ~/sandbox/hot-deployment/ui]
    [total: 2987 ms]

Refresh the page to see the modified content immediately:

[http://localhost:8080/hot-demo/](http://localhost:8080/hot-demo/)

In the server console, you should see following output:

    20:00:33,540 INFO  [stdout] (http--127.0.0.1-8080-1) JRebel: Reloading class 'org.richfaces.renderkit.HotComponentRendererBase'.
    20:00:35,115 INFO  [stdout] (http--127.0.0.1-8080-1) JRebel: Reloading class 'org.richfaces.component.UIHotComponent'.
    20:00:35,125 INFO  [stdout] (http--127.0.0.1-8080-1) JRebel: Reloading class 'org.richfaces.component.UIHotComponent$Properties'.
    20:00:35,148 INFO  [stdout] (http--127.0.0.1-8080-1) JRebel: Reinitialized class 'org.richfaces.component.UIHotComponent$Properties'.
    20:00:35,182 INFO  [stdout] (http--127.0.0.1-8080-1) JRebel: Reloading class 'org.richfaces.renderkit.html.HotComponentRenderer'.


Note: if something dind't go well, you can add `-d` argument to enable
debugging output, see section "Setup Builder" above.

#### Modifying template file

Let's try another modification:

1. Open `hotComponent.template.xml` in `hot-ui` project
2. Change line 19 to read "My Hot Component"
3. Save the file

The generator is triggered again.

Let's refresh the browser:

[http://localhost:8080/hot-demo/](http://localhost:8080/hot-demo/)


