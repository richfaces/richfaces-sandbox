(function ($) {

    $.widget('rf.orderingList', $.rf.orderingListCommon, {

        options: {
            cssClass: "with-handle",
            filter: "li"
        },

        _create: function() {
            this._addDomElements();
            this.widgetEventPrefix = "orderingList_";

            if ($(this.element).is("table")) {
                this._createTable();
            } else {
                this._createList();
            }

        },

        _createTable: function() {
            var that = this;
            $( this.element).find("tbody")
                .sortable({ handle: ".handle", helper: $.proxy(this._rowHelper, this),
                    start: function(event, ui) {
                        $(that.element).find(".ui-selected").removeClass('ui-selected');
                        $(ui.item).addClass('ui-selected');
                    }})
                .selectable({filter: this.options.filter});
            $( this.element )
                .find( "tr" )
                .prepend( "<th><div class='handle'><i class='icon-move'></i></div></th>" );
        },

        _createList: function() {
            var that = this;
            $( this.element )
                .sortable({ handle: ".handle",
                    start: function(event, ui) {
                        $(that.element).find(".ui-selected").removeClass('ui-selected');
                        $(ui.item).addClass('ui-selected');
                    }})
                .selectable({filter: this.options.filter});
            $( this.element )
                .find( "li" )
                .prepend( "<div class='handle'><i class='icon-move'></i></div>" );
        },

        _rowHelper: function(e, tr) {
            var $helper = tr;
            $helper.css('border-radius', '5px');
            $helper.children().last().addClass("borderRadiusRight");
            return $helper;
        }
    });

}(jQuery));