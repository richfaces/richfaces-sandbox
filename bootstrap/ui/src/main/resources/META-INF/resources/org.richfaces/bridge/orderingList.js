(function ($) {

    $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

        options: {
            hiddenInputSuffix: 'Input',
            pluginNames: 'orderingList, orderingListBridge'
        },

        _create: function() {
            $.rf.bridgeBase.prototype._create.call( this );
            var self = this;
            this._registerListeners();
            var hiddenInputId = $(this.element).attr('id') + this.options.hiddenInputSuffix;
            this.hiddenInput = $(document.getElementById(hiddenInputId)); // getElementById workaround for JSF ":" separator

        },

        destroy: function() {
            $.rf.bridgeBase.prototype.destroy.call( this );
            this._unRegisterListeners();
        },

        _registerListeners: function() {
            var self = this;
            $(this.element).orderingList('option', 'orderChanged', function(event, ui) {
                var csvKeys = ui.orderedKeys.join(',');
                self.hiddenInput.val(csvKeys);
            });
        },

        _unRegisterListeners: function() {
        }
    });

}(jQuery));