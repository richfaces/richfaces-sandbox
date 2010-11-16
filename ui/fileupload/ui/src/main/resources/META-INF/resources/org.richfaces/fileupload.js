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
    richfaces.ui = richfaces.ui || {};

	richfaces.ui.FileUpload = richfaces.BaseComponent.extendClass({
	
	    name: "FileUpload",
	    
	    init: function (id) {
	        this.id = id;
	        this.element = jQuery(this.attachToDom());
	        var header = this.element.children(".rf-fu-hdr:first");
	        var buttons = header.children(".rf-fu-btns-lft:first").children(".rf-fu-btn");
	        this.addButton = buttons.first();
//	        this.uploadButton = buttons.last();
//	        this.clearButton = header.children(".rf-fu-btns-rgh:first").children(".rf-fu-btn:first");
	        this.inputContainer = this.addButton.find(".rf-fu-inp-cntr:first");
	        this.input = this.inputContainer.children("input");
	        this.list = this.element.children(".rf-fu-lst:first");
	        this.cleanInput = this.input.clone();
	        this.addProxy =  jQuery.proxy(this.__add, this)
	        this.input.change(this.addProxy);
	    },
	    
	    __add: function () {
	    	this.input.hide();
	        this.input.unbind("change", this.addProxy);
	    	var item = new Item(this.input);
	    	this.list.append(item.html());
	    	this.input = this.cleanInput.clone();
	    	this.inputContainer.append(this.input);
	        this.input.change(this.addProxy);
	    }
	});
	
	var Item = function(input) {
		this.input = input;
	};
	
	jQuery.extend(Item.prototype, {
		html: function() { //TODO Optimize concatenation of strings.
			return '<div class="rf-fu-itm">'
			+	'<span class="rf-fu-itm-lft">'
			+		'<span class="rf-fu-itm-lbl">' + this.input.val() + '</span>'
			+	'</span>'
			+	'<span class="rf-fu-itm-rgh">'
			+		'<a href="#" class="rf-fu-itm-lnk">Delete</a>'
			+	'</span>'
			+ '</div>';
		}
	});
}(window.RichFaces, jQuery));
