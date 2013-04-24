/**
 * This function evaluates code in template with object in ScopeChain.
 * This is usefull if you need to evaluate code that uses member names
 * that colide with external names that the code refers to.
 * There is almost exact method in utils.js called Richfaces.eval,
 * but it swallows exception thrown during evaluation, which makes debugging
 * hard.
 */
(function ($, rf)
{
    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    //    var defaultOptions = {};
    var SUBMIT_EVENT_FUNCTION = 'submitEventFunction';
    var DASHBOARD_CHANGE = "dashboardChanged";
    var DASHBOARD_START_POSITION = "dashboardStartPosition";
    var ORIGINAL_MIN_HEIGHT = "dashboardOriginalMinHeight";

    /**
     * Calculates child index for given item within it's parent.
     * @param item item to get index for
     */
    var getIndex = function(item)
    {
        var index = -1;
        while (item != null && item.size() > 0) {
            item = item.prev();
            index++;
        }
        return index;
    };
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.Dashboard = rf.BaseComponent.extendClass({
        // class name
        name:"Dashboard",
        init: function (componentId, options)
        {
            var dashboard = this;
            this.element = document.getElementById(componentId);
            if (!this.element) {
                throw "No element with id '" + componentId + "' found.";
            }
            this.container = jQuery(this.element);
            this.options = options;
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            // ...

            var startHandler = function(event, data)
            {
                /**
                 * Store original position
                 */
                var y = getIndex(data.item);
                var x = getIndex(data.item.parent());
                data.item.data(DASHBOARD_START_POSITION, {x:x,y:y});
                /**
                 * set min-height to all columns so that it's easy to move components accross columns
                 */
                var columns = jQuery(".rf-db-cl", dashboard.container);
                columns.css("minHeight", "");
                columns.each(function()
                {
                    var jthis = jQuery(this);
                    jthis.data(ORIGINAL_MIN_HEIGHT, jthis.css("minHeight"));
                    jthis.css("minHeight", 0);
                });
                var maxHeight = 0;
                columns.each(function()
                {
                    var height = jQuery(this).height();
                    if (height > maxHeight) {
                        maxHeight = height;
                    }
                });
                columns.css("minHeight", maxHeight + 50);
            };
            var stopHandler = function(event, data)
            {
                var columns = jQuery(".rf-db-cl", dashboard.container);
                columns.each(function()
                {
                    var jthis = jQuery(this);
                    var originalMinHeight = jthis.data(ORIGINAL_MIN_HEIGHT);
                    jthis.css("minHeight", originalMinHeight == null ? 0 : originalMinHeight);
                });
                var changed = data.item.data(DASHBOARD_CHANGE);
                data.item.data(DASHBOARD_CHANGE, null);
                var y = getIndex(data.item);
                var x = getIndex(data.item.parent());
                if (changed) {
                    var sP = data.item.data(DASHBOARD_START_POSITION);
                    if (dashboard.__executeEventHandler("beforechange", {event:event,item:data.item,startX:sP.x,startY:sP.y,endX:x,endY:y}) === false) {
                        dashboard.cancel();
                        return;
                    }
                    if (dashboard.options[SUBMIT_EVENT_FUNCTION] != null) {
                        dashboard.options[SUBMIT_EVENT_FUNCTION](event, 'change', sP.x, sP.y, x, y);
                    }
                    dashboard.__executeEventHandler("change", {event:event,item:data.item,startX:sP.x,startY:sP.y,endX:x,endY:y});
                }
            };
            var updateHandler = function(event, data)
            {
                /**
                 * Store info about change in item because this method is fired for every column
                 * (dropping 1 item may call this method 3 times if there are 3 columns)
                 */
                data.item.data(DASHBOARD_CHANGE, true)
            };
            this.options.disabled = !this.options.enabled;
            this.options.placeholder = this.options["placeholderClass"];
            var placeholderRF = "rf-db-plh";
            this.options = jQuery.extend({
                placeholder:placeholderRF,
                forcePlaceholderSize:true,
                tolerance:"pointer",
                disabled:false,
                start:startHandler,
                stop:stopHandler,
                update:updateHandler
            }, this.options);
            this.options = jQuery.extend(this.options, {
                connectWith : jQuery(".rf-db-cl", this.container)
            });
            if (this.options.placeholder != placeholderRF) {
                this.options.placeholder = placeholderRF + " " + this.options.placeholder;
            }

            /**
             * Plugin init on document load
             */
            jQuery(function()
            {
                var sortableContainer = jQuery(".rf-db-cl", dashboard.container);
                sortableContainer.sortable(dashboard.options);
                if (dashboard.options.enabled !== false) {
                    sortableContainer.disableSelection();
                }
            });
        },
        // private functions definition
        /**
         * Utility functions.
         */
        __getResponseComponentData : function(event)
        {
            return event.componentData[this.id];
        },
        /**
         * Executes event handler passed in options.
         * @param eventName name of the event
         * @param context hash of variables that should be placed in scope when during evaluation
         */
        __executeEventHandler : function(eventName, context)
        {
            if (this.options[eventName] instanceof Function) {
                return this.options[eventName].apply(this, context);
            } else if (this.options[eventName] != null) {
                return rf.ui.Dashboard.eval("(function(){" + this.options[eventName] + "})()", context);
            } else {
                return null;
            }
        },
        __getDelegate : function()
        {
            return jQuery(".rf-db-cl", this.container);
        },
        cancel : function ()
        {
            this.__getDelegate().sortable("cancel");
        },
        enable : function ()
        {
            this.__getDelegate().sortable("enable");
            this.__getDelegate().disableSelection();
        },
        disable : function ()
        {
            this.__getDelegate().sortable("disable");
            this.__getDelegate().enableSelection();
        },
        // destructor definition
        destroy: function ()
        {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
        }
    });
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Dashboard.$super;
})(jQuery, RichFaces);
RichFaces.ui.Dashboard.eval = function(template, object)
{
    var value;
    with (object) {
        value = eval(template);
    }
    return value;
};

/**
 * Add :focus selector to jQuery since it is in 1.6 and RF is shipped with 1.5
 */
(function ($)
{
    var filters = $.expr[":"];
    if (!filters.focus) {
        filters.focus = function(elem)
        {
            return elem === document.activeElement && ( elem.type || elem.href );
        };
    }
})(jQuery);
