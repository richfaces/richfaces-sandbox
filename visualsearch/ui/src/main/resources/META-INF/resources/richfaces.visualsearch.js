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
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.Visualsearch = rf.BaseComponent.extendClass({
        // class name
        name:"Visualsearch",
        init: function (componentId, options)
        {
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
            options = jQuery.extend({
                container : jQuery(document.getElementById(this.id)),
                query     : '',
                unquotable: ["state"],
                callbacks : {
                    search: function(query, searchCollection)
                    {
                        _this.__search(query, searchCollection)
                    },
                    valueMatches: function(facet, searchTerm, callback)
                    {
                        _this.__valueMatches(facet, searchTerm, callback)
                    },
                    facetMatches: function(searchTerm, callback)
                    {
                        _this.__facetMatches(searchTerm, callback)
                    }
                }
            }, options);
            var _this = this;
            jQuery(function()
            {
                _this.delegate = VS.init(options);
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
        __executeInlineEventHandler : function(eventName, context)
        {
            if (this.options[eventName] != null) {
                return rf.ui.Visualsearch.eval("(function(){" + this.options[eventName] + "})()", context);
            } else {
                return null;
            }
        },
        __getDelegate : function()
        {
            return this.delegate;
        },
        __valueMatches : function (facet, searchTerm, callback)
        {
            if(this.options.values !=null) {
                callback(this.options.values[facet]);
                return;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION]({} /* stub event */, 'suggestValue', null, null, facet, searchTerm, function(event)
                {
                    var data = _this.__getResponseComponentData(event);
                    if (data != undefined) {
                        callback(data);
                    }
                });
            }
        },
        __facetMatches : function (searchTerm, callback)
        {
            if(this.options.facets !=null) {
                callback(this.options.facets);
                return;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION]({} /* stub event */, 'suggestFacet', null, null, null, searchTerm, function(event)
                {
                    var data = _this.__getResponseComponentData(event);
                    if (data != undefined) {
                        callback(data);
                    }
                });
            }
        },
        __search : function(query, searchCollection)
        {
            var queryJSON = {};
            searchCollection.each(function(model)
            {
                var attributeName = model.get("category");
                //noinspection UnnecessaryLocalVariableJS
                var attributeValue = model.get("value");
                queryJSON[attributeName] = attributeValue;
            });
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                document.getElementById(this.id).value=query;
                this.options[SUBMIT_EVENT_FUNCTION]({} /* stub event */, 'search', query, JSON.stringify(queryJSON), null, null, function(event)
                {
                    var data = _this.__getResponseComponentData(event);
                    if (data != undefined) {
                        callback(data);
                    }
                    _this.__executeInlineEventHandler('onsearch', { 'query':query, 'queryJSON':queryJSON});
                });
            } else {
                this.__executeInlineEventHandler('onsearch', { 'query':query, 'queryJSON':queryJSON});
            }
        }
        ,
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
    var $super = rf.ui.Visualsearch.$super;
})(jQuery, RichFaces);
RichFaces.ui.Visualsearch.eval = function(template, object)
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
