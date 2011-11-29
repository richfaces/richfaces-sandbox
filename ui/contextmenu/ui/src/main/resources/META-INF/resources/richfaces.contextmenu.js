/**
 * This function evaluates code in template with object in ScopeChain.
 * This is usefull if you need to evaluate code that uses member names
 * that colide with external names that the code refers to.
 * There is almost exact method in utils.js called Richfaces.eval,
 * but it swallows exception thrown during evaluation, which makes debugging
 * hard.
 */
(function ($, rf) {
    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.ContextMenu = rf.BaseComponent.extendClass({
        // class name
        name:"ContextMenu",
        init: function (componentId, options) {
            var contextmenu = this;
            this.element = document.getElementById(componentId);
            if (!this.element) {
                throw "No element with id '" + componentId + "' found.";
            }
            this.container = jQuery(this.element);
            this.options = $.extend({attachTo:document.body}, options);
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            // ...
            /**
             * Plugin init on document load
             */
            $(function() {
                $(document.getElementById(contextmenu.options.attachTo)).contextMenu({
                    menu: contextmenu.element
                });
            });
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
    var $super = rf.ui.ContextMenu.$super;
})(jQuery, RichFaces);