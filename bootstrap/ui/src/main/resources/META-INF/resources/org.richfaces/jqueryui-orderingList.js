(function ($) {

    $.widget('rf.orderingList', {

        options: {
            cssClass: '',
            disabled: false,
            headerText: ''
        },

        _create: function() {
            if ($(this.element).is("table")) {
                this.strategy = "table";
                this.filter = "tr";
                this.helper = $.proxy(this._rowHelper, this);
                var $pluginRoot = $( this.element).find("tbody");
            } else {
                this.strategy = "list";
                this.filter = "li";
                this.helper = "original"
                var $pluginRoot = $( this.element);
            }
            this._addDomElements();
            this.widgetEventPrefix = "orderingList_";
            var that = this;
            $pluginRoot
                .sortable({ handle: ".handle", helper: this.helper,
                    start: function(event, ui) {
                        $(that.element).find(".ui-selected").removeClass('ui-selected');
                        $(ui.item).addClass('ui-selected');
                    }})
                .selectable({filter: this.filter});
        },

        _rowHelper: function(e, tr) {
            var $helper = tr.clone();
            $helper.addClass("ui-selected rowhelper");
            return $helper;
        },

        /** Public API methods **/

        isSelected: function (item) {
            return $(item).hasClass('ui-selected');
        },

        selectItem: function (item) {
            $(item).addClass('ui-selected');
        },

        unSelectItem: function (item) {
            $(item).removeClass('ui-selected');
        },

        unSelectAll: function() {
            var that = this;
            this._removeDomElements();
            $(this.element).children().each(function() {
                that.unSelectItem(this);
            });
        },

        moveTop: function (items) {
            var first = items.prevAll().not('.ui-selected').last();
            $(items).insertBefore(first);
        },

        moveUp: function (items) {
            $(items).each( function () {
                var $item = $(this);
                var prev = $item.prevAll().not('.ui-selected').first();
                if (prev.length > 0) {
                    $item.insertBefore(prev);
                }
            });
        },

        moveDown: function (items) {
            $(items).sort(function() {return 1}).each( function () {
                var $item = $(this);
                var next = $item.nextAll().not('.ui-selected').first();
                if (next.length > 0) {
                    $item.insertAfter(next);
                }
            });
        },

        moveLast: function (items) {
            var last = items.nextAll().not('.ui-selected').last();
            $(items).insertAfter(last);
        },

        /** Initialisation methods **/

        _addDomElements: function() {
            $(this.element).addClass("list").wrap(
                jQuery("<div />").addClass('orderingList container-fluid with-handle').addClass(this.options.cssClass).append(
                    jQuery('<div />').addClass('content row-fluid').append(
                        jQuery('<div />').addClass('span10')
                    )
                )
            );
            this.outer = $(this.element).parents(".orderingList").first();
            this.outer.prepend(
                jQuery("<div />").addClass("row-fluid").append(
                    jQuery("<div />").addClass('span12 header').append(
                        jQuery("<h3/>").html(this.options.headerText)
                    )
                )
            );
            this.content = this.outer.find(".content");
            if (this.strategy === 'table') {
                $( this.element )
                    .find( "tr" )
                    .prepend( "<th><div class='handle'><i class='icon-move'></i></div></th>")
                    .append("<td class='last'></td>");
            } else if (this.strategy === 'list') {
                $( this.element )
                    .find( "li" )
                    .prepend( "<div class='handle'><i class='icon-move'></i></div>" );
            }
            this._addButtons();
        },

        _addButtons: function() {
            var button = jQuery("<button/>")
                .addClass("btn")
            var buttonStack = jQuery("<div/>")
                .addClass("btn-group-vertical");
            buttonStack
                .append(
                button.clone()
                    .addClass('first')
                    .html("<i class='icon-arrow-up'></i>")
                    .bind('click.orderingList', $.proxy(this._topHandler, this))
            )
                .append(
                button.clone()
                    .addClass('up')
                    .html("<i class='icon-arrow-up'></i>")
                    .bind('click.orderingList', $.proxy(this._upHandler, this))
            )
                .append(
                button.clone()
                    .addClass('down')
                    .html("<i class='icon-arrow-down'></i>")
                    .bind('click.orderingList', $.proxy(this._downHandler, this))
            )
                .append(
                button
                    .clone()
                    .addClass('last')
                    .html("<i class='icon-arrow-down'></i>")
                    .bind('click.orderingList', $.proxy(this._lastHandler, this))
            );
            this.content.append(
                jQuery('<div />').addClass('buttonColumn span2').append(buttonStack));
            this.content.find('.buttonColumn').position({of: this.content, my: "right center", at: "right center" })
        },

        /** Cleanup methods **/

        _removeDomElements: function() {
            // TODO: impl
        },

        /** Event Handlers **/

        _topHandler: function (event) {
            this.moveTop($('.ui-selected', this.element));
        },

        _upHandler: function (event) {
            this.moveUp($('.ui-selected', this.element));
        },

        _downHandler: function (event) {
            this.moveDown($('.ui-selected', this.element));
        },

        _lastHandler: function (event) {
            this.moveLast($('.ui-selected', this.element));
        }

    });

}(jQuery));