(function ($) {

    $.widget('rf.pickListBridge', $.rf.bridgeBase, {

        options: {
            componentId: null,
            hiddenInputSuffix: 'Input',
            pluginNames: 'pickList, pickListBridge'
        },

        _create: function() {
            $.rf.bridgeBase.prototype._create.call( this );
            var self = this;
            var componentId = this.options.componentId === null ? $(this.element).attr('id') : this.options.componentId;
            this.component = $(document.getElementById(componentId));
            this._registerListeners();
            var hiddenInputId = $(this.element).attr('id') + this.options.hiddenInputSuffix;
            this.element.parents("pickList").first().append(
                $('<input type="hidden" />').attr('id', hiddenInputId).attr('name', componentId)
            );

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
            $element.bind('picklist_change', function(event, ui) {
                var csvKeys = ui.pickedKeys.join(',');
                self.hiddenInput.val(csvKeys);
                ui.originalEvent = event;
                // bubble the event up to the dom element with the id of the component
                self.component.trigger('change.pickList.bootstrap.RICH', ui);
            });
            // Bind the client-provided change listeners
            if (this.options.onchange && typeof this.options.onchange == 'function') {
                this.component.bind('change.pickList.bootstrap.RICH', this.options.onchange);
            }
        },

        _unRegisterListeners: function() {
        }
    });

}(jQuery));