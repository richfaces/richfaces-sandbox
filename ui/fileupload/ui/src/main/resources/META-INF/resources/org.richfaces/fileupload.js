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

    var UID = "rf_fu_uid";
    
    var ITEM_HTML = '<div class="rf-fu-itm">'
    	+ '<span class="rf-fu-itm-lft"><span class="rf-fu-itm-lbl"/><span class="rf-fu-itm-st"/></span>'
		+ '<span class="rf-fu-itm-rgh"><a href="javascript:void(0)" class="rf-fu-itm-lnk"/></span></div>';
	
    richfaces.ui = richfaces.ui || {};

	richfaces.ui.FileUpload = richfaces.BaseComponent.extendClass({
	
	    name: "FileUpload",
	    
	    items: [],
	    
	    init: function(id) {
	        this.id = id;
	        this.element = jQuery(this.attachToDom());
	        this.form = this.element.parents("form:first");
	        this.iframe = this.element.children("iframe:first");
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
	        this.uploadButton.click(jQuery.proxy(this.__startUpload, this));
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
	    	if (!this.loadableItem && this.list.children(".rf-fu-itm").size()) {
				if (this.items.length) {
		    		this.uploadButton.css("display", "inline-block");
				} else {
					this.uploadButton.hide();
	    		}
	    		this.clearButton.css("display", "inline-block");
	    	} else {
	    		this.uploadButton.hide();
	    		this.clearButton.hide();
	    	}
	    },
	    
	    __startUpload: function() {
	    	this.loadableItem = this.items.shift();
	    	this.loadableItem.startUploading();
	    	this.__updateButtons();
	    	this.__submit();
	    },
	    
	    __submit: function() {
	    	var originalAction = this.form.attr("action");
	    	var originalEncoding = this.form.attr("encoding");
	    	var originalEnctype = this.form.attr("enctype");
	    	try {
	    		this.loadableItem.input.attr("name", this.id);
	    		this.form.attr("action", originalAction + "?" + UID + "=1");
	    		this.form.attr("encoding", "multipart/form-data");
	    		this.form.attr("enctype", "multipart/form-data");
	    		this.iframe.load(jQuery.proxy(this.__load, this));
	    		richfaces.submitForm(this.form, null, this.id);
	    	} finally {
	    		this.form.attr("action", originalAction);
	    		this.form.attr("encoding", originalEncoding);
	    		this.form.attr("enctype", originalEnctype);
	    		this.loadableItem.input.removeAttr("name");
			}
	    },
	    
	    __load: function(event) {
	    	var result = event.target.contentDocument.documentElement.id.split(":");
	    	if (this.loadableItem && UID + 1 == result[0]) {
	    		if ("done" == result[1]) {
	    			this.loadableItem.finishUploading();
	    			this.loadableItem = null;
	    	    	this.__updateButtons();
	    		}
	    	}
	    }
	});
	
	var Item = function(fileUpload) {
		this.fileUpload = fileUpload;
	};
	
	jQuery.extend(Item.prototype, {
		getJQuery: function() {
			this.input = this.fileUpload.input;
			this.element = jQuery(ITEM_HTML);
	        var leftArea = this.element.children(".rf-fu-itm-lft:first");
			this.label = leftArea.children(".rf-fu-itm-lbl:first");
			this.state = leftArea.children(".rf-fu-itm-st:first");
			this.link = this.element.children(".rf-fu-itm-rgh:first").children("a");
			this.label.html(this.input.val());
			this.link.html("Delete");
			
			this.link.click(jQuery.proxy(this.removeOrStop, this));
			return this.element;
		},
	    
	    removeOrStop: function() {
	    	this.input.remove();
	    	this.element.remove();
	    	this.fileUpload.__removeItem(this);
	    },
	    
	    startUploading: function() {
	    	this.state.css("display", "block");;
			this.link.html("");
	    },
	    
	    finishUploading: function() {
	    	this.input.remove();
	    	this.state.html("Done");
			this.link.html("Clear");
	    }
	});
}(window.RichFaces, jQuery));
