(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.Droppable =  function(id, options) {
		this.options = options;
		this.dropElement = $(document.getElementById(id));
		
		this.dropElement.droppable({addClasses: false});
		this.dropElement.data("init", true);
		this.dropElement.bind('drop', $.proxy(this.drop, this));
		this.dropElement.bind('dropover', $.proxy(this.dropover, this));
		this.dropElement.bind('dropout', $.proxy(this.dropout, this));
	};

	$.extend(rf.ui.Droppable.prototype, ( function () {
    		return {
				drop: function(e, ui) {
					if(this.accept(ui.draggable)) {
						this.__callAjax(e, ui);
					}
						
					var dragIndicatorObj = rf.$(ui.helper.attr("id"));
					if(dragIndicatorObj) {
						ui.helper.removeClass(dragIndicatorObj.acceptClass());
						ui.helper.removeClass(dragIndicatorObj.rejectClass());
					}
				}, 
				
				dropover: function(event, ui) {
					var draggable = ui.draggable;
					var dragIndicatorObj = rf.$(ui.helper.attr("id"));
					if(dragIndicatorObj) {
						if(this.accept(draggable)) {
							ui.helper.removeClass(dragIndicatorObj.rejectClass());								
							ui.helper.addClass(dragIndicatorObj.acceptClass());	
						} else {
							ui.helper.removeClass(dragIndicatorObj.acceptClass());
							ui.helper.addClass(dragIndicatorObj.rejectClass());								
						}
					}
				}, 
				
				dropout: function(event, ui) {
					var draggable = ui.draggable;
					var dragIndicatorObj = rf.$(ui.helper.attr("id"));
					if(dragIndicatorObj) {
						ui.helper.removeClass(dragIndicatorObj.acceptClass());
						ui.helper.removeClass(dragIndicatorObj.rejectClass());
					}
				}, 
								
				accept: function(draggable) {
					var accept;
					var acceptType = draggable.data("type");
					if(acceptType) {
						$.each(this.options.acceptedTypes, function() {
							accept = (acceptType == this); 	return !(accept);
						});
					}	
					return accept;
				}, 
				
				__callAjax: function(e, ui){
					var options = {};
					var originalEvent =  this.options['event'];
					if(ui.draggable) {
						options['dragSource'] = ui.draggable.attr("id");
						options['javax.faces.behavior.event'] = originalEvent.type; 
					}
					rf.ajax(this.dropElement[0], originalEvent, {parameters: options});
				}
			}
    	})());

})(jQuery, window.RichFaces);