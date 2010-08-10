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

	richfaces.ui.InputNumberSlider = richfaces.BaseComponent.extendClass({
	
	    name: "InputNumberSlider",
	    
	    delay: 200,
	    maxValue: 100,
	    minValue: 0,
	    step: 1,
	    tabIndex: 0,
	
	    decreaseSelectedClass: "rf-ins-db-s",
	    handleSelectedClass: "rf-ins-h-s",
	    increaseSelectedClass: "rf-ins-ib-s",

	    init: function (id, options, selectedClasses) {
			jQuery.extend(this, options);
	        this.range = this.maxValue - this.minValue;
	        this.id = id;
	        this.element = jQuery(this.attachToDom());
	        this.input = this.element.children(".rf-ins-i");
	        this.track = this.element.children(".rf-ins-ta").children(".rf-ins-t");
	        this.handle = this.track.children(".rf-ins-h, .rf-ins-h-d");
	        this.tooltip = this.element.children(".rf-ins-tt");
	        
	        this.__inputHandler();
	        
	        if (!this.disabled) {
		        this.decreaseButton = this.element.children(".rf-ins-db");
		        this.increaseButton = this.element.children(".rf-ins-ib");
	
	        	this.track[0].tabIndex = this.tabIndex;
	
				for (var i in selectedClasses) {
					this[i] += " " + selectedClasses[i];
				}
	        	var proxy = jQuery.proxy(this.__inputHandler, this)
		        this.input.change(proxy);
		        this.input.submit(proxy);
		        this.track.keydown(jQuery.proxy(this.__keydownHandler, this));
		        this.decreaseButton.mousedown(jQuery.proxy(this.__decreaseHandler, this));
		        this.increaseButton.mousedown(jQuery.proxy(this.__increaseHandler, this));
		        this.track.mousedown(jQuery.proxy(this.__mousedownHandler, this));
	        }
	    },
	
	    decrease: function (event) {
	    	this.setValue(this.value - this.step);
	    },
	    	    
	    increase: function (event) {
	    	this.setValue(this.value + this.step);
	    },
	    
	    setValue: function (value) {
	    	if (!this.disabled) {
	    		this.__setValue(value);
	    	}
	    },
	
	    __setValue: function (value) {
	    	if (!isNaN(value)) {
	        	value = Math.round(value / this.step) * this.step; //TODO Add normal support of float values. E.g. '0.3' should be instead of '0.30000000000000004'.
		        if (value > this.maxValue) {
		        	value = this.maxValue;
		        } else if (value < this.minValue) {
		        	value = this.minValue;
		        }
		        if (value != this.value) {
		        	this.input.val(value);
		        	var left = (value - this.minValue) * (this.track.width() - this.handle.width()) / this.range;
		        	this.handle.css("margin-left", left + "px");
		        	this.tooltip.text(value);
		        	this.tooltip.setPosition(this.handle,{from: 'LT', offset: [0, -3]}); //TODO Seems offset doesn't work now.
		        	this.value = value;
		        }
	    	}
	    },
	
	    __inputHandler: function () {
	    	var value = Number(this.input.val());
	    	if (isNaN(value)) {
	    		this.input.val(this.value);
	    	} else {
	    		this.__setValue(value);
	    	}
	    },
	
	    __keydownHandler: function (event) {
			if (event.keyCode == 37) { //LEFT
		    	this.__setValue(Number(this.input.val()) - this.step);
		    	event.preventDefault();
			} else if (event.keyCode == 39) { //RIGHT
		    	this.__setValue(Number(this.input.val()) + this.step);
		    	event.preventDefault();
			}
	    },
	    
	    __decreaseHandler: function (event) {
	    	var component = this;
    		component.decrease();
	    	this.intervalId = window.setInterval(function() {
	    		component.decrease();
	    	}, this.delay);
	    	jQuery(document).one("mouseup", true, jQuery.proxy(this.__clearInterval, this));
	    	this.decreaseButton.addClass(this.decreaseSelectedClass);
	    	event.preventDefault();
	    },
	    
	    __increaseHandler: function (event) {
	    	var component = this;
    		component.increase();
	    	this.intervalId = window.setInterval(function() {
	    		component.increase();
	    	}, this.delay);
	    	jQuery(document).one("mouseup",jQuery.proxy(this.__clearInterval, this));
	    	this.increaseButton.addClass(this.increaseSelectedClass);
	    	event.preventDefault();
	    },
	    
	    __clearInterval: function (event) {
	    	window.clearInterval(this.intervalId);
	    	if (event.data) { // decreaseButton
	    		this.decreaseButton.removeClass(this.decreaseSelectedClass);
	    	} else {
	    		this.increaseButton.removeClass(this.increaseSelectedClass);
	    	}
	    },
	    
	    __mousedownHandler: function (event) {
	    	this.__mousemoveHandler(event);
	    	this.track.focus();
	    	var jQueryDocument = jQuery(document);
	    	jQueryDocument.mousemove(jQuery.proxy(this.__mousemoveHandler, this));
	    	jQueryDocument.one("mouseup", jQuery.proxy(this.__mouseupHandler, this));
	    	this.handle.addClass(this.handleSelectedClass);
	    	this.tooltip.show();
	    },
	    
	    __mousemoveHandler: function (event) {
	    	var value = this.range * (event.pageX - this.track.position().left) / (this.track.width()
	    			- this.handle.width()) + this.minValue;
	        this.__setValue(value);
	    	event.preventDefault();
	    },
	    
	    __mouseupHandler: function () {
	    	this.handle.removeClass(this.handleSelectedClass);
	    	this.tooltip.hide();
	    	jQuery(document).unbind("mousemove", this.__mousemoveHandler);
	    }
	});
}(window.RichFaces, jQuery));