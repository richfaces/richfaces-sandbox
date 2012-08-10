(function ($) {

    $.widget('rf.bridgeBase', {

        options: {
            pluginNames: null
        },

        _create: function () {
          this._registerListeners();
        },

        destroy: function() {
            this._unRegisterListeners();
        },

        _registerListeners: function() {
            this._registerCleanDomListener(this.element, this.options.pluginNames);
        },

        _unRegisterListeners: function() {
            $('body').off("cleanDom.orderingList.bootstrap.RICH");
        },

        _registerCleanDomListener: function (element, pluginNames) {
            $('body').on("cleanDom.orderingList.bootstrap.RICH", function(event, ui) {
                if ($.contains(ui.target, element)) {
                    $.each(pluginNames, function() {
                        var pluginName = this;
                        $(element).data(pluginName).destroy();
                    })

                }
            });
        }
    });

}(jQuery));