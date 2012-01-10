(function ($, rf) {
    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    var defaultOptions = {};
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.Watermark = rf.BaseComponent.extendClass({
        // class name
        name:"Watermark",
        init: function (componentId, options) {
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            jQuery(function() {
                options.className = options['styleClass'];
                jQuery(document.getElementById(options.targetId)).watermark(options.text, options);
            });
        },
        // destructor definition
        destroy: function () {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Watermark.$super;
})(jQuery, RichFaces);