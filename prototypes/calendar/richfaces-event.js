/**
 * @author Pavel Yaschenko
 */

// TODO: add support to bind multiple events using type param as an object with eventType,function pairs // see bindById method
// TODO: update js docs 

(function($, richfaces) {
	
	/**
	  * RichFaces Event API container 
	  * @class
	  * @memberOf RichFaces
	  * @static
	  * @name Event
	  * */
	richfaces.Event = richfaces.Event || {};
	
	var getEventElement = function (selector) {
		if (!selector) {
			throw "RichFaces.Event: empty selector";
		}
		var element;
		if (RichFaces.BaseComponent && selector instanceof RichFaces.BaseComponent) {
			element = $(richfaces.getDomElement(selector.getEventElement()));
		} else {
			element = $(selector);
		}			

		return element;
	}
	
	var getHandlerWrapper = function (component, fn) {
		return function (e,d){
			return fn.call(component||this, e, this, d);
		};
	}
	
	var getMultipleHandlerWrapper = function (object, component) {
		var result = {};
		for (var type in object) {
			result[type] = getHandlerWrapper(component, object[type]);
		}
		return result;
	}
	
	$.extend(richfaces.Event, {
		/**
		  * @constant
		  * @name RichFaces.Event.RICH_NAMESPACE
		  * @type string
		  * */
		RICH_NAMESPACE : "RICH",
		
		/** 
		  * Attach an event handler to execute when the DOM is fully loaded.
		  * 
		  * @function
		  * @name RichFaces.Event.ready
		  * @param {function} fn - event handler
		  * @return {jQuery} document element wrapped by jQuery
		  * */
		ready : function(fn) {
			// TODO: not completed yet
			return $(document).ready(fn);
			/*
				function callback(jQueryReference) {
  					this; // document
			  	}
			*/
		},
		
		/** 
		  * Attach a handler to an event for the elements.
		  * @function
		  * @name RichFaces.Event.bind
		  *
		  * @param {string|DOMElement|jQuery} selector - jQuery elements selector
		  * @param {string} eventType - one or more JavaScript event types, such as "click" or "submit," or custom event names
		  * @param {function} fn - event handler
		  * @param {Object} [data] - component or object with additional data
		  * It is a context for an event handler 
		  * @return {function} function that binded to the element's event
		  * */		
		bind : function(selector, eventType, fn, component, data) {
			// eventType: namespace can be used, like onclick.rf.conponentName
			var f = getHandlerWrapper(component, fn);
			getEventElement(selector).bind(eventType, data, f);
			return f;
		},
		
		/** 
		  * Attach a handler to an event for the element by element id.
		  * @function
		  * @name RichFaces.Event.bindById
		  *
		  * @param {string} id - DOM element id
		  * @param {string} eventType - one or more JavaScript event types, such as "click" or "submit," or custom event names
		  * @param {function} fn - event handler
		  * @param {Object} [data] - component or object with additional data
		  * It is a context for an event handler 
		  * @return {function} function that binded to the element's event
		  * */	
		bindById : function(id, eventType, fn, component, data) {
			// eventType: namespace can be used, like onclick.rf.conponentName
			if (typeof eventType == "object") {
				// in this case fn == component object
				$(document.getElementById(id)).bind(getMultipleHandlerWrapper(eventType, fn), data);
			} else {
				var f = getHandlerWrapper(component, fn);
				$(document.getElementById(id)).bind(eventType, data, f);
			}
			return f;
		},		
		
		/** 
		  * Attach a handler to an event for the elements.
		  * The handler will be called only once when event happened.
		  * @function
		  * @name RichFaces.Event.bindOne
		  *
		  * @param {string|DOMElement|jQuery} selector - jQuery elements selector
		  * @param {string} eventType - one or more JavaScript event types, such as "click" or "submit," or custom event names
		  * @param {function} fn - event handler
		  * @param {Object} [data] - component or object with additional data
		  * It is a context for an event handler 
		  * @return {function} function that binded to the element's event
		  * */		
		bindOne: function(selector, eventType, fn, component, data) {
			// eventType: namespace can be used, like onclick.rf.conponentName
			var f = getHandlerWrapper(component, fn);
			getEventElement(selector).one(eventType, data, f);
			return f;
		},
		
		/** 
		  * Attach a handler to an event for the element by element id.
		  * The handler will be called only once when event happened.
		  * @function
		  * @name RichFaces.Event.bindOneById
		  *
		  * @param {string} id - DOM element id
		  * @param {string} eventType - one or more JavaScript event types, such as "click" or "submit," or custom event names
		  * @param {function} fn - event handler
		  * @param {Object} [data] - component or object with additional data
		  * It is a context for an event handler 
		  * @return {function} function that binded to the element's event
		  * */		
		bindOneById: function(id, eventType, fn, component, data) {
			// eventType: namespace can be used, like onclick.rf.conponentName
			var f = getHandlerWrapper(component, fn);
			$(document.getElementById(id)).one(eventType, data, f);
			return f;
		},
		
		/** 
		  * Remove a previously-attached event handler from the elements.
		  * @function
		  * @name RichFaces.Event.unbind
		  *
		  * @param {string|DOMElement|jQuery} selector - jQuery elements selector
		  * @param {string} [eventType] - one or more JavaScript event types, such as "click" or "submit," or custom event names
		  * @param {function} [fn] - event handler
		  * @return {jQuery} element wrapped by jQuery
		  * */
		unbind : function(selector, eventType, fn) {
			// eventType: namespace can be used, like onclick.rf.conponentName
			return getEventElement(selector).unbind(eventType, fn);
		},
		
		/** 
		  * Remove a previously-attached event handler from the elements by element id.
		  * The handler will be called only once when event happened.
		  * @function
		  * @name RichFaces.Event.unbindById
		  *
		  * @param {string} id - DOM element id
		  * @param {string} [eventType] - one or more JavaScript event types, such as "click" or "submit," or custom event names
		  * @param {function} [fn] - event handler
		  * @return {jQuery} element wrapped by jQuery
		  * */
		unbindById : function(id, eventType, fn) {
			// eventType: namespace can be used, like onclick.rf.conponentName
			return $(document.getElementById(id)).unbind(eventType, fn);
		},
		
		// TODO add jsdocs and qunits
		bindScrollEventHandlers: function(element, handler, component) {
			var elements = [];
			element = richfaces.getDomElement(element).parentNode;
			while (element && element!=window.document.body)
			{
				if (element.offsetWidth!=element.scrollWidth || element.offsetHeight!=element.scrollHeight)
				{
					elements.push(element);
					richfaces.Event.bind(element, "scroll"+component.getNamespace(), handler, component);
				}
				element = element.parentNode;
			}
			return elements;
		},
		unbindScrollEventHandlers: function(elements, component) {
			richfaces.Event.unbind(elements, "scroll"+component.getNamespace());
		},
		
		/** 
		  * Execute all handlers and behaviors attached to the matched elements for the given event type.
		  * @function
		  * @name RichFaces.Event.fire
		  *
		  * @param {string|DOMElement|jQuery} selector - jQuery elements selector
		  * @param {string} eventType - event type
		  * @param {Object} [data] - a object of additional parameters to pass to the event handler
		  * @return {jQuery} element wrapped by jQuery
		  * */
		fire : function(selector, eventType, data) {
			var event = $.Event(eventType);
			getEventElement(selector).trigger(eventType, data);
			return !event.isDefaultPrevented();
		},
		
		/** 
		  * The same as the fire method, but selects element by id.
		  * @function
		  * @name RichFaces.Event.fireById
		  *
		  * @param {string} id - DOM element id
		  * @param {string} eventType - event type
		  * @param {Object} [data] - a object of additional parameters to pass to the event handler
		  * @return {jQuery} element wrapped by jQuery
		  * */
		fireById : function(id, eventType, data) {
			var event = $.Event(eventType);
			$(document.getElementById(id)).trigger(event, data);
			return !event.isDefaultPrevented();
		},
		
		/** 
		  * The same as the fire method, but:
		  *  - does not cause the default behavior of an event to occur
		  *  - does not bubble up event
		  *  - call handler only for the first founded element
		  *  - returns whatever value that was returned by the handler
		  * @function
		  * @name RichFaces.Event.callHandler
		  *
		  * @param {string|DOMElement|jQuery} selector - jQuery elements selector
		  * @param {string} eventType - event type
		  * @param {Object} [data] - a object of additional parameters to pass to the event handler
		  * @return value that was returned by the handler
		  * */
		callHandler : function(selector, eventType, data) {
			return getEventElement(selector).triggerHandler(eventType, data);
		},
		
		/** 
		  * The same as the callHandler method, but selects element by id.
		  * @function
		  * @name RichFaces.Event.callHandlerById
		  *
		  * @param {string} id - DOM element id
		  * @param {string} eventType - event type
		  * @param {Object} [data] - a object of additional parameters to pass to the event handler
		  * @return value that was returned by the handler
		  *  */
		callHandlerById : function(id, eventType, data) {
			return $(document.getElementById(id)).triggerHandler(eventType, data);
		},
		
		/** 
		  * Create an event namespace for the components.
		  * @function
		  * @name RichFaces.Event.createNamespace
		  *
		  * @param {string} [componentName] - component name
		  * @param {string} [id] - element id
		  * @param {string} [prefix=RichFaces.Event.RICH_NAMESPACE] - namespace prefix
		  * @return {string} namespace string
		  *  */
		// TODO: rename argument names
		createNamespace : function(componentName, id, prefix) {
			var a = [];
			a.push(prefix || richfaces.Event.RICH_NAMESPACE);
			if (componentName) {
				a.push(componentName);
			}
			if (id) {
				a.push(id);
			}
			return a.join('.');
		}
	});
	
})(jQuery, window.RichFaces || (window.RichFaces={}));

/*
fn : function (eventObject, element) {
	this; // object passed as data to bind function or dom element if no data
	element; // dom element
	
}
*/

// 	API usage example:
// 		RichFaces.Event.bind(selector, type, fn, data);
