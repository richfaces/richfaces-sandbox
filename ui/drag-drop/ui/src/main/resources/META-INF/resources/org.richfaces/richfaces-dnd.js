(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.indicator =  function(id, options) {
		this.indicator = $(document.getElementById(id));
	};
	
	var defaultOptions = {
	};

	$.extend(rf.ui.indicator.prototype, ( function () {
    		return {
				show : function() {
					this.indicator.show();
				}, 
				
				hide: function() {
					this.indicator.hide();
				},
				
				getDomElement: function() {
					return this.indicator[0];
				}
			}
    	})());

})(jQuery, window.RichFaces);



(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.draggable =  function(id, options) {
		this.dragElement = $(document.getElementById(id));
		this.indicator = new rf.ui.indicator(options.indicator, {});
	
		this.options = options;
		
		this.dragElement.draggable({addClasses: false});
		this.dragElement.draggable("option", "helper", $.proxy(this.indicator.getDomElement, this.indicator));
		this.dragElement.data('acceptType', this.options.acceptType);
		this.dragElement.data('indicator', $(document.getElementById(options.indicator)));
		
		this.dragElement.bind('dragstart', $.proxy(this.dragStart, this));
		this.dragElement.bind('drag', $.proxy(this.drag, this));
		this.dragElement.bind('dragstop', $.proxy(this.dragStop, this));
    };

	
	var defaultOptions = {
	};

	$.extend(rf.ui.draggable.prototype, ( function () {
    		return {
				
				dragStart: function(e, ui) {
					ui.helper.show();
				}, 
				
				drag: function(e, ui) {
					ui.helper.removeClass("ui-draggable-dragging")
					ui.helper.addClass("rf-ind-dragging");	
				},
				
				dragStop: function(e, ui){
					ui.helper.hide();
				}
			}
    	})());

})(jQuery, window.RichFaces);



(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.droppable =  function(id, options) {
		this.options = options;
		this.dropElement = $(document.getElementById(id));
		this.dropElement.droppable({addClasses: false});
		this.dropElement.droppable("option", "hoverClass", 'rf-drop-hover');
		this.dropElement.droppable("option", "activeClass", 'rf-drop-highlight' );
		
		this.dropElement.bind('drop', $.proxy(this.drop, this));
		this.dropElement.bind('dropover', $.proxy(this.dropover, this));
		this.dropElement.bind('dropout', $.proxy(this.dropout, this));
    };

	$.extend(rf.ui.droppable.prototype, ( function () {
    		return {
				drop: function(e, ui) {
					var draggable = ui.draggable;
					var indicator = draggable.data("indicator");
					indicator.removeClass("rf-accept");
					indicator.removeClass("rf-reject");								
					if(this.accept(draggable)) {
						draggable.detach();
						draggable.appendTo(this.dropElement);
					}
				}, 
				
				dropover: function(event, ui) {
					var draggable = ui.draggable;
					var indicator = draggable.data("indicator");
					if(this.accept(draggable)) {
						indicator.removeClass("rf-reject");								
						indicator.addClass("rf-accept");	
					} else {
						indicator.removeClass("rf-accept");
						indicator.addClass("rf-reject");	
					}	
				}, 
				
				dropout: function(event, ui) {
					var draggable = ui.draggable;
					var indicator = draggable.data("indicator");
					indicator.removeClass("rf-accept");
					indicator.removeClass("rf-reject");								
				}, 
								
				accept: function(draggable) {
					var accept;
					var acceptType = draggable.data("acceptType");

					$.each(this.options.acceptTypes, function() {
						accept = (acceptType == this);
						return !(accept);
					});

					return accept;
				}
			}
    	})());

})(jQuery, window.RichFaces);