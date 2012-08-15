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
            $element.bind('orderinglist_change', function(event, ui) {
                var csvKeys = ui.orderedKeys.join(',');
                self.hiddenInput.val(csvKeys);
                ui.originalEvent = event;
                // bubble the event up to the dom element with the id of the component
                self.component.trigger('change.orderingList.bootstrap.RICH', ui);
            });
            // Bind the client-provided change listeners
            if (this.options.onchange && typeof this.options.onchange == 'function') {
                this.component.bind('change.orderingList.bootstrap.RICH', this.options.onchange);
            }
        },

        _unRegisterListeners: function() {
        }
    });

}(jQuery));