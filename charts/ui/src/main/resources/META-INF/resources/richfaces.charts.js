(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.chart = function (id, data, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        
        
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