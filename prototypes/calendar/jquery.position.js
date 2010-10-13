/**
 * @fileOverview jQuery setPosition Plugin to place elements on the page
 * @author Pavel Yaschenko, 05.2010
 * @version 0.5
 */

// draft examples of usage
// jQuery('#tooltip').setPosition('#aaa',{from:'LB', to:'AA'});
// jQuery('#bbb').bind("click",function(e){jQuery('#tooltip').setPosition(e);});
// TODO: clear code
// TODO: optimization

// jQuery(target).setPosition(source,[params])
// source:
//			jQuery selector
//			object {id:}
//			object {left:,top:,width:,height} // all properties are optimal
//			jQuery object
//			dom element
//			event
//
//	params:
//			type: string // position type
//			collision: string // not implemented
//			offset: array [x,y] // implemented only for noPositionType
//			from: string // place target relative of source
//			to: string // direction for target

	/**
	  * @name jQuery
	  * @namespace jQuery 
	  * */

(function($) {
	/**
     * Place DOM element relative to another element or using position parameters. Elements with style.display='none' also supported.
     * 
     * @example jQuery('#tooltip').setPosition('#myDiv',{from:'LB', to:'AA'});
     * @example jQuery('#myClickDiv').bind("click",function(e){jQuery('#tooltip').setPosition(e);});
     *
     * @function
     * @name jQuery#setPosition
     * 
     * @param {object} source - object that provides information about new position. <p>
     * accepts:
     * <ul>
     * 		<li>jQuery selector or object</li>
     * 		<li>object with id: <code>{id:'myDiv'}</code></li>
     * 		<li>object with region settings: <code>{left:0, top:0, width:100, height:100}</code></li>
     * 		<li>DOM Element</li>
     * 		<li>Event object</li>
     * </ul>
     * </p>
     * @param {object} params - position parameters:
     * <dl><dt>
     * @param {string} [params.type] - position type that defines positioning and auto positioning rules ["TOOLTIP","DROPDOWN"]</dt><dt>
     * @param {string} [params.collision] - not implemented yet</dt><dt>
     * @param {array} [params.offset] - provides array(2) with x and y for manually position definition<br/>
     * affects only if "type", "from" and "to" not defined</dt><dt>
     * @param {string} [params.from] - place target relative of source // draft definition</dt><dt>
     * @param {string} [params.to] - direction for target // draft definition</dt>
     * </blockquote>
     *
     * @return {jQuery} jQuery wrapped DOM elements
     * */
	$.fn.setPosition = function(source, params) {
		var stype = typeof source;
		if (stype == "object" || stype == "string") {
			var rect = {};
			if (stype == "string" || source.nodeType || source instanceof jQuery) {
					rect = getElementRect(source);
			} else if (source.type) {
				rect = getPointerRect(source);
			} else if (source.id) {
				rect = getElementRect(document.getElementById(source.id));
			} else {
				rect = source;
			}
			
			var params = params || {};
			var def = params.type || params.from || params.to ? $.PositionTypes[params.type || defaultType] : {noPositionType:true};
			
			var options =  $.extend({}, defaults, def, params);
			return this.each(function() {
					element = $(this);
					//alert(rect.left+" "+rect.top+" "+rect.width+" "+rect.height);
					position(rect, element, options);
				});
		}
		return this;
	};
	
	var defaultType = "TOOLTIP";
	var defaults = {
		collision: "",
		offset: [0,0]
	};
	var re = /^(left|right)-(top|buttom|auto)$/i;
	
	// TODO: make it private
	$.PositionTypes = {
		// horisontal constants: L-left, R-right, C-center, A-auto
		// vertical constants:   T-top, B-bottom, M-middle, A-auto
		// for auto: list of joinPoint-Direction pairs
		TOOLTIP: {from:"AA", to:"AA", auto:["RTRT", "RBRT", "LTRT", "RTLT", "LTLT", "LBLT", "RTRB", "RBRB", "LBRB", "RBLB"]},
		DROPDOWN:{from:"AA", to:"AA", auto:["LBRB", "LTRT", "RBLB", "RTLT"]}
	};
	
	/** 
	  * Add or replace position type rules for auto positioning.
	  * Does not fully determinated with parameters yet, only draft version.
	  * 
	  * @function
	  * @name jQuery.addPositionType
	  * @param {string} type - name of position rules
	  * @param {object} option - options of position rules
	  * */
	$.addPositionType = function (type, options) {
		// TODO: change [options] to [from, to, auto]
		/*var obj = {};
		if (match=from.match(re))!=null ) {
			obj.from = [ match[1]=='right' ? 'R' : 'L', match[2]=='bottom' ? 'B' : 'T'];
		}
		if (match=to.match(re))!=null ) {
			obj.to = [ match[1]=='right' ? 'R' : match[1]=='left' ? 'L' : 'A', match[2]=='bottom' ? 'B' : match[2]=='top' ? 'T' : 'A'];
		}*/
		$.PositionTypes[type] = options;
	}
    
    function getPointerRect (event) {
		var e = $.event.fix(event);
		return {width: 0, height: 0, left: e.pageX, top: e.pageY};
	};

	function getElementRect (element) {
		var jqe = $(element);
		var offset = jqe.offset();
		var rect = {width: jqe.width(), height: jqe.height(), left: Math.floor(offset.left), top: Math.floor(offset.top)};
		/*if (jge.length>1) {
			var width, height, offset;
			var e;
			for (var i=1;i<jqe.length;i++) {
				e = jqe.eq(i);
				offset = e.offset();
				if (offset.left < rect.left) rect.left = offset.left;
				if (offset.top < rect.top) rect.top = offset.top;
				width = e.width(); height = e.height();
				if (rect.left + width > rect)
			}
		}*/
		
		return rect;
		/*
			var jqe = $(element);
			var offset = jqe.offset();
			var width = jqe.width();
			var height = jqe.height();
			if (width == 0 && height==0) {
				//TODO: create getComputedStyle function for this
				var e = jqe.get(0);
				if (e.currentStyle) {
					width = parseInt(e.currentStyle['width'],10) || 0;
					height = parseInt(e.currentStyle['height'],10) || 0;
				}
			}
			return {width: width, height: height, left: Math.floor(offset.left), top: Math.floor(offset.top)};
		 */
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
					top:    (elementRect.top>windowRect.top ? elementRect.top : windowRect.top)};
		rect.right = elementRect.right<windowRect.right ? (elementRect.right==elementRect.left ? rect.left : elementRect.right) : windowRect.right;
		rect.bottom = elementRect.bottom<windowRect.bottom ? (elementRect.bottom==elementRect.top ? rect.top : elementRect.bottom) : windowRect.bottom;

		return (rect.right-rect.left) * (rect.bottom-rect.top);
	};
	
	//function fromLeft() {
	/*
	 * params: {
	 * 	left,top,width,height, //baseRect
	 * 	ox,oy, //rectoffset
	 * 	w,h // elementDim
	 * }
	 */
	/*	return this.left;
	}
	
	function fromRight(params) {
			return this.left + this.width - this.w;
	}
	
	function (params) {
		var rect = {left:fromLeft.call(params), right:fromRight.call(params), top:}
	}*/
	
	function getPositionRect(baseRect, rectOffset, elementDim, pos) {
		var rect = {};
		// TODO: add support for center and middle // may be middle rename to center too
		// TODO: add rectOffset support && tests 
		
		var v = pos.charAt(0);
		if (v=='L') {
			rect.left = baseRect.left;
		} else if (v=='R') {
			rect.left = baseRect.left + baseRect.width;
		}
		
		v = pos.charAt(1);
		if (v=='T') {
			rect.top = baseRect.top;
		} else if (v=='B') {
			rect.top = baseRect.top + baseRect.height;
		}
		
		v = pos.charAt(2);
		if (v=='L') {
			rect.right = rect.left;
			rect.left -= elementDim.width;
		} else if (v=='R') {
			rect.right = rect.left + elementDim.width;
		}		
		
		v = pos.charAt(3);
		if (v=='T') {
			rect.bottom = rect.top;
			rect.top -= elementDim.height;
		} else if (v=='B') {
			rect.bottom = rect.top + elementDim.height;
		}
		
		return rect;
	}
	
	function __mergePos(s1,s2) {
		var result = "";
		var ch;
		while (result.length < s1.length) {
			ch = s1.charAt(result.length);
			result += ch == 'A' ? s2.charAt(result.length) : ch; 
		}
		return result;
	}
	
	function calculatePosition (baseRect, rectOffset, windowRect, elementDim, options) {

		var theBest = {square:0};
		var rect;
		var s;
		var ox, oy;
		var p = options.from+options.to;
		
		if (p.indexOf('A')<0) {
			return getPositionRect(baseRect, rectOffset, elementDim, p);
		} else {
			var flag = p=="AAAA";
			var pos;
			for (var i = 0; i<options.auto.length; i++) {
				
				// TODO: draft functional
				pos = flag ? options.auto[i] : __mergePos(p, options.auto[i]);
				rect = getPositionRect(baseRect, rectOffset, elementDim, pos);
				ox = rect.left; oy = rect.top;
				s = checkCollision(rect, windowRect);
				if (s!=0) {
					if (ox>=0 && oy>=0 && theBest.square<s) theBest = {x:ox, y:oy, square:s};
				} else break;
			}
			if (s!=0 && (ox<0 || oy<0 || theBest.square>s)) {
				ox=theBest.x; oy=theBest.y
			}
		}
		
		return {left:ox, top:oy};
	}
	
	function position (rect, element, options) {
		var width = element.width();
		var height = element.height();
		
		rect.width = rect.width || 0;
		rect.height = rect.height || 0;
		
		var left = parseInt(element.css('left'),10);
		if (isNaN(left) || left==0) {
			left = 0;
			element.css('left', '0px');
		}
		if (isNaN(rect.left)) rect.left = left;
		
		var top = parseInt(element.css('top'),10);
		if (isNaN(top) || top==0) {
			top = 0;
			element.css('top', '0px');
		}
		if (isNaN(rect.top)) rect.top = top;
		
		var pos = {};
		if (options.noPositionType) {
			pos.left = rect.left + rect.width + options.offset[0];
			pos.top = rect.top + options.offset[1];
		} else {
			var jqw = $(window);
			var winRect = {left:jqw.scrollLeft(), top:jqw.scrollTop()};
			winRect.right = winRect.left + jqw.width();
			winRect.bottom = winRect.top + jqw.height();
		
			pos = calculatePosition(rect, options.offset, winRect, {width:width, height:height}, options);		
		}
		
		// jQuery does not support to get offset for hidden elements
		var hideElement=false;
		var eVisibility;
		var e;
		if (element.css("display")=="none") {
			hideElement=true;
			e = element.get(0);
			eVisibility = e.style.visibility;
			e.style.visibility = 'hidden';
			e.style.display = 'block';
		}
		
		var elementOffset = element.offset();
		
		if (hideElement) {
			e.style.visibility = eVisibility;
			e.style.display = 'none';
		}
		
		pos.left += left - Math.floor(elementOffset.left);
		pos.top += top - Math.floor(elementOffset.top);	

		if (left!=pos.left) {
			element.css('left', (pos.left + 'px'));
		}
		if (top!=pos.top) {
			element.css('top', (pos.top + 'px'));
		}
	};

})(jQuery);

