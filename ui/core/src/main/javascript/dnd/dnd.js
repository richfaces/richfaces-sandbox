/**
 * Document-wide object manipulating drag and drop events.
 * When drag is in process, <code>window.drag</code> object of class <code>DnD.Drag</code> holds its state.
 * Draggables should create DnD.Drag instance and pass it to DnD.startDrag call to initialize drag.
 * Dropzones should implement accept method to be able to get drag results.
 * @author Maksim Kaszynski
 */
var DnD = {
	
	CODE_ACCEPT : "accept",
	
	CODE_DEFAULT: "default",
	
	CODE_REJECT: "reject",
	
	/**
	 * Switch mouse observing to DnD mode
	 * @param {DnD.Drag} drag
	 */
	startDrag: function(drag) {
		if (!window.drag) {
			this.init();
			window.drag = drag;
			Event.observe(document, 'mousemove', this.mousemove);
			Event.observe(document, 'mouseup', this.mouseup);
		} else {
			alert('drag in progress');
		}
	},
	
	endDrag: function(event) {
		Event.stopObserving(document, 'mousemove', this.mousemove);
		Event.stopObserving(document, 'mouseup', this.mouseup);
		var drag = window.drag;
		if (drag) {
			window.drag = null;
			var dropzone = drag.dropzone;
			drag.source.endDrag(event, drag);
			if (dropzone) {
				dropzone.onbeforedrag(event, drag);
				if (dropzone.accept(drag)) {
					dropzone.drop(event, drag);
				}
				dropzone.onafterdrag(event);
			}
		}
	},
	
	updateDrag: function(event) {
		var drag = window.drag;
	    
	    if (!drag.source.onupdatedrag || (drag.source.onupdatedrag(event) != false)) {
		    var x = Event.pointerX(event);
		    var y = Event.pointerY(event);
			drag.indicator.position(x + 5, y + 14);
			Event.stop(event);
	    }
	},
	
	initialized : false,
	
	init: function() {
		if (!this.initialized) {
			this.mousemove = this.updateDrag.bindAsEventListener(this);
			this.mouseup = this.endDrag.bindAsEventListener(this);
			this.initialized = true;
		}	
			
	}
	


};

/**
 * @classDescription
 * Object holding drag state info
 */
DnD.Drag = Class.create();
DnD.Drag.prototype = {
	/**
	 * @constructor
	 * @param {DnD.Draggable} source
	 * @param {DnD.Indicator} indicator
	 */
	initialize: function(source, indicator, type) {
		this.source = source;
		this.indicator = indicator;
		this.type = type;
		this.params = {};
	},
	dragged: false,
	dropzone: null,
	getParameters: function() {
		var params = {};
		Object.extend(params, this.params);
	
		return params;
	}
};
