/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.renderkit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractCollapsibleSubTable;
import org.richfaces.component.AbstractColumn;
import org.richfaces.component.AbstractDataTableExt;
import org.richfaces.component.Row;
import org.richfaces.component.UIDataAdaptor;
import org.richfaces.component.UIDataTableBase;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.util.AjaxRendererUtils;

/**
 * @author Anton Belevich
 */
@JsfRenderer(type = "org.richfaces.DataTableExtRenderer", family = AbstractDataTableExt.COMPONENT_FAMILY)
@ResourceDependencies({ @ResourceDependency(library = "org.richfaces", name = "base-component.reslib"),
    @ResourceDependency(library = "org.richfaces", name = "datatable.js"),
    @ResourceDependency(library = "org.richfaces", name = "datatable.ecss") })
public class DataTableExtRenderer extends AbstractTableRenderer {
    private static final String BEHAVIOR_EVENT_NAME = "javax.faces.behavior.event";
    private static final String ROW = "row";

    private class DataTableHiddenEncodeStrategy implements EncodeStrategy {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            AbstractDataTableExt dataTable = (AbstractDataTableExt) component;

            writer.startElement(HtmlConstants.TBODY_ELEMENT, dataTable);
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, dataTable.getContainerClientId(context) + HIDDEN_CONTAINER_ID,
                null);
            writer.startElement(HtmlConstants.TR_ELEMENT, dataTable);
            writer.startElement(HtmlConstants.TD_ELEM, dataTable);
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, "display: none", null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
            writer.endElement(HtmlConstants.TD_ELEM);
            writer.endElement(HtmlConstants.TR_ELEMENT);
            writer.endElement(HtmlConstants.TBODY_ELEMENT);
        }
    }

    ;

    private class RichHeaderEncodeStrategy implements EncodeStrategy {
        public void begin(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params)
            throws IOException {
            AbstractColumn column = (AbstractColumn) component;
            writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, column.getClientId(context), null);
        }

        public void end(ResponseWriter writer, FacesContext context, UIComponent component, Object[] params) throws IOException {
        }
    }

    protected void doDecode(FacesContext context, final UIComponent component) {
        super.doDecode(context, component);

        if ((null == context) || (null == component)) {
            throw new NullPointerException();
        }

        if (component instanceof ClientBehaviorHolder) {
            final Map<String, List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors();

            if (behaviors == null || behaviors.isEmpty()) {
                return;
            }

            Map<String, String> parametersMap = context.getExternalContext().getRequestParameterMap();
            final String behaviorEvent = parametersMap.get(BEHAVIOR_EVENT_NAME);

            if (behaviorEvent == null || !behaviorEvent.startsWith(ROW)) {
                return;
            }

            String behaviorSourceId = RenderKitUtils.getBehaviorSourceId(context);

            ((UIDataAdaptor) component).invokeOnRow(context, behaviorSourceId, new ContextCallback() {
                public void invokeContextCallback(FacesContext context, UIComponent target) {
                    if (target.equals(component)) {
                        List<ClientBehavior> behaviorsForEvent = behaviors.get(behaviorEvent);
                        if (behaviorsForEvent != null && !behaviorsForEvent.isEmpty()) {
                            for (ClientBehavior behavior : behaviorsForEvent) {
                                behavior.decode(context, component);
                            }
                        }
                    }
                }
            });
        }
    }

    public void encodeTableStructure(ResponseWriter writer, FacesContext context, UIDataTableBase dataTable) throws IOException {
        if (dataTable instanceof AbstractDataTableExt) {
            encodeStyle(writer, context, dataTable, null);
            encodeCaption(writer, context, (AbstractDataTableExt) dataTable);
            // TODO nick - do we need this element if "columnsWidth" is absent?
            writer.startElement(HtmlConstants.COLGROUP_ELEMENT, dataTable);

            int columns = getColumnsCount(dataTable.columns());
            writer.writeAttribute(HtmlConstants.SPAN_ELEM, String.valueOf(columns), null);
            String columnsWidth = (String) dataTable.getAttributes().get("columnsWidth");

            if (columnsWidth != null) {

                String[] widths = columnsWidth.split(",");
                for (int i = 0; i < widths.length; i++) {
                    writer.startElement(HtmlConstants.COL_ELEMENT, dataTable);
                    writer.writeAttribute(HtmlConstants.WIDTH_ATTRIBUTE, widths[i], null);
                    writer.endElement(HtmlConstants.COL_ELEMENT);
                }
            }

            writer.endElement(HtmlConstants.COLGROUP_ELEMENT);
        }
    }

    @Override
    public void encodeBeforeRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
        if (encodeParentTBody) {
            if (partialUpdate) {
                partialStart(facesContext, dataTableBase.getContainerClientId(facesContext) + ":tb");
            }
            encodeTableBodyStart(writer, facesContext, dataTableBase);
        }
    }

    @Override
    public void encodeAfterRows(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase,
        boolean encodeParentTBody, boolean partialUpdate) throws IOException {
        if (encodeParentTBody) {
            encodeTableBodyEnd(writer);
            if (partialUpdate) {
                partialEnd(facesContext);
            }
        }
    }

    public void encodeRow(ResponseWriter writer, FacesContext facesContext, RowHolderBase holder) throws IOException {
        RowHolder rowHolder = (RowHolder) holder;
        Row row = rowHolder.getRow();

        AbstractDataTableExt dataTable = (AbstractDataTableExt) row;

        putRowStylesIntoContext(facesContext, rowHolder);

        boolean partialUpdate = rowHolder.isUpdatePartial();
        boolean parentTbodyStart = rowHolder.isEncodeParentTBody();
        boolean tbodyStart = parentTbodyStart;

        rowHolder.setRowStart(true);

        Iterator<UIComponent> components = row.columns();
        int columnNumber = 0;
        boolean rowBegun = false;
        while (components.hasNext()) {
            UIComponent child = components.next();
            if (child.isRendered()) {
                rowBegun = true;
                if (child instanceof Row) {
                    boolean isSubtable = (child instanceof AbstractCollapsibleSubTable);
                    // new row -> close </tr>
                    if (rowHolder.getProcessCell() != 0) {
                        encodeRowEnd(writer);

                        if (isSubtable) {
                            encodeTableBodyEnd(writer);
                            tbodyStart = false;

                            if (partialUpdate) {
                                partialEnd(facesContext);
                            }
                        }
                    }

                    rowHolder.nextCell();

                    if (isSubtable && partialUpdate) {
                        String id = dataTable.getRelativeClientId(facesContext) + ":" + child.getId() + ":c";
                        partialStart(facesContext, id);
                    }

                    if (!isSubtable && !parentTbodyStart && !tbodyStart) {
                        encodeTableBodyStart(writer, facesContext, dataTable);
                        rowHolder.setRowStart(true);
                        tbodyStart = true;
                    }

                    child.encodeAll(facesContext);

                    if (!isSubtable) {
                        encodeRowEnd(writer);
                        if (!components.hasNext()) {
                            if (!parentTbodyStart && tbodyStart) {
                                encodeTableBodyEnd(writer);
                                tbodyStart = false;
                            }
                        }
                        rowHolder.setRowStart(true);
                        rowHolder.resetProcessCell();
                    }

                    if (isSubtable && partialUpdate) {
                        partialEnd(facesContext);
                    }
                } else if (child instanceof UIColumn) {

                    if (!parentTbodyStart && !tbodyStart) {
                        if (partialUpdate) {
                            partialStart(facesContext, dataTable.getRelativeClientId(facesContext) + ":tb");
                        }

                        encodeTableBodyStart(writer, facesContext, dataTable);
                        rowHolder.setRowStart(true);
                        tbodyStart = true;
                    }

                    child.getAttributes().put(COLUMN_CLASS, getColumnClass(rowHolder, columnNumber));
                    encodeColumn(facesContext, writer, (UIColumn) child, rowHolder);
                    columnNumber++;

                    if (!components.hasNext()) {
                        encodeRowEnd(writer);

                        if (!parentTbodyStart && tbodyStart) {
                            encodeTableBodyEnd(writer);
                            tbodyStart = false;

                            if (partialUpdate) {
                                partialEnd(facesContext);
                            }
                        }
                    }
                }
            } else if (!components.hasNext() && rowBegun) {
                encodeRowEnd(writer);

                if (!parentTbodyStart && tbodyStart) {
                    encodeTableBodyEnd(writer);
                    tbodyStart = false;

                    if (partialUpdate) {
                        partialEnd(facesContext);
                    }
                }
            }
        }
    }

    public boolean encodeParentTBody(UIDataTableBase dataTableBase) {
        Iterator<UIComponent> iterator = dataTableBase.columns();
        while (iterator.hasNext()) {
            if (iterator.next() instanceof Row) {
                return false;
            }
        }
        return true;
    }

    protected void doEncodeBegin(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        UIDataTableBase dataTable = (UIDataTableBase) component;
        encodeTableStart(writer, context, dataTable);
        encodeTableFacets(writer, context, dataTable);
    }

    protected void doEncodeEnd(ResponseWriter writer, FacesContext context, UIComponent component) throws IOException {
        encodeTableEnd(writer);
    }

    protected Class<? extends UIComponent> getComponentClass() {
        return AbstractDataTableExt.class;
    }

    public void encodeCaption(ResponseWriter writer, FacesContext context, AbstractDataTableExt dataTable) throws IOException {
        UIComponent caption = dataTable.getCaption();

        if (caption == null) {
            return;
        }

        if (!caption.isRendered()) {
            return;
        }

        writer.startElement(HtmlConstants.CAPTION_ELEMENT, dataTable);

        String captionClass = (String) dataTable.getAttributes().get("captionClass");
        String captionSkinClass = getCaptionSkinClass();

        captionClass = HtmlUtil.concatClasses(captionClass, captionSkinClass);
        writer.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, captionClass, "captionClass");

        String captionStyle = (String) dataTable.getAttributes().get("captionStyle");
        if (captionStyle != null && captionStyle.trim().length() != 0) {
            writer.writeAttribute(HtmlConstants.STYLE_ATTRIBUTE, captionStyle, "captionStyle");
        }

        caption.encodeAll(context);

        writer.endElement(HtmlConstants.CAPTION_ELEMENT);
    }

    public EncodeStrategy getHeaderEncodeStrategy(UIComponent column, String facetName) {
        return (column instanceof AbstractColumn && UIDataTableBase.HEADER.equals(facetName)) ? new RichHeaderEncodeStrategy()
            : new SimpleHeaderEncodeStrategy();
    }

    public boolean containsThead() {
        return true;
    }

    public void encodeClientScript(ResponseWriter writer, FacesContext facesContext, UIDataTableBase dataTableBase)
        throws IOException {
        AbstractDataTableExt dataTable = (AbstractDataTableExt) dataTableBase;

        writer.startElement(HtmlConstants.SCRIPT_ELEM, dataTable);
        writer.writeAttribute(HtmlConstants.TYPE_ATTR, HtmlConstants.JAVASCRIPT_TYPE, null);

        JSFunction function = new JSFunction("new RichFaces.ui.DataTable");
        function.addParameter(dataTable.getClientId(facesContext));

        AjaxOptions ajaxOptions = AjaxRendererUtils.buildEventOptions(facesContext, dataTable);

        Map<String, Object> options = new HashMap<String, Object>();
        options.put("ajaxEventOptions", ajaxOptions.getParameters());
        function.addParameter(options);

        writer.writeText(function.toScript(), null);
        writer.endElement(HtmlConstants.SCRIPT_ELEM);
    }

    @Override
    public void encodeHiddenInput(ResponseWriter writer, FacesContext context, UIDataTableBase component) throws IOException {
    }

    public String getTableSkinClass() {
        return "rf-dt";
    }

    public String getCaptionSkinClass() {
        return "rf-dt-cap";
    }

    public String getRowSkinClass() {
        return "rf-dt-r";
    }

    public String getFirstRowSkinClass() {
        return "rf-dt-fst-r";
    }

    public String getCellSkinClass() {
        return "rf-dt-c";
    }

    public String getHeaderSkinClass() {
        return "rf-dt-hdr";
    }

    public String getHeaderFirstSkinClass() {
        return "rf-dt-hdr-fst";
    }

    public String getHeaderCellSkinClass() {
        return "rf-dt-hdr-c";
    }

    public String getColumnHeaderSkinClass() {
        return "rf-dt-shdr";
    }

    public String getColumnHeaderFirstSkinClass() {
        return "rf-dt-shdr-fst";
    }

    public String getColumnHeaderCellSkinClass() {
        return "rf-dt-shdr-c";
    }

    public String getColumnFooterSkinClass() {
        return "rf-dt-sftr";
    }

    public String getColumnFooterFirstSkinClass() {
        return "rf-dt-sftr-fst";
    }

    public String getColumnFooterCellSkinClass() {
        return "rf-dt-sftr-c";
    }

    public String getFooterSkinClass() {
        return "rf-dt-ftr";
    }

    public String getFooterFirstSkinClass() {
        return "rf-dt-ftr-fst";
    }

    public String getFooterCellSkinClass() {
        return "rf-dt-ftr-c";
    }

    public String getNoDataClass() {
        return "rf-dt-nd";
    }

    @Override
    public String getNoDataCellClass() {
        return "rf-dt-nd-c";
    }

    @Override
    public String getTableBodySkinClass() {
        return "rf-dt-b";
    }

    public EncodeStrategy getHiddenContainerStrategy(UIDataTableBase dataTableBase) {
        return new DataTableHiddenEncodeStrategy();
    }
}
