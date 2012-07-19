(function ($) {

    $.widget('rf.orderingListWidget', $.rf.orderingListCommon, {

        options: {
            disabled: false,
            color: 'red'
        },
        
        /** jQuery UI widget factory callback methods **/

        _create: function () {
            this._addDomElements();
            this.widgetEventPrefix = "orderingList_";
        },

        _init: function () {
            if (this.options.disabled !== true) {
                $(this.element).bind('click.orderingList', $.proxy(this._itemClickHandler, this));
            }
        },

        _setOption: function(key, value) {
            if (this.options[key] === value) {
                return this; // Do nothing, the value didn't change
            }

            switch (key){
                case "disabled":
                    if (value === true) {
                        this.unSelectAll();
                        $.Widget.prototype.disable.call(this);
                    } else {
                        this.init();
                    }
                    break;
                default:
                    this.options[key] = value;
            }
        },

        destroy: function() {
            $(this.element).unbind('click.orderingList');
            this.unSelectAll();
            $(this.element).removeClass('orderingList');
            $.Widget.prototype.destroy.call(this);
        },

        /** Event Handlers **/

        _itemClickHandler: function(event) {
            var item = event.target;
            if (this.isSelected(item)) {
                this.unSelectItem(item);
                this._trigger('item_selected', event , item);
            } else {
                this.selectItem(item);
                this._trigger('item_selected', event , item);
            }
        }
    });

}(jQuery));