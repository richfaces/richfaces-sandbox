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
package org.richfaces.model;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.swing.tree.TreeNode;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.ExtendedDataModel;
import org.ajax4jsf.model.Range;

import com.google.common.collect.Iterators;

/**
 * @author Nick Belaevski
 * 
 */
public class TreeDataModelImpl extends ExtendedDataModel<TreeNode> {

    private static final SequenceRowKey<Integer> EMPTY_SEQUENCE_ROW_KEY = new SequenceRowKey<Integer>();

    private SwingTreeNodeImpl rootNode;

    private TreeNode selectedNode;
    
    private SequenceRowKey<Integer> selectedRowKey;
    
    private TreeNode walkNode;
    
    private SequenceRowKey<Integer> walkRowKey;
    
    private void setWalkContextData(TreeNode node, SequenceRowKey<Integer> key) {
        this.walkNode = node;
        this.walkRowKey = key;
    }
    
    private Iterator<TreeNode> findChildren(SequenceRowKey<Integer> compositeKey) {
        TreeNode treeNode = findNode(compositeKey);
        
        if (treeNode == null) {
            return Iterators.emptyIterator();
        }
        
        return Iterators.forEnumeration((Enumeration<TreeNode>) treeNode.children());
    }
    
    private TreeNode findNode(SequenceRowKey<Integer> compositeKey) {
        if (compositeKey == null) {
            return null;
        }
        
        TreeNode result = rootNode;
        
        for (Integer simpleKey : compositeKey.getSimpleKeys()) {
            int idx = simpleKey.intValue();
            
            if (idx < result.getChildCount()) {
                result = result.getChildAt(idx);
            } else {
                result = null;
                break;
            }
        }
        
        return result;
    }
    
    @Override
    public void setRowKey(Object key) {
        this.selectedRowKey = (SequenceRowKey<Integer>) key;

        if (walkRowKey != null && walkRowKey.equals(key)) {
            this.selectedNode = walkNode;
        } else {
            this.selectedNode = findNode(selectedRowKey);
        }
    }

    @Override
    public Object getRowKey() {
        return selectedRowKey;
    }

    @Override
    public boolean isRowAvailable() {
        return selectedNode != null;
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public TreeNode getRowData() {
        return selectedNode;
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#getRowIndex()
     */
    @Override
    public int getRowIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.faces.model.DataModel#setRowIndex(int)
     */
    @Override
    public void setRowIndex(int rowIndex) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Object getWrappedData() {
        return rootNode.getChildrenList();
    }

    @Override
    public void setWrappedData(Object data) {
        this.rootNode = new SwingTreeNodeImpl((List<TreeNode>) data);
    }

    private SequenceRowKey<Integer> castKeyAndWrapNull(Object rowKey) {
        if (rowKey == null) {
            return EMPTY_SEQUENCE_ROW_KEY;
        }
        
        return (SequenceRowKey<Integer>) rowKey;
    }
    
    public Iterator<Object> getChildrenIterator(FacesContext faces, Object rowKey) {
        SequenceRowKey<Integer> sequenceKey = castKeyAndWrapNull(rowKey);
        Iterator<TreeNode> itr = findChildren(sequenceKey);
        
        return new SequenceRowKeyIterator<TreeNode>(sequenceKey, itr);
    }

    /* (non-Javadoc)
     * @see org.ajax4jsf.model.ExtendedDataModel#walk(javax.faces.context.FacesContext, org.ajax4jsf.model.DataVisitor, org.ajax4jsf.model.Range, java.lang.Object)
     */
    @Override
    public void walk(FacesContext context, DataVisitor visitor, Range range, Object argument) {
        // TODO Auto-generated method stub
        
    }

}
