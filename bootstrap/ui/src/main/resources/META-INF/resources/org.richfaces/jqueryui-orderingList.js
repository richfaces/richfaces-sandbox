(function ($) {

    $.widget('rf.orderingList', $.rf.orderingListCommon, {

        options: {
            cssClass: "with-handle"
        },

        _create: function() {
            this._addDomElements();
            this.widgetEventPrefix = "orderingList_";

            var that = this;
            $( this.element )
                .sortable({ handle: ".handle" ,
                    start: function(event, ui) {
                        $('li', that.element).removeClass('ui-selected');
                        $(ui.item).addClass('ui-selected');
                    }})
                .selectable()
                .find( "li" )
                .prepend( "<div class='handle'><i class='icon-move'></i></div>" );
        }
    });

}(jQuery));