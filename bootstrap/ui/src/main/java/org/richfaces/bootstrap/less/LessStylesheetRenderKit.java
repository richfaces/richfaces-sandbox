package org.richfaces.bootstrap.less;

import java.io.Writer;

import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;

public class LessStylesheetRenderKit extends RenderKitWrapper {

    private RenderKit wrapped;

    public LessStylesheetRenderKit(RenderKit wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String characterEncoding) {
        return new LessResponseWriter(super.createResponseWriter(writer, contentTypeList, characterEncoding));
    }

    @Override
    public RenderKit getWrapped() {
        return wrapped;
    }

}