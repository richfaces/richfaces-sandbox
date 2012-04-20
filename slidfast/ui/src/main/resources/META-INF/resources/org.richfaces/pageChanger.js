(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.PageChanger = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom();
        if (! window.slidfast.slidfast) {
            slidfast({
                defaultPageID: mergedOptions.defaultPageID,
                callback: $.proxy(this.callback, this),
                backButtonID: 'back-button'
            });
        }
        this.activePageId = null;
        this.dynamicPageID = mergedOptions.dynamicPageID;
    };

    rf.BaseComponent.extend(rf.ui.PageChanger);
    var $super = rf.ui.PageChanger.$super;

    var defaultOptions = {
        defaultPageID: 'home-page',
        dynamicPageID: 'app-page'
    }

    $.extend(rf.ui.PageChanger.prototype, (function () {

        return {
            name : "pageChanger",
            request_param : "org.richfaces.slidfast.activePage",

            callback : function(pageId) {

                if (this.activePageId === pageId) {
                    slidfast.core.slideTo(this.dynamicPageID);
                } else {
                    this.renderPage(pageId);
                }
            },

            renderPage: function(pageId) {
                var clientParameters = {};
                clientParameters[this.request_param] = pageId;
                var that = this;
                var ajaxSuccess = function (event) {
                    slidfast.core.slideTo(that.dynamicPageID);
                }

                rf.ajax(this.id, null, {clientParameters: clientParameters ,incId :"1", complete : ajaxSuccess} );
                this.activePageId = pageId;
            },

            setActivePage: function(pageId) {
                this.activePageId = pageId;
            }

        };
    })());

})(jQuery, window.RichFaces);