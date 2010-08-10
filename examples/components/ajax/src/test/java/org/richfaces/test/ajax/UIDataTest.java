/**
 * 
 */
package org.richfaces.test.ajax;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.FactoryFinder;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.richfaces.test.AbstractFacesTest;
import org.richfaces.test.DataBean;
import org.richfaces.test.staging.HttpMethod;

/**
 * @author asmirnov
 * 
 */
public class UIDataTest extends AbstractFacesTest {

	@Before
	public void setUpData() {

	}

	@Override
	protected void setupWebContent() {
		facesServer.addResource("/WEB-INF/faces-config.xml",
				"WEB-INF/faces-config.xml");
		facesServer.addResource("/test.xhtml", "test.xhtml");
	}
	
	@Override
	protected UIViewRoot setupView() {
		return null;
	}

	protected UIViewRoot createView() {
		facesContext.setCurrentPhaseId(PhaseId.RESTORE_VIEW);
		ELContext elContext = facesContext.getELContext();
		ExpressionFactory expressionFactory = application
				.getExpressionFactory();
		UIViewRoot root = super.setupView();
		UIComponent output = application
				.createComponent(UIOutput.COMPONENT_TYPE);
		output.setId(root.createUniqueId());
		root.getChildren().add(output);
		UIData data = (UIData) application
				.createComponent(UIData.COMPONENT_TYPE);
		data.setId("data");
		data.setVar("var");
		data.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{dataBean.data}",
						List.class));
		output.getChildren().add(data);
		UIComponent column = application
				.createComponent(UIColumn.COMPONENT_TYPE);
		data.getChildren().add(column);
		UIData enclosedData = (UIData) application
				.createComponent(UIData.COMPONENT_TYPE);
		enclosedData.setId("data1");
		enclosedData.setVar("var1");
		enclosedData.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{var.items}", List.class));
		column.getChildren().add(enclosedData);
		UIComponent enclosedColumn = application
				.createComponent(UIColumn.COMPONENT_TYPE);
		enclosedData.getChildren().add(enclosedColumn);
		UIForm form = (UIForm) application
				.createComponent(UIForm.COMPONENT_TYPE);
		form.setId("form");
		enclosedColumn.getChildren().add(form);
		UIInput input = (UIInput) application
				.createComponent(UIInput.COMPONENT_TYPE);
		input.setId("input");
		input
				.setValueExpression("value", expressionFactory
						.createValueExpression(elContext, "#{var1.name}",
								String.class));
		form.getChildren().add(input);
		// /
		input = (UIInput) application.createComponent(UIInput.COMPONENT_TYPE);
		input.setId("priceinput");
		input.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{var1.price}",
						Integer.class));
		enclosedColumn.getChildren().add(input);
		input = (UIInput) application.createComponent(UIInput.COMPONENT_TYPE);
		input.setId("nameinput");
		input.setValueExpression("value", expressionFactory
				.createValueExpression(elContext, "#{var.name}", String.class));
		column.getChildren().add(input);
		return root;
	}

	@Override
	protected void setupConnection() {
		this.connection.addRequestParameter(
				ResponseStateManager.VIEW_STATE_PARAM, "j_id1:j_id2");
		this.connection.addRequestParameter("data:3:nameinput", "foo");
		this.connection.addRequestParameter("data:5:data1:4:priceinput", "333");
		this.connection.addRequestParameter("data:6:data1:5:form",
				"data:6:data1:5:form");
		this.connection.addRequestParameter("data:6:data1:5:form:input", "bar");
		connection.setRequestMethod(HttpMethod.POST);
	}

	@Test
	public void testDecode() throws Exception {
		setupFacesRequest();
		facesContext.setViewRoot(createView());
		long startTime = System.currentTimeMillis();
		lifecycle.execute(facesContext);
		System.err.println("Execute time "
				+ (System.currentTimeMillis() - startTime) + "ms");
		lifecycle.render(facesContext);
		DataBean dataBean = (DataBean) this.facesServer.getSession()
				.getAttribute("dataBean");
		assertNotNull(dataBean);
		assertEquals("foo", dataBean.getData().get(3).getName());
		assertEquals(333, dataBean.getData().get(5).getItems().get(4)
				.getPrice());
		assertEquals("bar", dataBean.getData().get(6).getItems().get(5)
				.getName());
	}

/*	@Ignore
	public void testCycleRequest() throws Exception {
		setupFacesRequest();
		facesContext.setViewRoot(createView());
		lifecycle.execute(facesContext);
		lifecycle.render(facesContext);
		facesContext.release();
		facesContext = null;
		connection.finish();
		this.facesServer.getSession().removeAttribute("dataBean");
		setupFacesRequest();
		lifecycle.execute(facesContext);
		assertTrue(facesContext.getViewRoot().getChildCount()>0);
		DataBean dataBean = (DataBean) this.facesServer.getSession()
				.getAttribute("dataBean");
		assertNotNull(dataBean);
		assertEquals("foo", dataBean.getData().get(3).getName());
		assertEquals(333, dataBean.getData().get(5).getItems().get(4)
				.getPrice());
		assertEquals("bar", dataBean.getData().get(6).getItems().get(5)
				.getName());
	}
*/
}
