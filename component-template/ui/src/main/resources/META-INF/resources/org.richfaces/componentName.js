(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.ComponentName = function (id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom();
    };

    rf.BaseComponent.extend(rf.ui.ComponentName);
    var $super = rf.ui.ComponentName.$super;

    var defaultOptions = {
        someOption:'devaultValue'
    }

    $.extend(rf.ui.ComponentName.prototype, (function () {

        return {
            name:"componentName"

            // place shared prototype attributes and methods here.  Prepend "__" ahead of private methods
        };
    })());

})(jQuery, window.RichFaces);