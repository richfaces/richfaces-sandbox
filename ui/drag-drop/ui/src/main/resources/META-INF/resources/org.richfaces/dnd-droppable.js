(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.Droppable =  function(id, options) {
		this.options = options;
		this.dropElement = $(document.getElementById(id));

		this.dropElement.droppable({addClasses: false});
		this.dropElement.droppable("option", "hoverClass", 'rf-drop-hover');
		this.dropElement.droppable("option", "activeClass", 'rf-drop-highlight');
		this.dropElement.data("init", true);
		this.dropElement.bind('drop', $.proxy(this.drop, this));
		this.dropElement.bind('dropover', $.proxy(this.dropover, this));
		this.dropElement.bind('dropout', $.proxy(this.dropout, this));
	};

	$.extend(rf.ui.Droppable.prototype, ( function () {
    		return {
				drop: function(e, ui) {
					var dragElement = ui.draggable;
					var helper = ui.helper;
					var indicator = rf.$(helper.attr("id"));
					if(indicator) {
						helper.removeClass(indicator.acceptClass());
						helper.removeClass(indicator.rejectClass());
					}
				}, 
				
				dropover: function(event, ui) {
					var draggable = ui.draggable;
					var helper = ui.helper;
					var indicator = rf.$(helper.attr("id"));
					if(indicator) {
						if(this.accept(draggable)) {
							helper.removeClass(indicator.rejectClass());								
							helper.addClass(indicator.acceptClass());	
						} else {
							helper.removeClass(indicator.acceptClass());
							helper.addClass(indicator.rejectClass());								
						}
					}
				}, 
				
				dropout: function(event, ui) {
					var draggable = ui.draggable;
					var helper = ui.helper;
					var indicator = rf.$(helper.attr("id"));
					if(indicator) {
						helper.removeClass(indicator.acceptClass());
						helper.removeClass(indicator.rejectClass());
					}
				}, 
								
				accept: function(draggable) {
					var accept;
					var acceptType = draggable.data("type");
					if(acceptType) {
						$.each(this.options.acceptType, function() {
							accept = (acceptType == this); 	return !(accept);
						});
					}	

					return accept;
				}
			}
    	})());

})(jQuery, window.RichFaces);