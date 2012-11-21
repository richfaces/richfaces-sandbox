(function ($) {

    $.widget('rf.orderingList', {

        options: {
            disabled: false,
            header: '',
            showButtons: true,
            mouseOrderable: true,
            widgetEventPrefix: 'orderingList_',
            dropOnEmpty: true,
            dragSelect: false
        },

        _create: function() {
            var self = this;
            this.selectableOptions = {
                disabled: self.options.disabled
            };
            this.sortableOptions = { handle: this.options.dragSelect ? ".handle" : false,
                disabled: this.options.disabled,
                dropOnEmpty: this.options.dropOnEmpty,
                placeholder: "placeholder",
                tolerance: "pointer",
                start: function(event, ui) {
                    self.currentItems = ui.item.parent().children('.ui-selected').not('.placeholder').not('.helper-item');
                    var helper = ui.helper;
                    var placeholder = self.element.find('.placeholder')
                    placeholder.css('height', helper.css('height'));
                    self.currentItems.not(ui.item).hide();
                },
                sort: function (event, ui) {
                    var that = $(this);
                    var helper_top = ui.helper.position().top,
                        helper_bottom = helper_top + ui.helper.outerHeight();
                    that.children('.ui-selectee').not('.placeholder').not('.helper-item').not('.ui-selected').each(function () {
                        var item = $(this);
                        var item_top = item.position().top;
                        var item_middle = item.position().top + item.outerHeight()/2;
                        /* if the helper overlaps half of an item, move the placeholder */
                        if (helper_top < item_middle && item_middle < helper_bottom) {
                            if (item_top > helper_top) {
                                $('.placeholder', that).insertAfter(item);
                            } else {
                                $('.placeholder', that).insertBefore(item);
                            }
                            return false;
                        }
                    });
                },
                cancel: function(event, ui) {
                    self.currentItems.show();
                },
                receive: function(event, ui) {
                    ui.item.after(ui.sender.find(".ui-selected"));
                    var new_ui = self._dumpState();
                    new_ui.originalEvent = event;
                    self._trigger("receive", event, new_ui);
                },
                beforeStop: function(event, ui) {
                },
                stop: function(event, ui) {
                    var first = self.currentItems.first();
                    if (first.get(0) !== ui.item.get(0)) {
                        ui.item.before(first);
                        first.after(self.currentItems.not(first).detach());
                    } else {
                        ui.item.after(self.currentItems.not(ui.item).detach());
                    }
                    self.currentItems.not('.placeholder').show();
                    var ui = self._dumpState();
                    ui.movement = 'drag';
                    self._trigger("change", event, ui);
                    }
                };
            if (this.element.is("table")) {
                this.strategy = "table";
                this.$pluginRoot = $( this.element).find("tbody");
                this.selectableOptions.filter = "tr";
                this.sortableOptions.helper = $.proxy(this._rowHelper, this);
            } else {
                this.strategy = "list";
                this.$pluginRoot = $( this.element);
                this.selectableOptions.filter = "li";
                this.sortableOptions.helper = $.proxy(this._listHelper, this);
            }
            this._addDomElements();
            this.widgetEventPrefix = this.options.widgetEventPrefix;
            if (this.options.mouseOrderable === true) {
                this.$pluginRoot.sortable(this.sortableOptions);
            }
            this.$pluginRoot.selectable(this.selectableOptions);
            if (this.options.disabled === true) {
                self._disable();
            }
            var selector = '.handle';
            if (this.options.dragSelect == false) {
                this.element.on("mousedown", '.ui-selectee', function(event) {
                    var item = $(this);
                    var list = item.parents('.list').first();
                    list.data('orderingList').mouseStarted = true;
                });
                this.$pluginRoot.on("mousemove", '.ui-selectee', function(event) {
                    var item = $(this);
                    var list = item.parents('.list').first();
                    var orderingList = list.data('orderingList');
                    if (orderingList.mouseStarted) {
                        orderingList.mouseStarted = false;
                        if (! item.hasClass('ui-selected')) {
                            var selectable = orderingList.$pluginRoot.data('selectable');
                            selectable._mouseStart(event);
                            selectable._mouseStop(event);
                        }
                    }
                });
                this.element.on("mouseup", '.ui-selectee', function(event) {
                    var item = $(this);
                    var list = item.parents('.list').first();
                    var orderingList = list.data('orderingList');
                    if (orderingList.mouseStarted) {
                        orderingList.mouseStarted = false;
                        var selectable = orderingList.$pluginRoot.data('selectable');
                        selectable._mouseStart(event);
                        selectable._mouseStop(event);
                    }
                });
            } else {
                this.element.find('.handle').on("mousedown", function(event) {
                    var item = $(this).parents('.ui-selectee').first();
                    if (! item.hasClass('ui-selected')) {
                        var list = item.parents('.list').first();
                        var selectable = orderingList.$pluginRoot.data('selectable');
                        list.data('selectable')._mouseStart(event);
                        list.data('selectable')._mouseStop(event);
                    }
                });
            }
        },

        destroy: function() {
            $.Widget.prototype.destroy.call( this );
            this._removeDomElements();
            this.$pluginRoot
                .sortable("destroy")
                .selectable("destroy");
            return this;
        },

        _listHelper: function(e, item) {
            var $helper = $("<ol />").addClass('helper').css('height', 'auto').css('width', this.element.css('width'));
            item.parent().children('.ui-selected').not('.ui-sortable-placeholder').clone().addClass("helper-item").show().appendTo($helper);
            return $helper;
        },

        _rowHelper: function(e, item) {
            var $helper = $("<tbody />").addClass('helper').css('height', 'auto');
            item.parent().children('.ui-selected').not('.ui-sortable-placeholder').clone().addClass("helper-item").show().appendTo($helper);
            /* we lose the cell width in the clone, so we re-set it here: */
            var firstRow = $helper.children("tr").first();  /* we only need to set the column widths on the first row */
            firstRow.children().each(function (colindex) {
                var original_cell = item.children().get(colindex);
                var original_width = $(original_cell).css('width');
                $(this).css('width', original_width);
            });
            return $helper;
        },

        _setOption: function(key, value) {
            var self = this;
            if (this.options.key === value) {
                return;
            }
            switch (key) {
                case "disabled":
                    if (value === true) {
                        self._disable();
                    } else {
                        self._enable();
                    }
                    break;
            }
            $.Widget.prototype._setOption.apply(self, arguments);
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
            this.element.children().each(function() {
                self.unSelectItem(this);
            });
        },

        moveTop: function (items, event) {
            if (this.options.disabled) return;
            var first = items.prevAll().not('.ui-selected').last();
            $(items).insertBefore(first);
            var ui = this._dumpState();
            ui.movement = 'moveTop';
            this._trigger("change", event, ui);
        },

        moveUp: function (items, event) {
            if (this.options.disabled) return;
            $(items).each( function () {
                var $item = $(this);
                var prev = $item.prevAll().not('.ui-selected').first();
                if (prev.length > 0) {
                    $item.insertBefore(prev);
                }
            });
            var ui = this._dumpState();
            ui.movement = 'moveUp';
            this._trigger("change", event, ui);
        },

        moveDown: function (items, event) {
            if (this.options.disabled) return;
            $(items).sort(function() {return 1}).each( function () {
                var $item = $(this);
                var next = $item.nextAll().not('.ui-selected').first();
                if (next.length > 0) {
                    $item.insertAfter(next);
                }
            });
            var ui = this._dumpState();
            ui.movement = 'moveDown';
            this._trigger("change", event, ui);
        },

        moveLast: function (items, event) {
            if (this.options.disabled) return;
            var last = items.nextAll().not('.ui-selected').last();
            $(items).insertAfter(last);
            var ui = this._dumpState();
            ui.movement = 'moveLast';
            this._trigger("change", event, ui);
        },

        getOrderedElements: function () {
            return this.element.find('.ui-selectee');
        },

        getOrderedKeys: function () {
            var keys = new Array();
            this.getOrderedElements().each( function() {
                var $this = $(this);
                var data_key = $this.data('key');
                var key = (data_key) ? data_key : $this.text();
                keys.push(key);
            })
            return keys;
        },

        /** Initialisation methods **/

        _addDomElements: function() {
            this._addParents();
            this._addMouseHandles();
            if (this.options.showButtons === true) {
                this._addButtons();
            }
            if (this.strategy === 'table') { /* round the table row corners */
                $( this.element )
                    .find( "tr").each(function() {
                        $(this).children().last().addClass('last');
                        $(this).children().first().addClass('first');
                    })
            }
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
                $('<div />').addClass('buttonColumn').append(buttonStack));
        },

        _addMouseHandles: function () {
            if (this.options.mouseOrderable !== true) {
                return
            }
            if (this.options.dragSelect === true) {
                this.content.addClass('with-handle');
                if (this.strategy === 'table') {
                    $( this.element )
                        .find( "tbody > tr" )
                        .prepend( "<th class='handleCell'><div class='handle'><i class='icon-move'></i></div></th>");
                    $( this.element )
                        .find("thead > tr")
                        .prepend( "<th class='handleCell'></th>");
                } else if (this.strategy === 'list') {
                    $( this.element )
                        .find( "li" )
                        .prepend( "<div class='handle'><i class='icon-move'></i></div>" );
                }
            } else {
                if (this.strategy === 'table') {
                    /* This empty cell is required to get the helper positioned correctly */
                    $( this.element )
                        .find( "tbody > tr" )
                        .prepend( "<th class='emptyCell'></th>");
                    $( this.element )
                        .find("thead > tr")
                        .prepend( "<th class='emptyCell'></th>");
                }
            }
        },

        _addParents: function() {
            this.element.addClass('list').wrap(
                $("<div />").addClass('orderingList outer').append(
                    $('<div />').addClass('content').append(
                        $('<div />').addClass('listBox')
                    )
                )
            );
            this.outer = this.element.parents(".outer").first();
            var header = $("<div />").addClass('header');
            if (this.options.header) {
                header.append($("<h3/>").html(this.options.header));
            }
            this.outer.prepend(header);
            this.content = this.outer.find(".content");
        },

        _disable: function() {
            this.$pluginRoot
                .sortable("option", "disabled", true)
                .selectable("option", "disabled", true);
            this.element
                .addClass('disabled')
                .find(".ui-selected").removeClass('ui-selected');
            $('.buttonColumn', this.content).find("button").attr("disabled", true);
        },

        _enable: function() {
            this.$pluginRoot
                .sortable("option", "disabled", false)
                .selectable("option", "disabled", false);
            this.element.removeClass('disabled');
            $('.buttonColumn', this.content).find("button").attr("disabled", false);
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
            var list = this.element.detach();
            this.outer.replaceWith(list);
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