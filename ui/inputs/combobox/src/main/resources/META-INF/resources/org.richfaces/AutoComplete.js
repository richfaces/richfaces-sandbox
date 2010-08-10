(function ($, rf) {
	rf.utils = rf.utils || {};

	rf.utils.Cache = function (key, items, values) {
		this.key = key;
		this.cache = {}
		this.cache[this.key] = items || [];
		this.values = typeof values != "function" ? values || this.cache[this.key] : values(items);
	};

	var getItems = function (key) {
		var newCache = [];
		
		if (key.length < this.key.length) {
			return newCache;
		}

		if (this.cache[key]) {
			newCache = this.cache[key];
		} else {
			var itemsCache = this.cache[this.key];
			for (var i = 0; i<this.values.length; i++) {
				var value = this.values[i].toLowerCase();
				var p = value.indexOf(key.toLowerCase());
				if (p == 0) {
					newCache.push(itemsCache[i]);
				}
			}

			if ((!this.lastKey || key.indexOf(this.lastKey)!=0) && newCache.length > 0) {
				this.cache[key] = newCache;
				if (newCache.length==1) {
					this.lastKey = key;
				}
			}
		}

		return newCache;
	};
	
	var getItemValue = function (item) {
		return this.values[this.cache[this.key].index(item)];
	};
	
	var isCached = function (key) {
		return this.cache[key];
	};

	$.extend(rf.utils.Cache.prototype, (function () {
		return  {
			getItems: getItems,
			getItemValue: getItemValue,
			isCached: isCached
		};
	})());

})(jQuery, RichFaces);

