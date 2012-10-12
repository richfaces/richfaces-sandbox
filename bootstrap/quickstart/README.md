RichFaces Bootstrap Quickstart
==============================

Introduction
------------

Use this project as a quickstart to "bootstrap" your applications.


NOT FOR PRODUCTION USE
======================
RichFaces Bootstrap is a Sandbox project, and not yet meant for production use.  API's and component implementations will change.

Building the Project
--------------------

Build the project with default options to use the generated CSS approach to styling the components:

    mvn clean install

To use the client-side LESS style approach, (compiled to CSS in the client using the less.js library), change the
org.richfaces.clientSideStyle context-param in the web.xml to true:

    <context-param>
        <param-name>org.richfaces.clientSideStyle</param-name>
        <param-value>true</param-value>
        <!-- Note this will be overridden by PROJECT_STAGE = Production -->
    </context-param>
