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

import static org.richfaces.component.AbstractTree.NODE_META_COMPONENT_ID;
import static org.richfaces.renderkit.util.AjaxRendererUtils.AJAX_FUNCTION_NAME;
import static org.richfaces.renderkit.util.AjaxRendererUtils.buildAjaxFunction;
import static org.richfaces.renderkit.util.AjaxRendererUtils.buildEventOptions;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;

import org.ajax4jsf.javascript.JSFunction;
import org.ajax4jsf.javascript.JSReference;
import org.richfaces.component.AbstractTree;
import org.richfaces.component.AbstractTreeNode;
import org.richfaces.component.MetaComponentResolver;
import org.richfaces.component.SwitchType;
import org.richfaces.event.TreeToggleEvent;
import org.richfaces.log.Logger;
import org.richfaces.log.RichfacesLogger;


/**
 * @author Nick Belaevski
 * 
 */
public abstract class TreeRendererBase extends RendererBase implements MetaComponentRenderer {

    static final Logger LOGGER = RichfacesLogger.RENDERKIT.getLogger();
    
    private static final JSReference TOGGLE_PARAMS = new JSReference("toggleParams");

    private static final JSReference TOGGLE_SOURCE = new JSReference("toggleSource");

    private static final String TREE_TOGGLE_ID_PARAM = "org.richfaces.Tree.TREE_TOGGLE_ID";
    
    private static final String NODE_TOGGLE_ID_PARAM = "org.richfaces.Tree.NODE_TOGGLE_ID";

    private static final String NEW_STATE_PARAM = "org.richfaces.Tree.NEW_STATE";

    enum NodeState {
        expanded("rf-tr-nd-exp", "rf-trn-hnd-exp", "rf-trn-ico-nd"), 
        collapsed("rf-tr-nd-colps", "rf-trn-hnd-colps", "rf-trn-ico-nd"), 
        leaf("rf-tr-nd-lf", "rf-trn-hnd-lf", "rf-trn-ico-lf");
        
        private String nodeClass;
        
        private String handleClass;
        
        private String iconClass;

        private NodeState(String nodeClass, String handleClass, String iconClass) {
            this.nodeClass = nodeClass;
            this.handleClass = handleClass;
            this.iconClass = iconClass;
        }
        
        public String getNodeClass() {
            return nodeClass;
        }
        
        public String getHandleClass() {
            return handleClass;
        }

        public String getIconClass() {
            return iconClass;
        }
        
    }
    
    static final class QueuedData {
        
        private Object rowKey;

        private boolean lastNode;

        private boolean expanded;
        
        private boolean encoded;
        
        public QueuedData(Object rowKey, boolean lastNode, boolean expanded) {
            this.rowKey = rowKey;
            this.lastNode = lastNode;
            this.expanded = expanded;
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
        
        public boolean isExpanded() {
            return expanded;
        }
    }
    
    public void encodeTree(FacesContext context, UIComponent component) throws IOException {
        AbstractTree tree = (AbstractTree) component;

        new TreeEncoderFull(context, tree).encode();
    }
    
    protected String getAjaxToggler(FacesContext context, UIComponent component) {
        AbstractTree tree = (AbstractTree) component;
        
        if (!SwitchType.ajax.equals(tree.getToggleMode())) {
            return null;
        }
        
        JSFunction ajaxFunction = buildAjaxFunction(context, component, AJAX_FUNCTION_NAME);
        AjaxEventOptions eventOptions = buildEventOptions(context, component);

        eventOptions.setAjaxComponent(TOGGLE_SOURCE);
        eventOptions.setClientParameters(TOGGLE_PARAMS);
        
        if (!eventOptions.isEmpty()) {
            ajaxFunction.addParameter(eventOptions);
        }

        return ajaxFunction.toScript();
    }
    
    @Override
    protected void doDecode(FacesContext context, UIComponent component) {
        super.doDecode(context, component);
        
    }
    
    /* (non-Javadoc)
     * @see org.richfaces.renderkit.MetaComponentRenderer#encodeMetaComponent(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
     */
    public void encodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId)
        throws IOException {
        
        if (NODE_META_COMPONENT_ID.equals(metaComponentId)) {
            AbstractTree tree = (AbstractTree) component;
            new TreeEncoderPartial(context, tree).encode();
        } else {
            throw new IllegalArgumentException(metaComponentId);
        }
        
        // TODO Auto-generated method stub
        
    }
    
    public void decodeMetaComponent(FacesContext context, UIComponent component, String metaComponentId) {
        if (NODE_META_COMPONENT_ID.equals(metaComponentId)) {
            final Map<String, String> map = context.getExternalContext().getRequestParameterMap();
            String toggleId = map.get(NODE_TOGGLE_ID_PARAM);
            if (component.getClientId(context).equals(toggleId)) {
                
                AbstractTree tree = (AbstractTree) component;
                AbstractTreeNode treeNode = tree.getTreeNodeComponent();
                boolean expanded = Boolean.valueOf(map.get(NEW_STATE_PARAM));
                if (tree.isExpanded() ^ expanded) {
                    new TreeToggleEvent(treeNode, expanded).queue();
                }
                
                PartialViewContext pvc = context.getPartialViewContext();
                if (pvc.isAjaxRequest()) {
                    pvc.getRenderIds().add(tree.getClientId(context) + MetaComponentResolver.META_COMPONENT_SEPARATOR_CHAR + "node");
                }
            }
        }
    }
    
}
