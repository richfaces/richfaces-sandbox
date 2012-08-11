(function ($) {

    $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

        options: {
            componentId: null,
            hiddenInputSuffix: 'Input',
            pluginNames: 'orderingList, orderingListBridge'
        },

        _create: function() {
            $.rf.bridgeBase.prototype._create.call( this );
            var self = this;
            this.component = this.options.componentId === null ? $(this.element) : $(document.getElementById(this.options.componentId));
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
            var $element = $(this.element);
            // the widget factory converts all events to lower case
            $element.bind('orderinglist_orderchanged', function(event, ui) {
                var csvKeys = ui.orderedKeys.join(',');
                self.hiddenInput.val(csvKeys);
                ui.originalEvent = event;
                // bubble the event up to the dom element with the id of the component
                self.component.trigger('orderChanged.orderingList.bootstrap.RICH', ui);
            });
            // Bind the client-provided orderChanged listeners
            if (this.options.onOrderChanged && typeof this.options.onOrderChanged == 'function') {
                this.component.bind('orderChanged.orderingList.bootstrap.RICH', this.options.onOrderChanged);
            }
        },

        _unRegisterListeners: function() {
        }
    });

}(jQuery));