/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import static org.richfaces.renderkit.util.AjaxRendererUtils.AJAX_FUNCTION_NAME;
import static org.richfaces.renderkit.util.AjaxRendererUtils.buildAjaxFunction;
import static org.richfaces.renderkit.util.AjaxRendererUtils.buildEventOptions;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.SwitchType;
import org.richfaces.component.TreeDecoderHelper;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

/**
 * @author Nick Belaevski
 * 
 */
public abstract class TreeRendererBase extends RendererBase {

    private static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();
    
    private static final String TOGGLE_DATA = "toggleData";

    private static final String TREE_TOGGLE_ID_PARAM = "org.richfaces.Tree.TREE_TOGGLE_ID";
    
    private static final String NODE_TOGGLE_ID_PARAM = "org.richfaces.Tree.NODE_TOGGLE_ID";

    private static final String NEW_STATE_PARAM = "org.richfaces.Tree.NEW_STATE";

    private static final JSReference JS_TREE_ID = new JSReference(TOGGLE_DATA + ".treeId");
    
    private static final JSReference JS_NODE_ID = new JSReference(TOGGLE_DATA + ".nodeId");

    private static final JSReference JS_NEW_STATE = new JSReference(TOGGLE_DATA + ".treeId");

    private static final class QueuedData {
        
        private Object rowKey;

        private boolean lastNode;

        private boolean encoded;
        
        public QueuedData(Object rowKey, boolean lastNode) {
            this.rowKey = rowKey;
            this.lastNode = lastNode;
        }

        public void setEncoded(boolean encoded) {
            this.encoded = encoded;
        }
        
        public boolean isEncoded() {
            return encoded;
        }
        
        public Object getRowKey() {
            return rowKey;
        }

        public boolean isLastNode() {
            return lastNode;
        }
    }
    
    private class TreeEncoder {

        private static final String TREE_NODE_HANDLE_CLASS_ATTRIBUTE = "__treeNodeHandleClass";
        
        private static final String TREE_NODE_ICON_CLASS_ATTRIBUTE = "__treeNodeIconClass";
        
        private FacesContext context;
        
        private ResponseWriter responseWriter;
        
        private AbstractTree tree;

        private LinkedList<QueuedData> queuedData = new LinkedList<QueuedData>();
        
        public TreeEncoder(FacesContext context, AbstractTree tree) {
            super();
            this.context = context;
            this.responseWriter = context.getResponseWriter();
            this.tree = tree;
        }

        protected void encodeTree(Iterator<Object> childrenIterator) throws IOException {
            Predicate<Object> renderedTreeNodeKeyPredicate = new Predicate<Object>() {
                public boolean apply(Object input) {
                    tree.setRowKey(input);
                    
                    if (!tree.isRowAvailable()) {
                        return false;
                    }
                    
                    return tree.getTreeNodeComponent() != null;
                }
            };
            
            UnmodifiableIterator<Object> filteredIterator = Iterators.filter(childrenIterator, renderedTreeNodeKeyPredicate);
            while (filteredIterator.hasNext()) {
                Object rowKey = filteredIterator.next();
                
                encodeTreeNode(rowKey, !filteredIterator.hasNext());
            }
        }
        
        protected void encodeTreeNode(Object rowKey, boolean isLastNode) throws IOException {
            if (!queuedData.isEmpty()) {
                QueuedData data = queuedData.getLast();
                if (!data.isEncoded()) {
                    tree.setRowKey(context, data.getRowKey());
                    
                    writeTreeNodeStartElement(data.isLastNode(), false);
                    
                    data.setEncoded(true);
                }
            }
             
            queuedData.add(new QueuedData(rowKey, isLastNode));
            
            tree.setRowKey(context, rowKey);
            
            encodeTree(tree.getChildrenIterator(context, rowKey));

            QueuedData data = queuedData.removeLast();
            if (!data.isEncoded()) {
                writeTreeNodeStartElement(data.isLastNode(), true);
            }
            
            writeTreeNodeEndElement();
        }
        
        protected void writeTreeNodeStartElement(boolean isLast, boolean isLeaf) throws IOException {
            context.getAttributes().put(TREE_NODE_HANDLE_CLASS_ATTRIBUTE, isLeaf ? "tree_handle_leaf" : "tree_handle_expanded");
            context.getAttributes().put(TREE_NODE_ICON_CLASS_ATTRIBUTE, isLeaf ? "tree_icon_leaf" : "tree_icon_node");
            
            responseWriter.startElement(HtmlConstants.DIV_ELEM, tree);
            responseWriter.writeAttribute(HtmlConstants.CLASS_ATTRIBUTE, 
                HtmlUtil.concatClasses("tree_node", isLast ? "tree_node_last" : null), 
                null);
            responseWriter.writeAttribute(HtmlConstants.ID_ATTRIBUTE, tree.getClientId(context), null);
            
            tree.getTreeNodeComponent().encodeAll(context);
        }

        protected void writeTreeNodeEndElement() throws IOException {
            responseWriter.endElement(HtmlConstants.DIV_ELEM);
        }

        public void encode() throws IOException {
            Object initialRowKey = tree.getRowKey();
            try {
                encodeTree(tree.getChildrenIterator(context, null));
            } finally {
                try {
                    tree.setRowKey(context, initialRowKey);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
    
    public void encodeTree(FacesContext context, UIComponent component) throws IOException {
        AbstractTree tree = (AbstractTree) component;

        new TreeEncoder(context, tree).encode();
    }
    
    protected String getAjaxToggler(FacesContext context, UIComponent component) {
        AbstractTree tree = (AbstractTree) component;
        
        if (!SwitchType.ajax.equals(tree.getToggleMode())) {
            return null;
        }
        
        JSFunction ajaxFunction = buildAjaxFunction(context, component, AJAX_FUNCTION_NAME);
        AjaxEventOptions eventOptions = buildEventOptions(context, component);

        eventOptions.setParameter(TREE_TOGGLE_ID_PARAM, JS_TREE_ID);
        eventOptions.setParameter(NODE_TOGGLE_ID_PARAM, JS_NODE_ID);
        eventOptions.setParameter(NEW_STATE_PARAM, JS_NEW_STATE);
        
        if (!eventOptions.isEmpty()) {
            ajaxFunction.addParameter(eventOptions);
        }

        return ajaxFunction.toScript();
    }
    
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);
        
        final Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String toggleId = map.get(TREE_TOGGLE_ID_PARAM);
        if (component.getClientId(context).equals(toggleId)) {
            
            String nodeId = map.get(NODE_TOGGLE_ID_PARAM) + UINamingContainer.getSeparatorChar(context) 
                + TreeDecoderHelper.HELPER_ID;
            
            VisitContext visitContext = createVisitContext(context, nodeId);
            component.visitTree(visitContext, new VisitCallback() {
                
                public VisitResult visit(VisitContext context, UIComponent target) {
                    AbstractTree tree = (AbstractTree) target.getParent();
                    AbstractTreeNode treeNode = tree.getTreeNodeComponent();
                    if (treeNode != null) {
                        boolean expanded = Boolean.valueOf(map.get(NEW_STATE_PARAM));
                        if (tree.isExpanded() ^ expanded) {
                            new TreeToggleEvent(treeNode, expanded);
                        }
                    }

                    return VisitResult.COMPLETE;
                }
            });
            
        }
    }

    private VisitContext createVisitContext(FacesContext context, String nodeId) {
        return VisitContext.createVisitContext(context, Collections.singleton(nodeId), 
            EnumSet.<VisitHint>of(VisitHint.SKIP_UNRENDERED));
    }
}
