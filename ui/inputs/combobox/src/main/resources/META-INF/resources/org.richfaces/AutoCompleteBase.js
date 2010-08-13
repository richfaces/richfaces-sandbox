// TODO: move this extend to RichFaces.Event for exapmle
$.extend(RichFaces.Event, {
	bindScrollEventHandlers: function(element, handler, component) {
		var elements = [];
		element = RichFaces.getDomElement(element).parentNode;
		while (element && element!=window.document.body)
		{
			if (element.offsetWidth!=element.scrollWidth || element.offsetHeight!=element.scrollHeight)
			{
				elements.push(element);
				RichFaces.Event.bind(element, "scroll"+component.getNamespace(), handler, component);
			}
			element = element.parentNode;
		}
		return elements;
	},
	unbindScrollEventHandlers: function(elements, component) {
		RichFaces.Event.unbind(elements, component.getNamespace());
	}
});

(function (rf) {
	rf.KEYS = {
		BACKSPACE: 8,	
		TAB: 9,
		RETURN: 13,
		ESC: 27,
		PAGEUP: 33,
		PAGEDOWN: 34,
		LEFT: 37,
		UP: 38,
		RIGHT: 39,
		DOWN: 40,
		DEL: 46,
	}
})(RichFaces);

(function ($, rf) {

	rf.ui = rf.ui || {}; 
	
	// Constructor definition
	rf.ui.AutoCompleteBase = function(componentId, selectId, fieldId, options) {
		// call constructor of parent class
		$super.constructor.call(this, componentId);
		this.selectId = selectId;
		this.fieldId = fieldId;
		this.options = $.extend({}, defaultOptions, options);
		this.namespace = this.namespace || "."+rf.Event.createNamespace(this.name, this.selectId);
		this.currentValue = this.getInputValue();
		bindEventHandlers.call(this);
	};
	
	// Extend component class and add protected methods from parent class to our container
	rf.BaseComponent.extend(rf.ui.AutoCompleteBase);
	
	// define super class link
	var $super = rf.ui.AutoCompleteBase.$super;
	
	var defaultOptions = {
			changeDelay:8
	};
	
	var bindEventHandlers = function() {
		
		var inputEventHandlers = {};
		
		if (this.options.buttonId && !this.options.disabled) {
			inputEventHandlers["mousedown"+this.namespace] = onButtonShow;
			inputEventHandlers["mouseup"+this.namespace] = onSelectMouseUp;
			rf.Event.bindById(this.options.buttonId, inputEventHandlers, this);
		}
		
		inputEventHandlers = {};
		inputEventHandlers["focus"+this.namespace] = onFocus;
		inputEventHandlers["blur"+this.namespace] = onBlur;
		inputEventHandlers["click"+this.namespace] = onClick;
		inputEventHandlers[($.browser.opera ? "keypress" : "keydown")+this.namespace] = onKeyDown;
		rf.Event.bindById(this.fieldId, inputEventHandlers, this);
		
		inputEventHandlers = {};
		inputEventHandlers["mousedown"+this.namespace] = onSelectMouseDown;
		inputEventHandlers["mouseup"+this.namespace] = onSelectMouseUp;
		rf.Event.bindById(this.selectId, inputEventHandlers, this);
	};
	
	var onSelectMouseDown = function () {
		this.isMouseDown = true;
	};
	var onSelectMouseUp = function () {
		rf.getDomElement(this.fieldId).focus();
	};
	
	var onButtonShow = function (event) {
		this.isMouseDown = true;
		if (this.timeoutId) {
			window.clearTimeout(this.timeoutId);
			this.timeoutId = null;
			rf.getDomElement(this.fieldId).focus();
		}
		
		if (this.isVisible) {
			this.hide(event);
		} else {
			onShow.call(this, event);
		}
	};
	
	var onFocus = function (event) {
	};
	
	var onBlur = function (event) {
		if (this.isMouseDown) {
			rf.getDomElement(this.fieldId).focus();
			this.isMouseDown = false;
		} else if (this.isVisible && !this.isMouseDown) {
			var _this = this;
			this.timeoutId = window.setTimeout(function(){_this.hide();}, 200);
		}
	};
	
	var onClick = function (event) {
	};
	
	var onChange = function (event) {
		var value = this.getInputValue();
		var flag = value != this.currentValue;
		//TODO: is it needed to chesk keys?
		//TODO: we need to set value when autoFill used when LEFT or RIGHT was pressed
		if (event.which == rf.KEYS.LEFT || event.which == rf.KEYS.RIGHT || flag) {
			if (flag || this.isVisible) {
				this.__onChangeValue(event);
			}
			if (flag) {
				this.currentValue = this.getInputValue();
				if(value && value.length>=this.options.minChars){
					onShow.call(this, event);
				}
				
			}
		}
	};
	
	var onShow = function (event) {
		this.__updateState(event);
		this.show(event);
	};
	
	var onKeyDown = function (event) {
		switch(event.which) {
			case rf.KEYS.UP:
				event.preventDefault();
				if (this.isVisible) {
					this.__onKeyUp(event);
				}
				break;
			case rf.KEYS.DOWN:
				event.preventDefault();
				if (this.isVisible) {
					this.__onKeyDown(event);
				} else {
					onShow.call(this, event);
				}
				break;
			case rf.KEYS.PAGEUP:
				event.preventDefault();
				if (this.isVisible) {
					this.__onPageUp(event);
				}
				break;
			case rf.KEYS.PAGEDOWN:
				event.preventDefault();
				if (this.isVisible) {
					this.__onPageDown(event);
				}
				break;
			case rf.KEYS.RETURN:

				event.preventDefault();
				this.__onEnter(event);
				//TODO: bind form submit event handler to cancel form submit under the opera
				//cancelSubmit = true;
				this.hide();
				return false;
				break;
			case rf.KEYS.ESC:
				this.hide();
				break;
			default:
				if (!this.options.selectOnly) {
					var _this = this;
					window.clearTimeout(this.changeTimerId);
					this.changeTimerId = window.setTimeout(function(){onChange.call(_this, event);}, this.options.changeDelay)
				}
				break;
		}
	}
	
	/*
	 * public API functions definition
	 */
	var show = function (event, showButtonPressed) {
		if (!this.isVisible) {
			if (this.__onBeforeShow(event)!=false) {
				this.scrollElements = rf.Event.bindScrollEventHandlers(this.selectId, this.hide, this, this.namespace);
				if (this.options.attachToBody) {
					var element = rf.getDomElement(this.selectId);
					this.parentElement = element.parentNode;
					$(element).detach().appendTo("body");
				}
				$(rf.getDomElement(this.selectId)).setPosition({id: this.fieldId}, {type:"DROPDOWN", offset:[0,20]}).show();
				this.isVisible = true;
				this.__onShow(event, showButtonPressed);
			}
		}
	};
	var hide = function (event) {
		if (this.isVisible) {
			rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
			this.scrollElements = null;
				$(rf.getDomElement(this.selectId)).hide();
				this.isVisible = false;
				if (this.options.attachToBody && this.parentElement) {
				$(rf.getDomElement(this.selectId)).detach().appendTo(this.parentElement);
				this.parentElement = null;
			}
		    this.__onHide(event);
		}
	};
	
	/*
	 * Prototype definition
	 */
	$.extend(rf.ui.AutoCompleteBase.prototype, (function () {
		return {
			/*
			 * public API functions
			 */
 			name:"AutoCompleteBase",
 			show: show,
 			hide: hide,
 			getNamespace: function () {
 				return this.namespace;
 			},
 			getInputValue: function () {
 				return this.fieldId ? rf.getDomElement(this.fieldId).value : "";
 			},
 			setInputValue: function (value) {
 				this.currentValue = this.__updateInputValue(value); 
 			},
 			/*
 			 * Protected methods
 			 */
 			__updateInputValue: function (value) {
 				if (this.fieldId) {
 					 rf.getDomElement(this.fieldId).value = value;
 					 return value;
 				} else {
 					return "";
 				}
 			},
 			/*
 			 * abstract protected methods
 			 */
 			__onChangeValue: function (event) {
 			},			
 			__onKeyUp: function () {
 			},
 			__onKeyDown: function () {
 			},
 			__onPageUp: function () {
 			},
 			__onPageDown: function () {
 			},
 			__onBeforeShow: function () {
 			},
 			__onShow: function () {
 			},
 			__onHide: function () {
 			},
 			/*
 			 * Destructor
 			 */
 			destroy: function () {
 				this.parentNode = null;
 				if (this.scrollElements) {
 					rf.Event.unbindScrollEventHandlers(this.scrollElements, this);
 					this.scrollElements = null;
 				}
 				this.options.buttonId && rf.Event.unbindById(this.options.buttonId, this.namespace);
 				rf.Event.unbindById(this.fieldId, this.namespace);
 				rf.Event.unbindById(this.selectId, this.namespace);
 				$super.destroy.call(this);
 			}
		};
	})());
})(jQuery, RichFaces);