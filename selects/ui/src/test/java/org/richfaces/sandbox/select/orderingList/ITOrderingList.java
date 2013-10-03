package org.richfaces.sandbox.select.orderingList;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.request.RequestGuard;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.richfaces.sandbox.select.orderingList.model.Capital;
import org.richfaces.sandbox.select.orderingList.model.Capitals;
import org.richfaces.sandbox.select.orderingList.model.CapitalsBean;
import org.richfaces.sandbox.select.orderingList.model.CapitalsConverter;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

import java.net.URL;

@RunWith(Arquillian.class)
@RunAsClient
public class ITOrderingList {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL deploymentUrl;

    @FindBy(css = ".orderingList")
    private OrderingListFragment orderingList;

    @JavaScript
    private RequestGuard guard;

    @Deployment
    public static WebArchive deployment() {
        ComponentDeployment deployment = new ComponentDeployment(ITOrderingList.class);

        deployment.archive()
            .addClasses(Capital.class)
            .addClasses(Capitals.class)
            .addClasses(CapitalsBean.class)
            .addClasses(CapitalsConverter.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(deploymentUrl.toExternalForm());
    }

    private static void addIndexPage(ComponentDeployment deployment) {
        FaceletAsset p = new FaceletAsset();
        p.form("<s:orderingList value='#{capitalsBean.capitalList}' var='capital' itemValue='#{capital}' itemLabel='#{capital.name}'> ");
        p.form("    <f:converter converterId='CapitalsConverter' />");
        p.form("</s:orderingList>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
