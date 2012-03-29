(function ($, rf) {

    var fireChangeListener = function(event, options) {
        if (typeof(options.onchange) === "function") {
            options.onchange(event);
        } else if (typeof(options.onchange) === "string") {
            eval(options.onchange);
        }
    };
    var fireSelectListener = function(event, options) {
        if (typeof(options.onselect) === "function") {
            options.onselect(event);
        } else if (typeof(options.onselect) === "string") {
            eval(options.onselect);
        }
    };
    var updateInput = function(event, input) {
        input.value = event.x + ";" + event.y + ";" + event.width + ";" + event.height;
    };

    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    var defaultOptions = {
        onchange:null,
        onselect:null,
        backgroundOpacity:.9,
        targetId:null
    };
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.ImageSelectTool = rf.BaseComponent.extendClass({
        // class name
        name:"ImageSelectTool",
        init: function (componentId, options) {
            if (!document.getElementById(componentId)) {
                throw "No element with id '" + componentId + "' found.";
            }
            options = $.extend(defaultOptions, options);
            this.options = options;
            if (rf.$(componentId) != null) {
                rf.$(componentId).destroy();
            }
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            // ...

            var that = this;
            options = options || {};
            options.onChange = function(event) {
                event = {
                    x:event.x,
                    y:event.y,
                    width:event.w,
                    height:event.h
                };
                updateInput(event, rf.getDomElement(that.options.inputId));
                fireChangeListener(event, that.options);
            };
            options.onSelect = function(event) {
                event = {
                    x:event.x,
                    y:event.y,
                    width:event.w,
                    height:event.h
                };
                updateInput(event, rf.getDomElement(that.options.inputId));
                fireSelectListener(event, that.options);
            };
            if (options.minWidth != null && options.minHeight != null) {
                options.minSize = [ options.minWidth, options.minHeight ];
            }
            if (options.maxWidth != null && options.maxHeight != null) {
                options.maxSize = [ options.maxWidth, options.maxHeight ];
            }
            if (options.selection) {
                options.setSelect = [
                    options.selection.x,
                    options.selection.y,
                    options.selection.x + options.selection.width,
                    options.selection.y + options.selection.height
                ];
            }
            options.bgColor = options.backgroundColor;
            options.bgOpacity = options.backgroundOpacity;

            //initialize Jcrop
            function initialize() {
                var image = document.getElementById(options.targetId);

                function doInitialize() {
                    that.delegate = jQuery.Jcrop(image, options);
                }

                if (image.complete) {
                    doInitialize();
                } else {
                    image.onload = doInitialize;
                }
            }

//    if (options.ajaxRequest) {
//        initialize();
//    } else {
//        jQuery(initialize());
//    }
            jQuery(initialize);
        },
        // private functions definition
        __getDelegate : function() {
            return this.delegate;
        },
        setSelection : function(x, y, w, h) {
            var event = {x:x,y:y,width:w,height:h};
            updateInput(event, rf.getDomElement(this.options.inputId));
            this.__getDelegate().setSelect([x,y,x + w,y + h]);
            fireSelectListener(this.getSelection(), this.options);
        },
        animateTo : function(x, y, w, h) {
            var event = {x:x,y:y,width:w,height:h};
            updateInput(event, rf.getDomElement(this.options.inputId));
            this.__getDelegate().animateTo([x,y,x + w,y + h]);
            fireSelectListener(event, this.options);
        },
        setOptions : function(options) {
            this.options = options;
            this.__getDelegate().setOptions(options);
        },
        getSelection : function() {
            var selection = this.__getDelegate().tellSelect();
            return {
                x:selection.x,
                y:selection.y,
                width:selection.w,
                height:selection.h
            };
        },
        getScaledSelection : function() {
            var selection = this.__getDelegate().tellScaled();
            return {
                x:selection.x,
                y:selection.y,
                width:selection.w,
                height:selection.h
            };
        },
        disable : function() {
            this.__getDelegate().disable();

        },
        enable : function() {
            this.__getDelegate().enable();
        },
        focus : function() {
            this.__getDelegate().focus();
        },
        getBounds : function() {
            return this.__getDelegate().getBounds();
        },
        getWidgetSize : function() {
            return this.__getDelegate().getWidgetSize();
        },
        release : function() {
            this.__getDelegate().release();
        },
        // destructor definition
        destroy: function () {
            this.__getDelegate().destroy();
            this.detach();
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.ImageSelectTool.$super;
})(jQuery, RichFaces);