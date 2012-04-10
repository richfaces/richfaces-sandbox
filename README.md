RichFaces Sandbox
=================

Prerequsities
-------------

* Maven 3.0.3+
* settings for [JBoss Maven Repository](https://community.jboss.org/wiki/MavenGettingStarted-Developers)

Building Sandbox Components
---------------------------

You can build all the components modules using Maven:

    mvn clean install


Building Sandbox Showcase
-------------------------

You can build Showcase and all components with following command:

    mvn clean install -Pshowcase

Once built, Showcase is available at `showcase-sandbox/target/showcase.war`.
