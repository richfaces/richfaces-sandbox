(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.chart = function (id, data, options,eventHandlers,seriesSpecificHandlers) {
        var escId = RichFaces.escapeCSSMetachars(id);
        if($("#"+escId)===[]){
            throw "Element with id '"+id+"' not found.";
        }
        
        var mergedOptions = $.extend({}, defaultOptions, options);

        
        this.init(eventHandlers,seriesSpecificHandlers);
        
        $super.constructor.call(this, escId, mergedOptions);
        
        this.plot = $.plot("#"+escId,data,mergedOptions);
        
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.escId);
        this.attachToDom();
    };

    rf.ui.Base.extend(rf.ui.chart);
    var $super = rf.ui.chart.$super;

    var defaultOptions = {
        grid:{
            clickable:true,
            hoverable:true
        }
    };

    $.extend(rf.ui.chart.prototype, //(function () {
        /*return*/{
            name:"chart",
            
            init: function(eventHandlers,seriesSpecificHandlers){
                this.eventHandlers = eventHandlers;
                this.seriesEventHandlers = seriesSpecificHandlers;
            },
        
            __events:{
                'onplotclick':'plotclick',
                'onmouseover':'plothover',
                'onmouseout':'mouseout'
                        
            },     
            __bindEventHandlers:function(){
                for(e in this.__events){            //loop events handled
                    if(this.eventHandlers[e]){      //is there handler for this ev
                        $('#'+this.id).bind(
                                this.__events[e],   //event name
                                this.__getHandlerFunction(
                                        this.id,
                                        this.eventHandlers,
                                        this.seriesEventHandlers,
                                        e));
                    }
                }
                
                
        
            },
            __getHandlerFunction: function(id,handlers,seriesHandlers,eventName){
                if(eventName==='onplotclick'){
                    return function(event,pos,item){
                        if(item !== null){
                            //point in a chart clicked
                            event.data={
                                seriesIndex: item.seriesIndex,
                                dataIndex:   item.dataIndex,
                                x: item.datapoint[0],
                                y: item.datapoint[1],
                                item:item
                            };
                            //server-side 
                            //!!! will not work withou client cause of if in __bindEventHandlers
                            handlers.eventFunction(event,'plotclick',
                                event.data.seriesIndex,
                                event.data.dataIndex,
                                event.data.x,
                                event.data.y);
                            //client-side
                            handlers[eventName].call($('#'+id),event);  
                            //client-side series specific
                            //if(seriesHandlers[eventName][event.data.seriesIndex]!==null){
                            //    seriesHandlers[eventName][event.data.seriesIndex].call($('#'+id),event);  
                            //}
                        }
                    };
                }
                else if(eventName==='onmouseover'){
                    return function(event,pos,item){
                        if(item !== null){
                            //point in a chart clicked
                            event.data={
                                seriesIndex: item.seriesIndex,
                                dataIndex:   item.dataIndex,
                                x: item.datapoint[0],
                                y: item.datapoint[1],
                                item:item
                            };
                            handlers[eventName].call($('#'+id),event);
                        }
                    };
                }
                else if(eventName==='onmouseout'){
                    return function(){
                        handlers[eventName].call($('#'+id));
                    };
                }
            }        
            

            // place shared prototype attributes and methods here.  Prepend "__" ahead of private methods
        }//;
    //})()
    );

})(jQuery, window.RichFaces);