(function ($, rf) {

	/*
	 * TODO: add user's event handlers call from options
	 * TODO: add fire events
	 */
	
	rf.ui = rf.ui || {};
	// Constructor definition
	rf.ui.AutoComplete = function(componentId, fieldId, options) {
		this.namespace = "."+rf.Event.createNamespace(this.name, this.id);
		this.options = {};
		// call constructor of parent class
		$super.constructor.call(this, componentId, componentId+ID.SELECT, fieldId, options);
		this.attachToDom(componentId);
		this.options = $.extend(this.options, defaultOptions, options);
		this.value = this.__getSubValue();
		this.index = -1;
		this.isFirstAjax = true;
		updateTokenOptions.call(this);
		bindEventHandlers.call(this);
		updateItemsList.call(this, "");
	};

	// Extend component class and add protected methods from parent class to our container
	rf.ui.AutoCompleteBase.extend(rf.ui.AutoComplete);

	// define super class link
	var $super = rf.ui.AutoComplete.$super;

	var defaultOptions = {
		selectedItemClass:'cb_select',
		autoFill:true,
		minChars:1,
		selectFirst:true,
		ajaxMode:true,
		tokens: ",",
		attachToBody:true
	};

	var ID = {
		SELECT:'List',
		ITEMS:'Items',
		VALUE:'Value'
	};
	
	var REGEXP_TRIM = /^[\n\s]*(.*)[\n\s]*$/;
	var REGEXP_TOKEN_LEFT;
	var REGEXP_TOKEN_RIGHT;
	
	var getData = function (nodeList) {
		var data = [];
		nodeList.each(function () {;
			data.push($(this).text().replace(REGEXP_TRIM, "$1"));
		});
		return data;
	}
	
	var updateTokenOptions = function () {
		this.useTokens = (typeof this.options.tokens == "string" && this.options.tokens.length>0);
		if (this.useTokens) {
			var escapedTokens = this.options.tokens.split('').join("\\");
			REGEXP_TOKEN_LEFT = new RegExp('[^'+escapedTokens+']+$','i');
			REGEXP_TOKEN_RIGHT = new RegExp('['+escapedTokens+']','i');
			this.hasSpaceToken = this.options.tokens.indexOf(' ')!=-1;
		};
	};

	var bindEventHandlers = function () {
		rf.Event.bind(rf.getDomElement(this.id+ID.ITEMS).parentNode, "click"+this.namespace+" mouseover"+this.namespace, onMouseAction, this);
	};

	var onMouseAction = function(event) {
		var element = $(event.target).closest(".rf-ac-i", event.currentTarget).get(0);

		if (element) {
			if (event.type=="mouseover") {
				var index = this.items.index(element);
				if (index!=this.index) {
					selectItem.call(this, index);
				}
			} else {
				this.__onChangeValue(event, getSelectedItemValue.call(this));
				rf.Selection.setCaretTo(rf.getDomElement(this.fieldId));
				this.hide(event);
			}
		}
	};

	var updateItemsList = function (value, fetchValues) {
		this.items = $(rf.getDomElement(this.id+ID.ITEMS)).find(".rf-ac-i");
		if (this.items.length>0) {
			this.cache = new rf.utils.Cache(value, this.items, fetchValues || getData);
		}
	};

	var scrollToSelectedItem = function() {
        var offset = 0;
        this.items.slice(0, this.index).each(function() {
			offset += this.offsetHeight;
		});
        var parentContainer = $(rf.getDomElement(this.id+ID.ITEMS)).parent();
        if(offset < parentContainer.scrollTop()) {
        	parentContainer.scrollTop(offset);
        } else {
        	offset+=this.items.get(this.index).offsetHeight;
        	if(offset - parentContainer.scrollTop() > parentContainer.get(0).clientHeight) {
        		parentContainer.scrollTop(offset - parentContainer.innerHeight());
            }
        }
	};

	var autoFill = function (inputValue, value) {
		if( this.options.autoFill) {
			var field = rf.getDomElement(this.fieldId);
			var start = rf.Selection.getStart(field);
			this.setInputValue(inputValue + value.substring(inputValue.length));
			var end = start+value.length - inputValue.length;
			rf.Selection.set(field, start, end);
		}
	};

	var callAjax = function(event, value) {
		
		$(rf.getDomElement(this.id+ID.ITEMS)).removeData().empty();
		rf.getDomElement(this.id+ID.VALUE).value = value;
		
		var _this = this;
		var ajaxSuccess = function (event) {
			updateItemsList.call(_this, _this.value, event.componentData && event.componentData[_this.id]);
			if (_this.isVisible && _this.options.selectFirst) {
				selectItem.call(_this, 0);
			}
		}
		
		var ajaxError = function () {
			//alert("error");
		}
		
		this.isFirstAjax = false;
		//caution: JSF submits inputs with empty names causing "WARNING: Parameters: Invalid chunk ignored." in Tomcat log
		var params = {};
		params[this.id + ".ajax"] = "1";

		rf.ajax(this.id, event, {parameters: params, error: ajaxError, complete:ajaxSuccess});
	};
	
	var selectItem = function(index, isOffset, noAutoFill) {
		if (this.items.length==0) return;
	
		if (this.index!=-1) {
				this.items.eq(this.index).removeClass(this.options.selectedItemClass);
		}

		if (index==undefined) {
			this.index = -1;
			return;
		}

		if (isOffset) {
			this.index += index;
		if ( this.index<0 ) {
			this.index = this.items.length - 1;
		} else if (this.index >= this.items.length) {
			this.index = 0;
		}
		} else {
			if (index<0) {
				index = 0;
			} else if (index>=this.items.length) {
				index = this.items.length - 1;
			}
			this.index = index;
		}
		var item = this.items.eq(this.index);
		item.addClass(this.options.selectedItemClass);
		scrollToSelectedItem.call(this);
		!noAutoFill && autoFill.call(this, this.value, getSelectedItemValue.call(this));
	};
	
	var onChangeValue = function (event, value) {
		selectItem.call(this);
		
		// value is undefined if called from AutoCompleteBase onChange
		var subValue = (typeof value == "undefined") ? this.__getSubValue() : value;
		
		// TODO: ajax call here if needed
		if ((!this.cache || subValue.length==0 || subValue.toLowerCase().indexOf(this.cache.key.toLowerCase())!=0) && 
				subValue.length>=this.options.minChars) {
			this.value = subValue;
			this.options.ajaxMode && callAjax.call(this, event, subValue);
			return;
		}
		
		var newItems = this.cache.getItems(subValue);
		this.items = $(newItems);
		//TODO: works only with simple markup, not with <tr>
		$(rf.getDomElement(this.id+ID.ITEMS)).empty().append(newItems);
		this.index = -1;
		this.value = subValue;
		if (this.options.selectFirst) {
			if (event.which == rf.KEYS.RETURN || event.type == "click") {
				this.setInputValue(subValue);
			} else {
				selectItem.call(this, 0, false, event.which == rf.KEYS.BACKSPACE || event.which == rf.KEYS.LEFT || event.which == rf.KEYS.RIGHT);
			}
		}
	};
	
	var getSelectedItemValue = function () {
		if ( this.index>=0) {
			var element = this.items.eq(this.index);
			return this.cache.getItemValue(element);
		}
		return undefined;
	};
	
	var getSubValue = function () {
		//TODO: add posibility to use space chars before and after tokens if space not a token char
		if (this.useTokens) {
			var field = rf.getDomElement(this.fieldId);
			var value = field.value;
			var cursorPosition = rf.Selection.getStart(field);
			var beforeCursorStr = value.substring(0, cursorPosition);
			var afterCursorStr = value.substring(cursorPosition);
			var r = REGEXP_TOKEN_LEFT.exec(beforeCursorStr);
			var result = "";
			if (r) {
				result = r[0];
			}
			r = afterCursorStr.search(REGEXP_TOKEN_RIGHT);
			if (r==-1) r = afterCursorStr.length;
			result += afterCursorStr.substring(0, r);

			return result;
		} else {
			return this.getInputValue();
		}
	};
	
	var updateInputValue = function (value) {
		var field = rf.getDomElement(this.fieldId);
		var inputValue = field.value;
		
		var cursorPosition = rf.Selection.getStart(field);
		var beforeCursorStr = inputValue.substring(0, cursorPosition);
		var afterCursorStr = inputValue.substring(cursorPosition);
		
		var pos = beforeCursorStr.search(REGEXP_TOKEN_LEFT);
		var startPos = pos!=-1 ? pos : beforeCursorStr.length;
		pos = afterCursorStr.search(REGEXP_TOKEN_RIGHT);
		var endPos = pos!=-1 ? pos : afterCursorStr.length;
		
		var beginNewValue = inputValue.substring(0, startPos) + value;
		cursorPosition = beginNewValue.length;
		field.value = beginNewValue + afterCursorStr.substring(endPos);
		field.focus();
		rf.Selection.setCaretTo(field, cursorPosition);
		return field.value;
	};
	
	/*
	 * Prototype definition
	 */
	$.extend(rf.ui.AutoComplete.prototype, (function () {
		return {
			/*
			 * public API functions
			 */
 			name:"AutoComplete",
 			/*
 			 * Protected methods
 			 */
 			__updateState: function () {
				var subValue = this.__getSubValue();
				// called from onShow method, not actually value changed
				if (this.items.length==0 && subValue.length>=this.options.minChars && this.isFirstAjax) {
					this.options.ajaxMode && callAjax.call(this, event, subValue);
				}
				return;
			},
 			__getSubValue: getSubValue,
 			__updateInputValue: function (value) {
				if (this.useTokens) {
					return updateInputValue.call(this, value);
				} else {
					return $super.__updateInputValue.call(this, value);
				}
 			},
 			__onChangeValue: onChangeValue,
 			/*
 			 * Override abstract protected methods
 			 */
 			__onKeyUp: function () {
 				selectItem.call(this, -1, true);
 			},
 			__onKeyDown: function () {
 				selectItem.call(this, 1, true);
 			},
 			__onPageUp: function () {

 			},
 			__onPageDown: function () {

 			},
 			__onBeforeShow: function (event) {
 			},
 			__onEnter: function (event) {
 				this.__onChangeValue(event, getSelectedItemValue.call(this));
				//rf.getDomElement(this.fieldId).blur();
				//rf.Selection.setCaretTo(rf.getDomElement(this.fieldId));
				//rf.getDomElement(this.fieldId).focus();
 			},
 			__onShow: function (event) {
 				if (event.which != rf.KEYS.BACKSPACE && this.items && this.items.length>0) {
 					if (this.index!=0 && this.options.selectFirst) {
 						selectItem.call(this, 0);
 					}
 				}
 			},
 			__onHide: function () {
 				selectItem.call(this);
 			},
 			/*
 			 * Destructor
 			 */
 			destroy: function () {
				//TODO: add all unbind
				this.items = null;
				this.cache = null;
				var itemsContainer = rf.getDomElement(this.id+ID.ITEMS);
				$(itemsContainer).removeData();
				rf.Event.unbind(itemsContainer.parentNode, this.namespace);
				$super.destroy.call(this);
 			}
		};
	})());
})(jQuery, RichFaces);