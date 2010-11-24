(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.DragIndicator =  function(id, options) {
    	$super.constructor.call(this, id);
    	this.attachToDom(id);
		
    	this.indicator = $(document.getElementById(id));
		this.options = options; 
	};
	
	var defaultOptions = {
	};
	
    rf.BaseComponent.extend(rf.ui.DragIndicator);
	var $super = rf.ui.DragIndicator.$super;

	$.extend(rf.ui.DragIndicator.prototype, ( function () {
    		return {
				show : function() {
					this.indicator.show();
				}, 
				
				hide: function() {
					this.indicator.hide();
				},
				
				acceptClass: function() {
					return this.options.acceptClass;
				},
				
				rejectClass: function() {
					return this.options.rejectClass;
				},
				
				draggingClass: function() {
					return this.options.draggingClass;
				},
				
				getElement: function() {
					return this.indicator;
				}
			}
    	})());

})(jQuery, window.RichFaces);

