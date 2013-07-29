(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.chart = function (id, data, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        
        
        /*var d1=[[0,2],[3,5],[10,4]];
        var d2=[[1,5],[2,6],[9,8]];

        var lineData=[{data:d1,label:"whatever"},{data:d2,label:"series 2"}];


        var plot=$.plot("#"+id,lineData,mergedOptions);
        */
        var plot=$.plot("#"+id,data,mergedOptions);
        
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom();
    };

    rf.BaseComponent.extend(rf.ui.chart);
    var $super = rf.ui.chart.$super;

    var defaultOptions = {
        someOption:'devaultValue'
    };

    $.extend(rf.ui.chart.prototype, (function () {

        return {
            name:"chart"

            // place shared prototype attributes and methods here.  Prepend "__" ahead of private methods
        };
    })());

})(jQuery, window.RichFaces);