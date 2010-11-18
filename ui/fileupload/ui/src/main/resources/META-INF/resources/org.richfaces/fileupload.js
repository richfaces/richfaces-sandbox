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
(function(richfaces, jQuery) {
	var ITEM_STATE = {
		NEW: {},
		IN_PROGRESS: {},
		DONE: {}
	};
	
	var ITEM_HTML = '<div class="rf-fu-itm"><span class="rf-fu-itm-lft"><span class="rf-fu-itm-lbl"/></span>'
		+ '<span class="rf-fu-itm-rgh"><a href="javascript:void(0)" class="rf-fu-itm-lnk"/></span></div>';
	
    richfaces.ui = richfaces.ui || {};

	richfaces.ui.FileUpload = richfaces.BaseComponent.extendClass({
	
	    name: "FileUpload",
	    
	    items: [],
	    
	    init: function(id) {
	        this.id = id;
	        this.element = jQuery(this.attachToDom());
	        var header = this.element.children(".rf-fu-hdr:first");
	        var leftButtons = header.children(".rf-fu-btns-lft:first");
	        this.addButton = leftButtons.children(".rf-fu-btn-add:first");
	        this.uploadButton = leftButtons.children(".rf-fu-btn-upl:first");
	        this.clearButton = header.children(".rf-fu-btns-rgh:first").children(".rf-fu-btn-clr:first");
	        this.inputContainer = this.addButton.find(".rf-fu-inp-cntr:first");
	        this.input = this.inputContainer.children("input");
	        this.list = this.element.children(".rf-fu-lst:first");
	        this.cleanInput = this.input.clone();
	        this.addProxy =  jQuery.proxy(this.__addItem, this);
	        this.input.change(this.addProxy);
	        this.clearButton.click(jQuery.proxy(this.__removeAllItems, this));
	    },
	    
	    __addItem: function() {
	    	this.input.hide();
	        this.input.unbind("change", this.addProxy);
	    	var item = new Item(this);
	    	this.list.append(item.getJQuery());
	    	this.items.push(item);
	    	this.input = this.cleanInput.clone();
	    	this.inputContainer.append(this.input);
	        this.input.change(this.addProxy);
	    	this.__updateButtons();
	    },
	    
	    __removeItem: function(item) {
	    	item.input.remove();
	    	item.element.remove();
	    	this.items.splice(this.items.indexOf(item), 1);
	    	this.__updateButtons();
	    },
	    
	    __removeAllItems: function(item) {
	    	this.inputContainer.children(":not(:visible)").remove();
	    	this.list.empty();
	    	this.items.splice(0);
	    	this.__updateButtons();
	    },
	    
	    __updateButtons: function() {
	    	if (this.items.length) {
	    		var hide = true;
	    		for ( var i = 0; i < this.items.length && hide; i++) {
					if (this.items[i].state == ITEM_STATE.NEW) {
			    		this.uploadButton.css("display", "inline-block");
			    		hide = false;
					}
				}
	    		if (hide) {
		    		this.uploadButton.hide();
	    		}
	    		this.clearButton.css("display", "inline-block");
	    	} else {
	    		this.uploadButton.hide();
	    		this.clearButton.hide();
	    	}
	    },
	    
	    __getLinkText: function(state) {
	    	var text = "";
	    	if (state == ITEM_STATE.NEW) {
	    		text = "Delete";
	    	} else if (state == ITEM_STATE.DONE) {
	    		text = "Clear";
	    	}
	    	return text;
	    }
	});
	
	var Item = function(fileUpload) {
		this.fileUpload = fileUpload;
	};
	
	jQuery.extend(Item.prototype, {
		getJQuery: function() {
			this.state = ITEM_STATE.NEW;
			this.input = this.fileUpload.input;
			this.element = jQuery(ITEM_HTML);
			this.label = this.element.children(".rf-fu-itm-lft:first").children(".rf-fu-itm-lbl:first");
			this.link = this.element.children(".rf-fu-itm-rgh:first").children("a");
			this.label.html(this.input.val());
			this.link.html(this.fileUpload.__getLinkText(this.state));
			
			this.link.click(jQuery.proxy(this.remove, this));
			return this.element;
		},
	    
	    remove: function() {
	    	this.fileUpload.__removeItem(this);
	    }
	});
}(window.RichFaces, jQuery));
