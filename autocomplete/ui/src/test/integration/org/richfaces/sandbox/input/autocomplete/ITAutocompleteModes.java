package org.richfaces.sandbox.input.autocomplete;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.guard.RequestGuard;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.RequestType;
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
public class ITAutocompleteModes {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(css = ".r-autocomplete")
    private RichAutocomplete autocomplete;

    @JavaScript
    private RequestGuard guard;

    @Deployment
    public static WebArchive deployment() {
        ComponentDeployment deployment = new ComponentDeployment(ITAutocompleteModes.class);

        deployment.archive()
            .addClasses(AutocompleteBean.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        addClientModePage(deployment);
        addAjaxModePage(deployment);

        return deployment.getFinalArchive();
    }

    @Test
    public void test_client_mode() {
        browser.get(contextPath.toExternalForm() + "client.jsf");

        guard.clearRequestDone();
        performAutocompleteSearchAndConfirmation("T", "Toronto");
        assertEquals(RequestType.NONE, guard.getRequestType());
    }

    @Test
    public void test_ajax_mode() {
        browser.get(contextPath.toExternalForm() + "ajax.jsf");

        guard.clearRequestDone();

        performAutocompleteSearchAndConfirmation("T", "Toronto");

        assertEquals(RequestType.XHR, guard.getRequestType());
    }

    private void performAutocompleteSearchAndConfirmation(String input, String expectedFirstChoice) {
        assertEquals(0, autocomplete.getSuggestions().size());

        autocomplete.type(input);
        autocomplete.waitForSuggestionsToShow();

        assertEquals(2, autocomplete.getSuggestions().size());

        autocomplete.selectFirst();
        autocomplete.waitForSuggestionsToHide();

        assertEquals(expectedFirstChoice, autocomplete.getInput().getAttribute("value"));
    }

    private static void addClientModePage(ComponentDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <s:autocomplete id='autocomplete' mode='client' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.body("    </s:autocomplete>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "client.xhtml");
    }

    private static void addAjaxModePage(ComponentDeployment deployment) {
        FaceletAsset p = new FaceletAsset();

        p.body("<h:form id='form'>");
        p.body("    <s:autocomplete id='autocomplete' mode='ajax' autocompleteList='#{autocompleteBean.suggestions}'>");
        p.body("    </s:autocomplete>");
        p.body("</h:form>");

        deployment.archive().addAsWebResource(p, "ajax.xhtml");
    }
}
