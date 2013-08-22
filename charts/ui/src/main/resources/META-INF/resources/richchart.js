(function( $ ) {

    $.widget( "rf.chart", {

        // These options will be used as defaults for all chart types
        options: {
            grid:{
                clickable:true,
                hoverable:true
            },
            tooltip:true,
            tooltipOpts: {
                content: "%s  [%x,%y]",
                shifts: {
                    x: 20,
                    y: 0
                },
                defaultTheme: false
            }
        },

        pieDefaults:{
             series:{
                pie:{
                    show:true
                }
             },
             tooltipOpts: {
                content: " %p.0%, %s",
                shifts: {
                    x: 20,
                    y: 0
                },
                defaultTheme: false
             }
        },
        dateDefaults:{
            xaxis:{
                mode:"time",
                timeformat:"%Y/%m/%d",
                minTickSize:[1,'day']  //TODO
            }
        },

        // Set up the widget
        _create: function() {
            this._handleTypeDependentOptions();
            this._draw();
            this._registerListeners();
        },

        _handleTypeDependentOptions:function(){

            if(this.options.charttype==='pie'){
                this.options = $.extend(this.options,this.pieDefaults);
                this.options.data = this.options.data[0]; //pie chart data should not be in a collection
            }
            else if(this.options.charttype==='bar'){
                if(this.options.xtype==='class java.lang.String'){
                    //category bar chart
                    var ticks=[],keys=[],first=true,order=0;

                    for(index in this.options.data){//loop through data series
                        var convertedData=[];
                        var cnt=0;
                        if(first){//the first series determine which keys (x-values are plotted)
                            for(key in this.options.data[index].data){
                                ticks.push([cnt,key]);
                                keys.push(key);
                                convertedData.push([cnt,this.options.data[index].data[key]]);
                                cnt++;
                            }
                            first=false;
                        }
                        else{
                            for(k in keys){ //select values for first series keys only
                                var key=keys[k];
                                if(this.options.data[index].data[key]){
                                    convertedData.push([cnt,this.options.data[index].data[key]]);
                                }
                                cnt++;
                            }
                        }
                        this.options.data[index].data=convertedData;
                        var bars={
                                order:order,
                                show:true
                        };
                        this.options.data[index].bars=bars;
                        order++;

                    }

                    this.options.xaxis={
                        ticks:ticks,
                        tickLength:0
                    };

                    this.options.bars={
                        show: true,
                        barWidth: 0.2,
                        align:'center'
                    };
                }
            }
            else if(this.options.charttype==='line'){
                if(this.options.zoom){
                    this.options.selection={mode: "xy"};
                }
                if(this.options.xtype==='class java.util.Date'){
                    this.options = $.extend(this.options,this.dateDefaults);
                    if(this.options.xaxis.format){
                        this.options.xaxis.timeformat=this.options.xaxis.format;
                    }
                }
            }
        },
        // Use the _setOption method to respond to changes to options
        _setOption: function( key, value ) {
            // In jQuery UI 1.8, you have to manually invoke the _setOption method from the base widget
            $.Widget.prototype._setOption.apply( this, arguments );
            // In jQuery UI 1.9 and above, you use the _super method instead
            this._super( "_setOption", key, value );
            
            var redraw=true;
            switch( key ) {
                case "zoom":
                case "handlers":
                case "particularSeriesHandlers":
                    this._unbind();
                    this._registerListeners();
                    redraw=false;
                    break;
            }

            if(redraw){
                this._draw();
            }
        },

        _draw:function(opts){
            if(opts){
                this.plot = $.plot(this.element,opts.data,opts);
            }
            else{
                this.plot = $.plot(this.element,this.options.data,this.options);
            }
        },

        _registerListeners:function(){
            if(this.options.zoom){
                this.element.bind("plotselected",this._getZoomFunction(this,this.element,this.options));
            }
            this.element.bind("plotclick",this._getPlotClickHandler(this.options,this.element));
            this.element.bind("plothover",this._getPlotHoverHandler(this.options,this.element));
            this.element.bind("mouseover",this.options.handlers.onmouseover);

        },

        _getPlotClickHandler:function(options,element){
            return function(event,mouse,item){
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
                    options.handlers.eventFunction(event,'plotclick',
                        event.data.seriesIndex,
                        event.data.dataIndex,
                        event.data.x,
                        event.data.y);

                    //client-side
                    if(options.handlers['onplotclick']){
                        options.handlers['onplotclick'].call(element,event);
                    }
                    //client-side particular series handler
                    if(options.particularSeriesHandlers['onplotclick'][event.data.seriesIndex]){
                        options.particularSeriesHandlers['onplotclick'][event.data.seriesIndex].call(element,event);
                    }
                }
            };
        },

        _getPlotHoverHandler: function(options,element){
            return function(event,mouse,item){
                if(item !== null){
                    //point in a chart clicked
                    event.data={
                        seriesIndex: item.seriesIndex,
                        dataIndex:   item.dataIndex,
                        x: item.datapoint[0],
                        y: item.datapoint[1],
                        item:item
                    };
                    //client-side
                    if(options.handlers['onmouseover']){
                        options.handlers['onmouseover'].call(element,event);
                    }
                    //client-side particular series handler
                    if(options.particularSeriesHandlers['onmouseover'][event.data.seriesIndex]){
                        options.particularSeriesHandlers['onmouseover'][event.data.seriesIndex].call(element,event);
                    }
                }
            };
        },



        _getZoomFunction : function(widget,element,options){
           return function(event,ranges){
               // clamp the zooming to prevent eternal zoom

               if (ranges.xaxis.to - ranges.xaxis.from < 0.00001) {
                   ranges.xaxis.to = ranges.xaxis.from + 0.00001;
               }

               if (ranges.yaxis.to - ranges.yaxis.from < 0.00001) {
                   ranges.yaxis.to = ranges.yaxis.from + 0.00001;
               }

               // do the zooming

               widget._draw($.extend({}, widget.options, {
                       xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to },
                       yaxis: { min: ranges.yaxis.from, max: ranges.yaxis.to }
                   })
               );
           };
        },

        resetZoom: function(){
             //redraw chart without axis ranges
            this._draw();
        },
                
        getPlotObject: function(){
            return this.plot;
        },        
                
        _unbind:function(){
            this.element.unbind("plotclick");
            this.element.unbind("plothover");
            this.element.unbind("plotselected");
            this.element.unbind("mouseout");
        },

        // Use the destroy method to clean up any modifications your widget has made to the DOM
        destroy: function() {
            this.plot.shutDown();
            this._unbind();
            // In jQuery UI 1.8, you must invoke the destroy method from the base widget
            $.Widget.prototype.destroy.call( this );
            // In jQuery UI 1.9 and above, you would define _destroy instead of destroy and not call the base method
        }
    });
}( jQuery ) );