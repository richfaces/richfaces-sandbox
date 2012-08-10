(function ($) {

    $.widget('rf.orderingListBridge', {

        options: {
            hiddenInputSuffix: 'Input',
            pluginNames: 'orderingList, orderingListBridge'
        },

        _create: function() {
            var self = this;
            this._registerListeners();
            var hiddenInputId = $(this.element).attr('id') + this.options.hiddenInputSuffix;
            this.hiddenInput = $(document.getElementById(hiddenInputId)); // getElementById workaround for JSF ":" separator

        },

        destroy: function() {
            this._unRegisterListeners();
        },

        _registerListeners: function() {
            var self = this;
            $(this.element).orderingList('option', 'orderChanged', function(event, ui) {
                var csvKeys = ui.orderedKeys.join(',');
                self.hiddenInput.val(csvKeys);
            });
            this._registerCleanDomListener(self.element, this.options.pluginNames);
        },

        _unRegisterListeners: function() {
            $('body').off("cleanDom.orderingList.bootstrap.RICH");
        },

        // TODO: be refactored into a event-bridge base-widget
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