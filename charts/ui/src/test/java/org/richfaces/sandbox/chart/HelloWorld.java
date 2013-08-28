package org.richfaces.sandbox.chart;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;


@JavaScript("helloworld")
@Dependency(sources = "helloworld.js")
public interface HelloWorld {
 
    String hello();
 
}