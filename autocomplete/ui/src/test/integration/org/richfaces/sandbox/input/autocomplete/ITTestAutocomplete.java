package org.richfaces.sandbox.input.autocomplete;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.richfaces.shrinkwrap.descriptor.FaceletAsset;

@RunWith(Arquillian.class)
@RunAsClient
public class ITTestAutocomplete {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = ".r-autocomplete")
    private RichAutocomplete autocomplete;

    @Deployment
    public static WebArchive deployment() {
        ComponentDeployment deployment = new ComponentDeployment(ITTestAutocomplete.class);

        deployment.archive()
            .addClasses(AutocompleteBean.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addIndexPage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test() {
        browser.get(contextPath.toExternalForm());

        autocomplete.type("T");

        waitAjax().until().element(autocomplete.advanced().getMenu()).is().visible();
    }

    private static void addIndexPage(ComponentDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <s:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.body("    </s:autocomplete> #{autocompleteBean.suggestions}");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "index.xhtml");
    }
}
