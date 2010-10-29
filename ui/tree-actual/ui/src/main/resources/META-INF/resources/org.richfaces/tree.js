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
		$(elt).children(".tree_node").each(function() {
			_this.addChild(new richfaces.ui.TreeNode(this));
		});
	}
	
	richfaces.ui = richfaces.ui || {};
    
	richfaces.ui.TreeNode = richfaces.BaseComponent.extendClass({

		name: "TreeNode",

		init: function (id) {
			this.id = id;

			this.__children = new Array();
			
			this.elt = $(this.attachToDom());
			
			this.handler = this.elt.find(" > .tree_item:first > .tree_handle:first");
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
			return !this.isLeaf() && !this.isCollapsed();
		},
		
		isCollapsed: function() {
			return !this.isLeaf() && this.elt.hasClass("tree_collapse");
		},
		
		isLeaf: function() {
			return this.handler.hasClass("tree_handle_leaf");
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
		
		collapse: function() {
			if (!this.isLeaf()) {
				var tree = this.getTree();
				
				switch (tree.getToggleMode()) {
					case 'client':
						this.elt.addClass("tree_collapse");
						this.handler.addClass("tree_handle_collapsed").removeClass("tree_handle_expanded");
					break;
					
					case 'ajax':
						tree.toggleByAjax(null, richfaces.getDomElement(this.id).id, false);
					break;

					case 'server':
						
					break;
				}
				
			}
		},

		expand: function() {
			if (!this.isLeaf()) {
				var tree = this.getTree();

				switch (tree.getToggleMode()) {
					case 'client':
						this.elt.removeClass("tree_collapse");
						this.handler.removeClass("tree_handle_collapsed").addClass("tree_handle_expanded");
					break;
					
					case 'ajax':
						tree.toggleByAjax(null, richfaces.getDomElement(this.id).id, true);
					break;

					case 'server':
						
					break;
				}
			}
		},
		
		getTree: function() {
			var component = this;
			while (component && component.name != "Tree") {
				component = component.getParent();
			}
			return component;
		},
		
		destroy: function() {
			if (this.parent) {
				this.parent.removeChild(this);
			}
			
			this.clearChildren();
			
			this.elt = null;
			this.handler = null;
		}
	});

	richfaces.ui.Tree = richfaces.ui.TreeNode.extendClass({

		name: "Tree",

		init: function (id, options) {
			this.$super.init.call(this, id, options);
			
			this.__toggleMode = options.toggleMode || 'ajax';
			this.__selectionMode = options.selectionMode || 'ajax';
			
			if (options.ajaxToggler) {
				this.__ajaxToggler = new Function("event", "nodeId", "newState", options.ajaxToggler);
			}
		},

		destroy: function() {
			this.$super.destroy();
			
			this.__ajaxToggler = null;
		},
		
		toggleByAjax: function(event, nodeId, newState) {
			this.__ajaxToggler(event, nodeId, newState);
		},
		
		getToggleMode: function() {
			return this.__toggleMode;
		},
		
		getSelectionMode: function() {
			return this.__selectionMode;
		}
	});

}(jQuery, RichFaces));