(function ($) {

    $.widget('rf.orderingListCommon', {

        options: {
            disabled: false,
            cssClass: '',
            headerText: ''
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
            $('li', this.element).each(function() {
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
            $(this.element).addClass("list selectable").wrap(
                "<div class='orderingList " + this.options.cssClass + "'><div class='group row-fluid'></div></div>"
            )
            this.container = $(this.element).parents(".orderingList").first();
            this.container.prepend(jQuery("<div class='header'></div>").html("<h3>" + this.options.headerText + "</h3>"));
            this.group = this.container.find(".group");
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
            this.group.append(buttonStack);
            this.group.find('.btn-group-vertical').position({of: this.group, my: "right center", at: "right center", offset: "-10 0" })
        },

        /** Cleanup methods **/

        _removeDomElements: function() {
            // TODO: impl
        },

        /** Event Handlers **/

        _topHandler: function (event) {
            this.moveTop($('li.ui-selected', this.element));
        },

        _upHandler: function (event) {
            this.moveUp($('li.ui-selected', this.element));
        },

        _downHandler: function (event) {
            this.moveDown($('li.ui-selected', this.element));
        },

        _lastHandler: function (event) {
            this.moveLast($('li.ui-selected', this.element));
        }

    });

}(jQuery));