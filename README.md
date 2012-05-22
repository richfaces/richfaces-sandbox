RichFaces Sandbox
=================

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
