(function ($) {

    $.widget('rf.pickList', {

        options: {
            disabled: false,
            header: ''
        },

        _create: function() {
            this.widgetEventPrefix = "picklist_";
            this.sourceList = this.element.find(".source");
            this.targetList = this.element.find(".target");
            this._addDomElements();
            this.sourceList.orderingList({
                showButtons: false,
                widgetEventPrefix: 'sourceList_'
            });
            this.targetList.orderingList({
                widgetEventPrefix: 'targetList_'
            });
            this.sourceList.sortable("option", "connectWith", this.targetList);
            this.targetList.sortable("option", "connectWith", this.sourceList);
            this._registerListeners();
        },

        destroy: function() {
            $.Widget.prototype.destroy.call( this );
            this._removeDomElements();
            this.sourceList.orderingList("destroy");
            this.targetList.orderingList("destroy");
            return this;
        },

        /** Public API methods **/

        moveLeft: function (items, event) {
            if (this.options.disabled) return;
            items.detach();
            this.sourceList.prepend(items);
            var ui = this._dumpState();
            ui.change = 'remove';
            this._trigger("change", event, ui);
        },

        moveRight: function (items, event) {
            if (this.options.disabled) return;
            items.detach();
            this.targetList.prepend(items);
            var ui = this._dumpState();
            ui.change = 'add';
            this._trigger("change", event, ui);
        },


        /** Initialisation methods **/

        _addDomElements: function() {
            this._addParents();
            this._addButtons();
        },

        _addButtons: function() {
            var button = $('<button type="button" class="btn" />');
            var buttonStack = $("<div/>")
                .addClass("btn-group-vertical");
            buttonStack
                .append(
                button.clone()
                    .addClass('leftAll')
                    .html("<i class='icon-fast-backward'></i>")
                    .bind('click.orderingList', $.proxy(this._leftAllHandler, this))
            )
                .append(
                button.clone()
                    .addClass('left')
                    .html("<i class='icon-step-backward'></i>")
                    .bind('click.orderingList', $.proxy(this._leftHandler, this))
            )
                .append(
                button.clone()
                    .addClass('right')
                    .html("<i class='icon-step-forward'></i>")
                    .bind('click.orderingList', $.proxy(this._rightHandler, this))
            )
                .append(
                button
                    .clone()
                    .addClass('rightAll')
                    .html("<i class='icon-fast-forward'></i>")
                    .bind('click.orderingList', $.proxy(this._rightAllHandler, this))
            );
            this.sourceList.parent().after(
                $('<div />').addClass('middle buttonColumnPickList span1').append(buttonStack));
        },

        _addParents: function() {
            this.element.addClass("row-fluid").wrap(
                $("<div />").addClass('pickList outer')
            );
            this.outer = this.element.parents(".outer").first();
            if (this.options.header) {
                this.outer.prepend(
                    $("<div />").addClass("row-fluid").append(
                        $("<div />").addClass('span12 header').append(
                            $("<h3/>").html(this.options.header)
                        )
                    )
                );
            }
            this.sourceList.wrap(
                $("<div />").addClass('left span5')
            )
            this.targetList.wrap(
                $("<div />").addClass('right span6')
            )
            this.content = this.element;

        },

        _registerListeners: function() {
            var self = this;
            // the widget factory converts all events to lower case
            this.sourceList.bind('sourcelist_receive', function(event, ui) {
                var new_ui = self._dumpState();
                new_ui.change = 'remove';
                new_ui.originalEvent = event;
                self._trigger("change", event, new_ui);
            });
            this.targetList.bind('targetlist_receive', function(event, ui) {
                var new_ui = self._dumpState();
                new_ui.change = 'add';
                new_ui.originalEvent = event;
                self._trigger("change", event, new_ui);
            });
            this.targetList.bind('targetlist_change', function(event, ui) {
                var new_ui = self._dumpState();
                new_ui.change = 'sort';
                new_ui.originalEvent = event;
                self._trigger("change", event, new_ui);
            });
            // Bind the client-provided change listeners
            if (this.options.onchange && typeof this.options.onchange == 'function') {
                this.component.bind('change.pickList.bootstrap.RICH', this.options.onchange);
            }
        },

        _dumpState: function() {
            var ui = {};
            ui.pickedElements = this.targetList.orderingList("getOrderedElements");
            ui.pickedKeys = this.targetList.orderingList("getOrderedKeys");
            return ui;
        },

        /** Cleanup methods **/

        _removeDomElements: function() {
            var list = this.element.detach();
            this.outer.replaceWith(list);
            this.element.removeClass("row-fluid");
            this.sourceList.parents('.left').first().replaceWith(this.sourceList.detach());
            this.targetList.parents('.right').first().replaceWith(this.targetList.detach());
        },

            /** Event Handlers **/

        _leftAllHandler: function (event) {
            var items = $('.ui-selectee', this.targetList);
            this.moveLeft(items, event);
            this.sourceList.orderingList('selectItem', items);
        },

        _leftHandler: function (event) {
            this.moveLeft($('.ui-selected', this.targetList), event);
        },

        _rightHandler: function (event) {
            this.moveRight($('.ui-selected', this.sourceList), event);
        },

        _rightAllHandler: function (event) {
            var items = $('.ui-selectee', this.sourceList);
            this.moveRight(items, event);
            this.targetList.orderingList('selectItem', items);
        }

});

}(jQuery));