(function ($, rf) {


    /**
     * Copies attributes from one objects to other object, but
     * can change the name of target attributes.
     */
    function extend(target, source, translation) {
        for (var attr in source) {
            var targetAttr = translation[attr] != null ? translation[attr] : attr;
            target[targetAttr] = source[attr];
            if (attr != 'stack' && target[targetAttr] instanceof Object) {
                target[targetAttr] = extend({}, target[targetAttr], translation);
            }
        }
        return target;
    }

    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    //    var defaultOptions = {};
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.Carousel = rf.BaseComponent.extendClass({
        // class name
        name:"Carousel",
        init: function (componentId, options) {
            if (!document.getElementById(componentId)) {
                throw "No element with id '" + componentId + "' found.";
            }
            this.options = options;
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            // ...
            /**
             * Message bundle & event handlers setup.
             */
            options = extend({
                slide_class:"rf-crl-sl",
                content_button_class:"rf-crl-cbtn",
                thumbnail_button_class:"rf-crl-thbtn",
                transition_time:300,
                transition_type:"slide",
                carousel_data:jQuery(".rf-crl-sl",document.getElementById(componentId)),
                timer:4000
            }, options, {
                width:"carousel_outer_width",
                height:"carousel_outer_height",
                slideWidth:"slide_width",
                slideHeight:"slide_height",
                continuousScrolling:"continuous_scrolling",
                changeOnHover:"change_on_hover",
                visibleSlides:"number_slides_visible",
                transitionType:"transition_type",
                transitionTime:"transition_time",
                interval:"timer",
                contentButtonClass:"content_button_class"
            });
            var _this = this;
            jQuery(function() {
                _this.delegate = $(document.getElementById(componentId)).agile_carousel(options);
            });
        },
        // private functions definition
        __getDelegate : function() {
            return this.delegate;
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
    var $super = rf.ui.Carousel.$super;
})(jQuery, RichFaces);