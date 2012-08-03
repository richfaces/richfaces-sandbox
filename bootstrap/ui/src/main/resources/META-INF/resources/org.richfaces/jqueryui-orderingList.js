(function ($) {

    $.widget('rf.orderingList', {

        options: {
            cssClass: '',
            disabled: false,
            headerText: ''
        },

        _create: function() {
            var self = this;
            this.selectableOptions = {};
            this.sortableOptions = { handle: ".handle",
                start: function(event, ui) {
                    $(self.element).find(".ui-selected").removeClass('ui-selected');
                    $(ui.item).addClass('ui-selected');
                },
                stop: function(event, ui) {
                    var ui = self._dumpState();
                    ui.movement = 'drag';
                    self._trigger("orderChanged", event, ui);
                    }
                };
            if ($(this.element).is("table")) {
                this.strategy = "table";
                var $pluginRoot = $( this.element).find("tbody");
                this.selectableOptions.filter = "tr";
                this.sortableOptions.placeholder = "placeholder";
                this.sortableOptions.helper = $.proxy(this._rowHelper, this);
            } else {
                this.strategy = "list";
                var $pluginRoot = $( this.element);
                this.selectableOptions.filter = "li";
            }
            this._addDomElements();
            this.widgetEventPrefix = "orderingList_";
            $pluginRoot
                .sortable(this.sortableOptions)
                .selectable(this.selectableOptions);
        },

        _rowHelper: function(e, tr) {
            var $helper = tr.clone();
            $helper.addClass("ui-selected rowhelper");
            // we lose the cell width in the clone, so we re-set it here:
            $helper.css('width', tr.css('width'));
            $helper.children().each(function (index) {
                var original_cell = tr.children().get(index);
                var original_width = $(original_cell).css('width');
                $(this).css('width', original_width);
            });
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
            var self = this;
            this._removeDomElements();
            $(this.element).children().each(function() {
                self.unSelectItem(this);
            });
        },

        moveTop: function (items, event) {
            var first = items.prevAll().not('.ui-selected').last();
            $(items).insertBefore(first);
            var ui = this._dumpState();
            ui.movement = 'moveTop';
            this._trigger("orderChanged", event, ui);
        },

        moveUp: function (items, event) {
            $(items).each( function () {
                var $item = $(this);
                var prev = $item.prevAll().not('.ui-selected').first();
                if (prev.length > 0) {
                    $item.insertBefore(prev);
                }
            });
            var ui = this._dumpState();
            ui.movement = 'moveUp';
            this._trigger("orderChanged", event, ui);
        },

        moveDown: function (items, event) {
            $(items).sort(function() {return 1}).each( function () {
                var $item = $(this);
                var next = $item.nextAll().not('.ui-selected').first();
                if (next.length > 0) {
                    $item.insertAfter(next);
                }
            });
            var ui = this._dumpState();
            ui.movement = 'moveDown';
            this._trigger("orderChanged", event, ui);
        },

        moveLast: function (items, event) {
            var last = items.nextAll().not('.ui-selected').last();
            $(items).insertAfter(last);
            var ui = this._dumpState();
            ui.movement = 'moveLast';
            this._trigger("orderChanged", event, ui);
        },

        getOrderedElements: function () {
            return $(this.element).find('.ui-selectee');
        },

        getOrderedKeys: function () {
            var keys = new Array();
            this.getOrderedElements().each( function() {
                var $this = $(this);
                var jbossKey = $this.data('jbossKey');
                var key = (jbossKey) ? jbossKey : $this.text();
                keys.push(key);
            })
            return keys;
        },

        /** Initialisation methods **/

        _addDomElements: function() {
            $(this.element).addClass("list").wrap(
                $("<div />").addClass('orderingList container-fluid with-handle').addClass(this.options.cssClass).append(
                    $('<div />').addClass('content row-fluid').append(
                        $('<div />').addClass('span10')
                    )
                )
            );
            this.outer = $(this.element).parents(".orderingList").first();
            this.outer.prepend(
                $("<div />").addClass("row-fluid").append(
                    $("<div />").addClass('span12 header').append(
                        $("<h3/>").html(this.options.headerText)
                    )
                )
            );
            this.content = this.outer.find(".content");
            if (this.strategy === 'table') {
                $( this.element )
                    .find( "tbody > tr" )
                    .prepend( "<th class='handleRow'><div class='handle'><i class='icon-move'></i></div></th>");
                $( this.element )
                    .find("thead > tr")
                    .prepend( "<th class='handleRow'></th>");
                $( this.element )
                    .find( "tr").each(function() {
                        $(this).children().last().addClass('last');
                        $(this).children().first().addClass('first');
                    })
            } else if (this.strategy === 'list') {
                $( this.element )
                    .find( "li" )
                    .prepend( "<div class='handle'><i class='icon-move'></i></div>" );
            }
            this._addButtons();
        },

        _addButtons: function() {
            var button = $("<button/>")
                .attr('type', 'button')
                .addClass("btn")
            var buttonStack = $("<div/>")
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
                $('<div />').addClass('buttonColumn span2').append(buttonStack));
            this.content.find('.buttonColumn').position({of: this.content, my: "right center", at: "right center" })
        },

        _dumpState: function() {
            var ui = {};
            ui.orderedElements = this.getOrderedElements();
            ui.orderedKeys = this.getOrderedKeys();
            return ui;
        },

        /** Cleanup methods **/

        _removeDomElements: function() {
            // TODO: impl
        },

        /** Event Handlers **/

        _topHandler: function (event) {
            this.moveTop($('.ui-selected', this.element), event);
        },

        _upHandler: function (event) {
            this.moveUp($('.ui-selected', this.element), event);
        },

        _downHandler: function (event) {
            this.moveDown($('.ui-selected', this.element), event);
        },

        _lastHandler: function (event) {
            this.moveLast($('.ui-selected', this.element), event);
        }

    });

}(jQuery));