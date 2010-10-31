/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
(function($, richfaces) {

	var __initializeChildNodes = function(elt) {
		var _this = this;
		$(elt).children(".rf-tr-nd").each(function() {
			_this.addChild(new richfaces.ui.TreeNode(this));
		});
	}
	
	var NEW_NODE_TOGGLE_STATE = "__NEW_NODE_TOGGLE_STATE"
	
	var TREE_CLASSES = ["rf-tr-nd-colps", "rf-tr-nd-exp"];
	
	var TREE_HANDLE_CLASSES = ["rf-trn-hnd-colps", "rf-trn-hnd-exp"];
	
	richfaces.ui = richfaces.ui || {};
    
	richfaces.ui.TreeNode = richfaces.BaseComponent.extendClass({

		name: "TreeNode",

		init: function (id) {
			this.id = id;

			this.__children = new Array();
			
			this.elt = $(this.attachToDom());
			
			this.handler = this.elt.find(" > .rf-trn:first > .rf-trn-hnd:first");
			this.handler.click($.proxy(this.toggle, this));
			
			__initializeChildNodes.call(this, this.elt[0]);
		},
		
		getParent: function() {
			return this.__parent;
		},
		
		setParent: function(newParent) {
			this.__parent = newParent;
		},
		
		addChild: function(child, idx) {
			var start;
			if (typeof idx != 'undefined') {
				start = idx;
			} else {
				start = this.__children.length;
			}
			
			this.__children.splice(start, 0, child);
			child.setParent(this);
		},
		
		removeChild: function(child) {
			if (this.__children.length) {
				var idx = this.__children.indexOf(child);
				if (idx != -1) {
					var removedChildren = this.__children.splice(idx, 1);
					if (removedChildren) {
						for (var i = 0; i < removedChildren.length; i++) {
							removedChildren[i].setParent(undefined);
						}
					}
				}
			}
		},
		
		clearChildren: function() {
			for (var i = 0; i < this.__children.length; i++) {
				this.__children[i].setParent(undefined);
			}
			
			this.__children = new Array();
		},
		
		isExpanded: function() {
			return !this.isLeaf() && this.handler.hasClass("rf-trn-hnd-exp");
		},
		
		isCollapsed: function() {
			return !this.isLeaf() && this.handler.hasClass("rf-trn-hnd-colps");
		},
		
		isLeaf: function() {
			return this.handler.hasClass("rf-trn-hnd-lf");
		},
		
		toggle: function() {
			if (this.isLeaf()) {
				return;
			}
			
			if (this.isCollapsed()) {
				this.expand();
			} else {
				this.collapse();
			}
		},
		
		__updateClientToggleStateInput: function(newState) {
			if (!this.__clientStateInput) {
				this.__clientStateInput = $("<input type='hidden' />").appendTo(this.elt)
					.attr({name: this.elt.attr("id") + NEW_NODE_TOGGLE_STATE});
			}
			
			this.__clientStateInput.val(newState.toString());

		},
		
		__changeToggleState: function(newState) {
			if (!this.isLeaf()) {
				var tree = this.getTree();
				
				switch (tree.getToggleMode()) {
					case 'client':
						this.elt.addClass(TREE_CLASSES[newState ? 1 : 0]).removeClass(TREE_CLASSES[!newState ? 1 : 0]);
						this.handler.addClass(TREE_HANDLE_CLASSES[newState ? 1 : 0]).removeClass(TREE_HANDLE_CLASSES[!newState ? 1 : 0]);
						this.__updateClientToggleStateInput(newState);
					break;
					
					case 'ajax':
					case 'server':
						//TODO - event?
						tree.sendToggleRequest(null, richfaces.getDomElement(this.id).id, newState);
					break;
				}
				
			}
		},
		
		collapse: function() {
			this.__changeToggleState(false);
		},

		expand: function() {
			this.__changeToggleState(true);
		},
		
		getTree: function() {
			var component = this;
			while (component && component.name != "Tree") {
				component = component.getParent();
			}
			return component;
		},
		
		destroy: function() {
			this.__clientStateInput = null;
			
			if (this.parent) {
				this.parent.removeChild(this);
			}
			
			this.clearChildren();
			
			this.elt = null;
			this.handler = null;
		}
	});

	richfaces.ui.TreeNode.initNodeByAjax = function(nodeId) {
		var node = $(document.getElementById(nodeId));
		
		if (node.nextAll(".rf-tr-nd:first").length != 0) {
			node.removeClass("rf-tr-nd-last");
		}
		
		var parent = node.parent(".rf-tr-nd, .rf-tr");
		
		var idx = node.prevAll(".rf-tr-nd").length;
		
		var parentNode = richfaces.$(parent[0]);
		parentNode.addChild(new richfaces.ui.TreeNode(node[0]), idx);
	};
	
	var decoderHelperId;
	
	richfaces.ui.Tree = richfaces.ui.TreeNode.extendClass({

		name: "Tree",

		init: function (id, options) {
			this.$super.init.call(this, id, options);
			
			this.__toggleMode = options.toggleMode || 'ajax';
			this.__selectionMode = options.selectionMode || 'ajax';
			
			if (options.ajaxToggler) {
				this.__ajaxToggler = new Function("event", "toggleSource", "toggleParams", options.ajaxToggler);
			}
		},

		destroy: function() {
			this.$super.destroy();
			
			this.__ajaxToggler = null;
		},
		
		sendToggleRequest: function(event, toggleSource, newNodeState) {
			var clientParams = {};
			clientParams[toggleSource + NEW_NODE_TOGGLE_STATE] = newNodeState;
			
			if (this.__toggleMode == 'server') {
				var form = $(richfaces.getDomElement(this.id)).closest('form');
				richfaces.submitForm(form, clientParams);
			} else {
				this.__ajaxToggler(event, toggleSource + decoderHelperId, clientParams);
			}
		},
		
		getToggleMode: function() {
			return this.__toggleMode;
		},
		
		getSelectionMode: function() {
			return this.__selectionMode;
		}
	});

	richfaces.ui.Tree.setDecoderHelperId = function(id) {
		decoderHelperId = id;
	};
	
}(jQuery, RichFaces));