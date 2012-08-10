(function ($) {

    $.widget('rf.orderingListBridge', {

        options: {
            hiddenInputSuffix: 'Input'
        },

        _create: function() {
            this._registerListeners();
            var hiddenInputId = $(this.element).attr('id') + this.options.hiddenInputSuffix;
            this.hiddenInput = $(document.getElementById(hiddenInputId)); // getElementById workaround for JSF ":" separator
            var $element = $(this.element);
            $element
                .attr('data-jboss-cleanup', true)
//                .data('jboss-cleanup', true)
                .on('cleanup.RICH', $element.data('orderingList').destroy);
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