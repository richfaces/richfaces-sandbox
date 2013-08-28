package org.richfaces.sandbox.chart;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;


@JavaScript("charttestutil")
@Dependency(sources = "charttestutil.js")
public interface ChartJs {
 
    String hello();
    int seriesLength(String id);
    int dataLength(String id, int seriesIndex);
    long pointX(String id, int seriesIndex, int pointIndex);
    long pointY(String id, int seriesIndex, int pointIndex);
    int pointXPos(String id, int seriesIndex, int pointIndex);
    int pointYPos(String id, int seriesIndex, int pointIndex);
 
}
