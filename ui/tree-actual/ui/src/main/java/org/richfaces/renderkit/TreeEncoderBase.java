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

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.richfaces.component.AbstractTree;
import org.richfaces.component.util.HtmlUtil;
import org.richfaces.renderkit.TreeRendererBase.NodeState;
import org.richfaces.renderkit.TreeRendererBase.QueuedData;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;

abstract class TreeEncoderBase {

    private static final String TREE_NODE_HANDLE_CLASS_ATTRIBUTE = "__treeNodeHandleClass";
    
    private static final String TREE_NODE_ICON_CLASS_ATTRIBUTE = "__treeNodeIconClass";
    
    protected final FacesContext context;
    
    protected final ResponseWriter responseWriter;
    
    protected final AbstractTree tree;

    private LinkedList<QueuedData> queuedData = new LinkedList<QueuedData>();
    
    public TreeEncoderBase(FacesContext context, AbstractTree tree) {
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
                
                writeTreeNodeStartElement(NodeState.expanded, data.isLastNode());
                
                data.setEncoded(true);
            }
        }
         
        queuedData.add(new QueuedData(rowKey, isLastNode));
        
        tree.setRowKey(context, rowKey);

        boolean iterateChildren = tree.isExpanded();
        
        if (iterateChildren) {
            encodeTree(tree.getChildrenIterator(context, rowKey));
        }

        QueuedData data = queuedData.removeLast();
        if (!data.isEncoded()) {
            NodeState nodeState = iterateChildren ? NodeState.leaf : NodeState.collapsed;
            writeTreeNodeStartElement(nodeState, data.isLastNode());
        }
        
        writeTreeNodeEndElement();
    }
    
    protected void writeTreeNodeStartElement(NodeState nodeState, boolean isLast) throws IOException {
        context.getAttributes().put(TREE_NODE_HANDLE_CLASS_ATTRIBUTE, nodeState.getHandleClass());
        context.getAttributes().put(TREE_NODE_ICON_CLASS_ATTRIBUTE, nodeState.getIconClass());
        
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

    public abstract void encode() throws IOException;
    
}