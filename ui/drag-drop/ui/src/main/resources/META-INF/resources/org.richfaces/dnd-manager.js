(function ($, rf) {
	
	rf.ui = rf.ui || {};
      
	rf.ui.DnDManager = rf.ui.DnDManager || {};  

	$.extend(rf.ui.DnDManager, ( function () {
		var draggables = {};
		var droppables = {};

		return {
    			draggable: function(id, options) {
					var draggable = draggables[id];
					if(!draggable) {
						draggables[id] = new rf.ui.Draggable(id, options);
					}
				}, 
				
				droppable: function(id, options) {
					var droppable = droppables[id];
					if(!droppable) {
						droppables[id] = new rf.ui.Droppable(id, options);
					}
				}
			}
    	})());
})(jQuery, window.RichFaces);