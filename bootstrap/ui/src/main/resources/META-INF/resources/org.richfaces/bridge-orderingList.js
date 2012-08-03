(function ($) {

    $.widget('rf.orderingListBridge', {

        options: {
            hiddenInputSuffix: 'Input'
        },

        _create: function() {
            this._registerListeners();
            var hiddenInputId = $(this.element).attr('id') + this.options.hiddenInputSuffix;
            this.hiddenInput = $(document.getElementById(hiddenInputId)); // getElementById workaround for JSF ":" separator
        },

        _registerListeners: function() {
            var self = this;
            $(this.element).orderingList('option', 'orderChanged', function(event, ui) {
                var csvKeys = ui.orderedKeys.join(',');
                self.hiddenInput.val(csvKeys);
            });
        }

    });

}(jQuery));