(function ($, rf) {


    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    //    var defaultOptions = {};
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.AccessKeyHelper = rf.BaseComponent.extendClass({
        // class name
        name:"AccessKeyHelper",
        init: function (componentId, options) {
            options = $.extend({highlightClass:'rf-ak-h',popupClass:'rf-ak-p',accessKeyTagClass:'rf-ak-u'}, options);
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            // ...
            $.keyTips(options);
        },
        // private functions definition
        // destructor definition
        destroy: function () {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.AccessKeyHelper.$super;
})(jQuery, RichFaces);