package org.richfaces.bootstrap.less;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LessResponseWriterTest {

    @Mock
    ResponseWriter wrapped;

    @Mock
    UIComponent component;

    @Test
    public void when_writing_non_link_element_then_write_through() throws IOException {

        // having
        LessResponseWriter writer = new LessResponseWriter(wrapped);

        // then
        writer.startElement("body", component);
        writer.writeAttribute("style", "xyz", "style");

        // verify
        InOrder inOrder = Mockito.inOrder(wrapped);
        inOrder.verify(wrapped).startElement("body", component);
        inOrder.verify(wrapped).writeAttribute("style", "xyz", "style");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void when_writing_link_attribute_then_the_attributes_are_not_written_immediately() throws IOException {

        // having
        LessResponseWriter writer = new LessResponseWriter(wrapped);

        // then
        writer.startElement("link", component);
        writer.writeAttribute("rel", "stylesheet", "rel");
        writer.writeAttribute("type", "text/css", "type");

        // verify
        InOrder inOrder = Mockito.inOrder(wrapped);
        inOrder.verify(wrapped).startElement("link", component);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void when_writing_link_attribute_then_the_attributes_are_written_on_the_end_of_element() throws IOException {

        // having
        LessResponseWriter writer = new LessResponseWriter(wrapped);

        // then
        writer.startElement("link", component);
        writer.writeAttribute("rel", "stylesheet", "rel");
        writer.writeAttribute("type", "text/css", "type");
        writer.endElement("link");

        // verify
        InOrder inOrder = Mockito.inOrder(wrapped);
        inOrder.verify(wrapped).startElement("link", component);
        inOrder.verify(wrapped).writeAttribute("rel", "stylesheet", "rel");
        inOrder.verify(wrapped).writeAttribute("type", "text/css", "type");
        inOrder.verify(wrapped).endElement("link");
        inOrder.verifyNoMoreInteractions();
    }
    
    @Test
    public void when_writing_link_attribute_and_its_href_points_to_less_resource_then_write_less_stylesheet_rel() throws IOException {

        // having
        LessResponseWriter writer = new LessResponseWriter(wrapped);

        // then
        writer.startElement("link", component);
        writer.writeAttribute("type", "text/css", "type");
        writer.writeAttribute("rel", "stylesheet", "rel");
        writer.writeURIAttribute("href", "styles.less.jsf", "href");
        writer.endElement("link");

        // verify
        InOrder inOrder = Mockito.inOrder(wrapped);
        inOrder.verify(wrapped).startElement("link", component);
        inOrder.verify(wrapped).writeAttribute("type", "text/css", "type");
        inOrder.verify(wrapped).writeAttribute("rel", "stylesheet/less", "rel");
        inOrder.verify(wrapped).writeURIAttribute("href", "styles.less.jsf", "href");
        inOrder.verify(wrapped).endElement("link");
        inOrder.verifyNoMoreInteractions();
    }
}
