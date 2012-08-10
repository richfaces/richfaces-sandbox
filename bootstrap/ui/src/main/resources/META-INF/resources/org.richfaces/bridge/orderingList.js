(function ($) {

    $.widget('rf.orderingListBridge', {

        options: {
            hiddenInputSuffix: 'Input'
        },

        _create: function() {
            var self = this;
            this._registerListeners();
            var hiddenInputId = $(this.element).attr('id') + this.options.hiddenInputSuffix;
            this.hiddenInput = $(document.getElementById(hiddenInputId)); // getElementById workaround for JSF ":" separator
            this._registerCleanDomListener(self.element, 'orderingList');
        },

        _registerListeners: function() {
            var self = this;
            $(this.element).orderingList('option', 'orderChanged', function(event, ui) {
                var csvKeys = ui.orderedKeys.join(',');
                self.hiddenInput.val(csvKeys);
            });
        },

        _registerCleanDomListener: function (element, pluginName) {
            $('body').on("cleanDom.bootstrap.RICH", function(event, ui) {
                if ($.contains(ui.target, element)) {
                    $(element).data(pluginName).destroy();
                }
            });
        }

    });

}(jQuery));