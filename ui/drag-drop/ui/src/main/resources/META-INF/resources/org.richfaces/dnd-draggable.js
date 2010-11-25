(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.Draggable =  function(id, options) {
		this.dragElement = $(document.getElementById(id));
		this.dragElement.draggable({addClasses: false});

		if(options.indicator) {
			var element = document.getElementById(options.indicator);
			this.indicator = rf.$(options.indicator);
			this.dragElement.draggable("option", "helper", function(){return element});
		} else {
			this.dragElement.draggable("option", "helper", 'clone');
		}
		
		this.options = options;
		
		this.dragElement.data('type', this.options.type);
		this.dragElement.data("init", true);
			
		this.dragElement.bind('dragstart', $.proxy(this.dragStart, this));
		this.dragElement.bind('drag', $.proxy(this.drag, this));
		this.dragElement.bind('dragstop', $.proxy(this.dragStop, this));
    };
	
	var defaultOptions = {
	};

	$.extend(rf.ui.Draggable.prototype, ( function () {
    		return {
				dragStart: function(e, ui) {
    				if(ui.helper && this.indicator) {
    					ui.helper.show();
    				}
				}, 
				
				drag: function(e, ui) {
					var helper = ui.helper;
					if(this.indicator) {
						helper.addClass(this.indicator.draggingClass());
					}
				},
				
				dragStop: function(e, ui){
					if(ui.helper && this.indicator) {
						ui.helper.hide();
					}
					
					if(ui.helper[0] != this.dragElement[0]) { 
						//fix to prevent remove custom indicator from DOM tree. see jQuery draggable._clear method for details
						ui.helper[0] = this.dragElement[0];
					}
				}
			}
    	})());
})(jQuery, window.RichFaces);
