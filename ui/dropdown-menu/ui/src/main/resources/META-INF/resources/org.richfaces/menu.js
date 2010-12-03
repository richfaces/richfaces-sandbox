(function($, rf) {
    rf.ui = rf.ui || {};
  
    var defaultOptions = {
        mode: 'server',
        attachToBody: false,
        positionOffset: [0, 0],
        showDelay: 50,
        hideDelay: 800,
        verticalOffset: 0,
        horisantalOffset: 0,
        showEvent: 'mouseover',
        direction: "AA",
        jointPoint: "AA",
        itemCss: "rf-ddm-itm",
        selectItemCss: "rf-ddm-itm-sel",
        unselectItemCss: "rf-ddm-itm-unsel",
        disabledItemCss: "rf-ddm-itm-dis",
        listCss: "rf-ddm-lst"
    };

    //constructor definition
    rf.ui.Menu = function(componentId, options) {        
        $super.constructor.call(this, componentId);
        this.id = componentId;
        this.groupList = new Array();
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        this.attachToDom(componentId);

        this.element = rf.getDomElement(this.id);
        
        this.selectItemCss = this.options.selectItemCss;
        this.unselectItemCss = this.options.unselectItemCss;
       
        this.displayed = false;
        
        this.options.attachTo = this.id;
        this.options.positionOffset=[this.options.horisantalOffset, this.options.verticalOffset];
        this.popupList = new RichFaces.ui.PopupList(this.id + "_list", this, this.options);
        this.selectedGroup = null;
        rf.Event.bindById(this.id, this.options.showEvent, $.proxy(this.___showHandler, this), this);
        rf.Event.bindById(this.id, "mouseover", $.proxy(this.__overHandler, this), this);
        rf.Event.bindById(this.id, "mouseleave", $.proxy(this.__leaveHandler, this), this);

    };

    rf.BaseComponent.extend(rf.ui.Menu);

    // define super class link
    var $super = rf.ui.Menu.$super;
    $.extend(rf.ui.Menu.prototype, (function() {
        return {
            name: "Menu",            
            initiateGroups: function(groupOptions) {               
                
                for (var i in groupOptions) {
                    var groupId = groupOptions[i].id;                 
                    var positionOffset = [groupOptions[i].horizontalOffset, groupOptions[i].verticalOffset]
                    var onshow = groupOptions[i].onshow;
                    var onhide = groupOptions[i].onhide;
                    
                    var eventType = "mouseover";
                    
                    if (null != groupId) {
                        //var popup = new RichFaces.ui.Popup(groupId + '_list', options);
                        var group= new RichFaces.ui.MenuGroup(groupId,{parentMenuId: this.id, onshow: onshow, onhide: onhide});
                        this.groupList[groupId] = group; 						
                    }
                }
            },
            
            submitForm: function(item) {
                var form = this.__getParentForm(item);
                if (this.options.mode == "server") {
                    //rf.submitForm(form, {selectedMenuItem: item.id});
                }
                if (this.options.mode == "ajax") {
                    //rf.ajax(item.id);
                }
            },

            processItem: function(item) {
                if (item && item.attr('id') && !this.__isDisabled(item) && this.__isGroup(item)) {
                	this.invokeEvent("itemclick", rf.getDomElement(this.id), null);
                    this.hidePopup();                                        
                }
            },
            
            selectItem: function(item) {                
//                if(item.attr('id') && !this.isDisabled(item)){
//                    item.addClass(this.selectItemCss);
//                    if(item.attr('id').search('group') != -1){
//                        this.selectedGroup=item.attr('id');    
//                    } else {
//                        this.selectedGroup = null;
//                    }                    
//                }               
            },
            unselectItem: function(item) {                
//                var nextItem = this.popupList.nextSelectItem();               
//                if (item.attr('id')  && (nextItem.attr('id') != this.selectedGroup) && !this.isWithin(nextItem) ){
//                    item.removeClass(this.selectItemCss);
//                    item.addClass(this.unselectItemCss);
//                }
                
            },      
            
            showPopup: function() {
            	if (!this.__isShown()){
                	this.invokeEvent("show", rf.getDomElement(this.id), null);
                	 this.popupList.show();
                	this.displayed=true;
            	}
            },
            
            hidePopup: function() {
               /* for (var i in this.groupList) {                   
                    this.groupList[i].hide();
                }*/
                if (this.__isShown()){
                	this.invokeEvent("hide", rf.getDomElement(this.id), null);
               		this.popupList.hide();
               		this.displayed=false;
                }              
                
            },
            
            __isGroup: function(item) {
            	return 'object' == typeof this.groupList[item.attr('id')];
            },
            
            __isDisabled: function(item) {
                return item.hasClass(this.options.disabledItemCss);
            },
            
            __isShown: function() {
            	return this.displayed;
            	
            },
            
            ___showHandler: function() {
                this.showTimeoutId = window.setTimeout($.proxy(function(){						
                                                this.showPopup();
					}, this), this.options.showDelay);
            },
            
            __getParentForm: function(item) {
                return item.parents("form")[0];
            },
            

            
            __leaveHandler: function(e) {
            	window.clearTimeout(this.showTimeoutId);
                this.hideTimeoutId = window.setTimeout($.proxy(function(){                                                
                                                this.hidePopup();
					}, this), this.options.hideDelay);
            },
            
            __overHandler: function(e) {
                window.clearTimeout(this.hideTimeoutId);
            },
            
            destroy: function() {
                // clean up code here
				this.detach(this.id);
                
				// call parent's destroy method
                $super.destroy.call(this);
            }
        };
    })());
})(jQuery, RichFaces)