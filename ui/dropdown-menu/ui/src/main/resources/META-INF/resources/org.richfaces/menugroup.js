(function($, rf) {
	rf.ui = rf.ui || {};
	var defaultOptions = {
		showEvent: 'mouseover',
        attachToBody: false,
        positionOffset: [0, 0],
        selectItemCss: "rf-ddm-itm-sel",
        listCss: "rf-ddm-lst"		
	}
	 //constructor definition
    rf.ui.MenuGroup = function(componentId, options) {
    	this.id = componentId;
    	$super.constructor.call(this, componentId);
    	this.attachToDom(componentId);
    	this.options = {};
        $.extend(this.options, defaultOptions, options || {});
    	this.parentMenu = rf.$(this.options.parentMenuId);
    	this.popup = new RichFaces.ui.Popup(this.id + '_list', 
    		{attachTo: this.id, 
    		positionOffset: this.options.positionOffset, 
    		attachToBody: this.options.attachToBody
    		}
    	);    	
    	rf.Event.bindById(this.id, "mouseenter", $.proxy(this.__enterHandler,this), this);
        rf.Event.bindById(this.id, "mouseleave",$.proxy(this.__leaveHandler,this), this);
        this.shown = false;
    };
     
     rf.BaseComponent.extend(rf.ui.MenuGroup);
     
     // define super class link
    var $super = rf.ui.MenuGroup.$super;
	
    $.extend(rf.ui.MenuGroup.prototype, (function() {
    	return {
            name: "MenuGroup",
            show: function() {
            	var id=this.id;
            	if (this.parentMenu.groupList[id] && !this.shown) {                                
                      this.parentMenu.invokeEvent("groupshow", rf.getDomElement(this.parentMenu.id), null);
                      this.invokeEvent("show", rf.getDomElement(id), null);
                      this.popup.show();
                      this.shown=true;
                }
            },
            hide: function() {
            	var id=this.id;
            	var menu = this.parentMenu;
            	if (menu.groupList[this.id] && this.shown) {
                                menu.invokeEvent("grouphide", rf.getDomElement(menu.id), null);
                                this.invokeEvent("hide", rf.getDomElement(id), null);
                                this.popup.hide();
                                this.shown = false;
                }
            },

            __enterHandler: function(){            	
            	this.show();    
            },
            __leaveHandler: function(){            
            	 this.hide();
            },
            destroy: function() {
                // clean up code here
				this.detach(this.id);
                // call parent's destroy method
                $super.destroy.call(this);
            }
    	}
    	
            })());
})(jQuery, RichFaces)