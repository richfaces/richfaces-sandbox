(function($) {
	$.fn.position = function(source, params) {
		var stype = typeof source;
		if (stype == "object" || stype == "string") {
			var rect = {};
			if (source.type) {
				rect = getPointerRect(source);
			} else if (stype == "string" || source.nodeType || source instanceof jQuery) {
				stype = "element";
				rect = getElementRect(source);
			} else {
				rect = source;
			}
			
			var params = params || {};
			var options =  $.extend({}, defaults, $.Richfaces.position.types[params.type || defaults.type] );
			return this.each(function() {
					element = $(this);
					//alert(rect.left+" "+rect.top+" "+rect.width+" "+rect.height);
					position(rect, element, options);
				});
		}
	};
	
	var defaults = {
		type: "TOOLTIP",
		collision: "",
		offset: [0,0]
	};
	
	if (!$.Richfaces) {
		$.Richfaces = {};
	}
	
	$.Richfaces.position = {};
	
	$.Richfaces.position.types = {
		// horisontal constants: L-left, R-right, C-center, A-auto
		// vertical constants:   T-top, B-bottom, M-middle, A-auto
		// for auto: list of joinPoint-Direction pairs
		TOOLTIP:{from:"LB", to:"AA", auto:["LB-LB", "LT-LT", "RB-RB", "RT-RT"]},
		DROPDOWN:{from:"RB", to:"AA",auto:["RB-LB", "RT-LT", "LB-RB", "LT-RT"]}
	};
	
	function cropPx(value) {
    	if (typeof value == "string") {
    		if (!isNaN(value)) {
    			return parseInt(value);
    		}
		}
    	return NaN;
    };
    
    function getPointerRect (event) {
		var e = jQuery.event.fix(event);
		return {width: 0, height: 0, left: e.pageX, top: e.pageY};
	};

	function getElementRect (element) {
		var jqe = jQuery(element);
		var offset = jqe.offset();
		return {width: jqe.width(), height: jqe.height(), left: offset.left, top: offset.top};
	};
	
	function checkCollision (elementRect, windowRect) {
		// return 0 if elementRect in windowRect without collision
		if (elementRect.left >= windowRect.left &&
			elementRect.top >= windowRect.top &&
			elementRect.right <= windowRect.right &&  
			elementRect.bottom <= windowRect.bottom)
			return 0;
		// return collision squire
		var rect = {left:   (elementRect.left>windowRect.left ? elementRect.left : windowRect.left),
					top:    (elementRect.top>windowRect.top ? elementRect.top : windowRect.top),
					right:  (elementRect.right<windowRect.right ? elementRect.right : windowRect.right),
					bottom: (elementRect.bottom<windowRect.bottom ? elementRect.bottom : windowRect.bottom)};
		return (rect.right-rect.left) * (rect.bottom-rect.top);
	};
	
	function calculateAutoPosition (baseRect, rectOffset, windowRect, elementDim, options) {
		var ox, oy;
		var theBest = {square:0};
		var rect;
		var s;
		for (var i = 0; i<options.auto.length; i++) {
			rect = getAutoPositionRect(baseRect, rectOffset, elementDim, options.auto[i]);
			ox = rect.left; oy = rect.top;
			s = Richfaces.Calendar.checkCollision(rect, windowRect);
			if (s!=0) {
				if (ox>=0 && oy>=0 && theBest.square<s) theBest = {x:ox, y:oy, square:s};
			} else break;
		}
		if (s!=0 && (ox<0 || oy<0 || theBest.square>s)) {
			ox=theBest.x; oy=theBest.y
		}
		
		/*
		// jointPoint: bottom-right, direction: bottom-left
		var basex = baseRect.left - rectOffset.left;
		var basey = baseRect.top + rectOffset.top;
		var rect = {right: basex + baseRect.width, top: basey + baseRect.height};
		rect.left = rect.right - elementDim.width;
		rect.bottom = rect.top + elementDim.height;
		ox = rect.left; oy = rect.top;
		var s = Richfaces.Calendar.checkCollision(rect, windowRect);
		if (s!=0)
		{
			if (ox>=0 && oy>=0 && theBest.square<s) theBest = {x:ox, y:oy, square:s};
			// jointPoint: top-right, direction: top-left
			basex = baseRect.left - rectOffset.left;
			basey = baseRect.top - rectOffset.top;
			rect = {right: basex + baseRect.width, bottom: basey};
			rect.left = rect.right - elementDim.width;
			rect.top = rect.bottom - elementDim.height;
			ox = rect.left; oy = rect.top;
			s = Richfaces.Calendar.checkCollision(rect, windowRect);
			if (s!=0)
			{
				if (ox>=0 && oy>=0 && theBest.square<s) theBest = {x:ox, y:oy, square:s};
				// jointPoint: bottom-left, direction: bottom-right
				basex = baseRect.left + rectOffset.left;
				basey = baseRect.top + rectOffset.top;
				rect = {left: basex, top: basey + baseRect.height};
				rect.right = rect.left + elementDim.width;
				rect.bottom = rect.top + elementDim.height;
				ox = rect.left; oy = rect.top;
				s = Richfaces.Calendar.checkCollision(rect, windowRect);
				if (s!=0)
				{
					if (ox>=0 && oy>=0 && theBest.square<s) theBest = {x:ox, y:oy, square:s};
					// jointPoint: top-left, direction: top-right
					basex = baseRect.left + rectOffset.left;
					basey = baseRect.top + rectOffset.top;
					rect = {left: basex, bottom: basey};
					rect.right = rect.left + elementDim.width;
					rect.top = rect.bottom - elementDim.height;
					ox = rect.left; oy = rect.top;
					s = Richfaces.Calendar.checkCollision(rect, windowRect);
					if (s!=0)
					{
						// the best way selection
						if (ox<0 || oy<0 || theBest.square>s) {ox=theBest.x; oy=theBest.y}
					}
				}
			}
		}*/
		
		return {left:ox, top:oy};
	}
	
	function position (rect, element, options) {
		var width = element.width();
		var height = element.height();
		
		var left = cropPx(element.css('left'));
		if (isNaN(left)) {
			left = 0;
			element.css('left', '0px');
		}
		
		var top = cropPx(element.css('top'));
		if (isNaN(top)) {
			top = 0;
			element.css('top', '0px');
		}
		
		var elementOffset = element.offset();
		
		var jqw = jQuery(window);
		var winRect = {left:jqw.scrollLeft(), top:jqw.scrollTop(), width:jqw.width(), height:jqw.height()}

		/*var winWidth = jqw.width();
		var winLeft = jqw.scrollLeft();
		
		var winHeight = jqw.height();
		var winTop = jqw.scrollTop();*/
		
		calculateAutoPosition(rect, options.offset, winRect, {width:width, height:height});

		/*var newLeft;
		if (rect.left + width > winRect.left + winRect.width && rect.left + rect.width - width >= winRect.left) {
			newLeft = rect.left + rect.width - width;
		} else {
			newLeft = rect.left;
		}
		
		var newTop;
		if (rect.top + rect.height + height > winRect.top + winRect.height && rect.top - height >= winRect.top) {
			newTop = rect.top - height;
		} else {
			newTop = rect.top + rect.height;
		}*/
		
		left += newLeft - elementOffset.left;
		top += newTop - elementOffset.top;

		element.css('left', (left + 'px')).css('top', (top + 'px'));
	};

})(jQuery);

