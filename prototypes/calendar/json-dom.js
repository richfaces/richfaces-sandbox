// AJAX-JSF AJAX-like library, for communicate with view Tree on server side.
// In case of XMLHttpRequest don't worked, use :
// JSHttpRequest v1.12. (C) Dmitry Koterov, 2005-01-27. 
// http://forum.dklab.ru/users/DmitryKoterov/
//
// Do not remove this comment if you want to use script!
// ?? ???????? ?????? ???????????, ???? ?? ?????? ???????????? ??????!
//
// Modified by Alexander J. Smirnov to use as JSF AJAX-like components. 

// DOM - like elements for JSRequest. JS serialiser encode
// XML sax events to creation of corresponding objects.  
JSNode =  function() {
};

// Base node  
JSNode.prototype = {
	tag : null,
	attrs : {},
	childs : [],
	value : "",
	_symbols : {
			'&':"&amp;",
			'<':"&lt;",
			'>':"&gt;",
			'"':"&quot;",
			'\'':"&apos;",
			'\u00A0':"&nbsp;"
	}, 
	// Public functions
	getInnerHTML : function(context) {
		var children = [];
		for (var i = 0; i < this.childs.length; i++)
		{
			children.push(this.childs[i].getContent(context)); 
		}
		return children.join('');
	},
	// Escape XML symbols - < > & ' ...
	xmlEscape : function(value) {
		var text = value ? value.toString() : "";
		/*for(var i in this._symbols ) {
			text = text.replace(i,this._symbols[i]);
		
		*/
		return text.escapeHTML();
	}
};

// Element node
E = function(tagname,attributes,childnodes) {
	this.tag = tagname;
	if (attributes) this.attrs = attributes;
	if(childnodes) this.childs = childnodes;
};

E.prototype = new JSNode();
E.prototype.getContent = function(context) {
	var html = "<"+this.tag;
	var inner = this.getInnerHTML(context);
	if (inner=='') this.isEmpty = true; else this.isEmpty=false;
	for(var i in this.attrs) {
		if (!this.attrs.hasOwnProperty(i)) {
			continue ;
		}

		var attrValue = this.attrs[i];
		
		if (typeof attrValue == "function")
			attrValue = attrValue.call(this, context);
		
		if (attrValue) 
			html += " "+(i=='className'?'class':i)+'="'+this.xmlEscape(attrValue)+'"';
	}
	html+= ">"+inner+"</"+this.tag+">";
	return html;
};

// Escaped Text node
ET = function(text) {
	this.value = text;
};


//ET.prototype = new JSNode();
ET.prototype.getContent = function(context) {
  	var value = this.value;
  	if (typeof value=="function") value=value(context);
  	if (value && value.getContent) {
		value = value.getContent(context);
	}
	
	if (value) return value;
  
	return "";
};

// Text node
T = function(text) {
	this.value = text;
};

T.prototype = new JSNode();
T.prototype.getContent = function(context) {
  	var value = this.value;
  	if (typeof value=="function") value=value(context);
  		
	if (value) return this.xmlEscape(value);

	return "";
};

// Comment node
C = function(text) {
	this.value = text;
};

//C.prototype = new JSNode();
C.prototype.getContent = function(context) {
	return "<!--"+this.value+"-->";
};
  
// CDATA Section node.
D = function(text) {
	this.value = text;
};
  
//D.prototype = new JSNode();
D.prototype.getContent = function(context) {
	return "<![CDATA["+this.value+"]]>";
};


