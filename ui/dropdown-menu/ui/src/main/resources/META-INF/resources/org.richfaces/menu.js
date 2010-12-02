(function($, rf) {
    rf.ui = rf.ui || {};
    var defaultPopupListOptions = {
        showEvent: 'mouseover',
        attachToBody: false,
        positionOffset: [0, 0],
        selectItemCss: "rf-ddm-itm-sel",
        listCss: "rf-ddm-lst"
    };
    var defaultOptions = {
        mode: 'server',
        attachToBody: false,
        positionOffset: [0, 0],
        showDelay: 50,
        hideDelay: 800,
        verticalOffset: 10,
        horisantalOffset: 10,
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
        this.id = componentId;
        $super.constructor.call(this, componentId);
        this.id = componentId;
        this.groupList = new Array();
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        this.attachToDom(componentId);

        //bindEventHandlers.call(this);
        this.element = rf.getDomElement(this.id);
        
        this.selectItemCss = this.options.selectItemCss;
        this.unselectItemCss = this.options.unselectItemCss;
        
        //menu items list
        //this.target = 'dd_menu_id_label_container';
        //popup for item menu list
        //var base = rf.getDomElement(this.id);
        
        this.displayed = false;
        
        //this.popup = $(rf.getDomElement(this.id + "_list"));
        //this.initiatePopup(this.popup, base, this.options.showEvent);
        //this.initiateMenu(this.popup, base, this.options.showEvent);
        this.options.attachTo = this.id;
        this.popupList = new RichFaces.ui.PopupList(this.id + "_list", this, this.options);
        this.selectedGroup = null;
        rf.Event.bindById(this.id, this.options.showEvent, $.proxy(this.___showHandler, this), this);
        //rf.Event.bindById(this.id, 'mouseenter', $.proxy(this.__enterHandler, this), this);
        rf.Event.bindById(this.id, "mouseleave", $.proxy(this.__leaveHandler, this), this);
        //rf.Event.bindById(this.id, "mouseover", $.proxy(this.__overHandler, this), this);        
        //rf.Event.bindById(this.id, "blur", $.proxy(this.__blurHandler, this), this);
       // rf.Event.bindById(this.id, "focus", $.proxy(this.__focusHandler, this), this);
        //this.popupList.show();
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
                    var horizontalOffset = groupOptions[i].horizontalOffset;
                    var verticalOffset = groupOptions[i].verticalOffset;
                    
                    var eventType = "mouseover";
                    
                    if (null != groupId) {
                        var options = defaultPopupListOptions;
                        options.attachTo = groupId;
                        var base = rf.getDomElement(groupId);
                        //var popup=$("#"+groupId+" .rf-ddm-sublst:first");
                        var menu = this;
                        var popup = new RichFaces.ui.Popup(groupId + '_list', options);
                        this.groupList[groupId] = popup;
 
                        rf.Event.bindById(groupId, "mouseenter",
                        function(event) {
                            var id = event.target.id;
                            if (menu.groupList[id]) {                                
                                menu.invokeEvent("groupshow", rf.getDomElement(menu.id), null);
                                //menu.invokeEvent("show", rf.getDomElement(id), null);
                                menu.groupList[id].popup.show();                               
                            }
                        },
                        this);
                        rf.Event.bindById(groupId, "mouseleave",
                        function(event) {
                            var id = event.target.id;
                            if (menu.groupList[id]) {
                                menu.invokeEvent("grouphide", rf.getDomElement(menu.id), null);
                                //menu.invokeEvent("hide", rf.getDomElement(id), null);
                                menu.groupList[id].popup.hide();                                
                            }
                        });
                    }
                }
            },
            
            submitForm: function(item) {
                var form = this.__getParentForm(item);
                if (this.options.mode == "server") {
                    console.info('server submit ' + item.attr('id'));
                    //rf.submitForm(form, {selectedMenuItem: item.id});
                }
                if (this.options.mode == "ajax") {
                    console.info('ajax submit');
                    //rf.ajax(item.id);
                }
            },

            processItem: function(item) {
                if (item &&  item.atrr('id') && !this.isDisabled(item) && this.isGroup(item)) {
                	this.invokeEvent("itemclick", rf.getDomElement(this.id), null);
                    this.hidePopup();
                    //this.submitForm(item);                    
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
            isGroup: function(item) {
            	return 'object' == typeof this.groupList[item.atrr('id')];
            },
            isDisabled: function(item) {
                return item.hasClass(this.options.disabledItemCss);
            },
            isWithin: function(item){
              if (this.selectedGroup && item.parents().is('#'+this.selectedGroup)){
                return true;
              } else {
                return false;
              }
            },
            
            showPopup: function() {
            	if (!this.___isShown()){
                	this.invokeEvent("show", rf.getDomElement(this.id), null);
                	 this.popupList.show();
                	this.displayed=true;
            	}
            },
            
            hidePopup: function() {
                for (var i in this.groupList) {                   
                    this.groupList[i].popup.hide();
                }
                if (this.___isShown()){
                	this.invokeEvent("hide", rf.getDomElement(this.id), null);
               		this.popupList.hide();
               		this.displayed=false;
                }              
                this.popupList.hide();
            },
            ___isShown: function() {
            	return this.displayed;
            	
            },
            ___showHandler: function() {
                this.showTimeoutId = window.setTimeout($.proxy(function(){						
                                                this.showPopup();
					}, this), this.options.showDelay)
            },
            __getParentForm: function(item) {
                return item.parents("form")[0];
            },
            __enterHandler: function(e) {                
               // this.showPopup();                   
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
            __clickHandler: function(e) {
                //this.showPopup();
            },
            __blurHandler: function(event) {
//                         this.timeoutId = window.setTimeout($.proxy(function(){
//						this.onblur();
//                                                this.hidePopup();
//					}, this), this.hidePopup);

            },
            __focusHandler: function(event) {
                //window.clearTimeout(this.timeoutId);

            },
            destroy: function() {
                // clean up code here

                // call parent's destroy method
                $super.destroy.call(this);
            }

        };
    })());
})(jQuery, RichFaces)