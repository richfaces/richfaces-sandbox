if (!window.Richfaces) {
	window.Richfaces = {};
}

Richfaces.jQuery = {};

(function(_r$, jQuery) {
    var PX_REGEX = /^\s*[^\s]*px\s*$/;
    
    var parseToPx = function(value) {
    	if (value) {
			if (PX_REGEX.test(value)) {
				try {
					return parseInt(value.replace(PX_REGEX, ""), 10);
				} catch (e) { }
			}
		}

    	return NaN;
    };
    
	_r$.position = function(rect, element) {
		var jqe = jQuery(element);
		var width = jqe.width();
		var height = jqe.height();
		
		var left = parseToPx(jqe.css('left'));
		if (isNaN(left)) {
			left = 0;
			jqe.css('left', '0px');
		}
		
		var top = parseToPx(jqe.css('top'));
		if (isNaN(top)) {
			top = 0;
			jqe.css('top', '0px');
		}
		
		var elementOffset = jqe.offset();
		
		var jqw = jQuery(window);

		var winWidth = jqw.width();
		var winLeft = jqw.scrollLeft();
		
		var winHeight = jqw.height();
		var winTop = jqw.scrollTop();

		var newLeft;
		if (rect.left + width > winLeft + winWidth && rect.left + rect.width - width >= winLeft) {
			newLeft = rect.left + rect.width - width;
		} else {
			newLeft = rect.left;
		}
		
		var newTop;
		if (rect.top + rect.height + height > winTop + winHeight && rect.top - height >= winTop) {
			newTop = rect.top - height;
		} else {
			newTop = rect.top + rect.height;
		}
		
		left += newLeft - elementOffset.left;
		top += newTop - elementOffset.top;

		jqe.css('left', (left + 'px')).css('top', (top + 'px'));
	};

	_r$.getPointerRectangle = function(event) {
		var e = jQuery.event.fix(event);
		
		return {width: 0, height: 0, left: e.pageX, top: e.pageY};
	};

	_r$.getElementRectangle = function(element) {
		var jqe = jQuery(element);
		var offset = jqe.offset();
		
		return {width: jqe.width(), height: jqe.height(), left: offset.left, top: offset.top};
	};
	
}(Richfaces.jQuery, jQuery));

