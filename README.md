RichFaces Sandbox
=================

Sandbox Components
------------------

The Sandbox repository is used for the incubation of new features, components and entirely new widget suites for the RichFaces framework.

Do you have an idea for a new component?  Do you want to contribute and be involved in RichFaces?  See:

[How to Contribute](https://community.jboss.org/wiki/RichFaces4ComponentDevelopmentProcess)
-----------------

Please take the time to read more about the [Sandbox process](https://community.jboss.org/wiki/RichFaces4ComponentDevelopmentProcess). This document covers the most important steps you need to take before diving into code and contributing.

[Issue Tracking](https://issues.jboss.org/browse/RFSBOX)
--------------

All feature requests, suggestions for improvement, and bugs are tracked in [JBoss JIRA issue tracker](https://issues.jboss.org/browse/RFSBOX)

[User Forums](http://www.jboss.org/index.html?module=bb&op=viewforum&f=261)
---------------------

The best place to start with contributions is [user forum](http://www.jboss.org/index.html?module=bb&op=viewforum&f=261).

Continuous Integration
----------------------

Each commit and each pull request is verified by a [continuous integration build](https://buildhive.cloudbees.com/job/richfaces/job/sandbox/).


Build Instructions
==================

Prerequsities
-------------

* Maven 3.0.3+
* settings for [JBoss Maven Repository](https://community.jboss.org/wiki/MavenGettingStarted-Developers)

Building the Sandbox Components
-------------------------------

You can build all the Sandbox component modules using Maven:

    mvn clean install


Building Sandbox Showcase
-------------------------

After having built the individual Sandbox components (see the above), you can build the Sandbox Showcase and with following command:

    mvn install -Pshowcase

Once built, the Sandbox Showcase is available at `showcase-sandbox/target/showcase.war`.
