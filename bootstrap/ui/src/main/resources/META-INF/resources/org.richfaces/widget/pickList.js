(function ($) {

    $.widget('rf.pickList', {

        options: {
            disabled: false,
            header: ''
        },

        _create: function() {
            this.source = this.element.find(".source");
            this.target = this.element.find(".target")
            this._addDomElements();
            this.source.orderingList({
                showButtons: false
            });
            this.target.orderingList();
            this.source.sortable("option", "connectWith", this.target);
            this.target.sortable("option", "connectWith", this.source);
            this._repositionButtons();
        },

        /** Public API methods **/

        moveLeft: function (items, event) {
            if (this.options.disabled) return;
            items.detach();
            this.source.prepend(items);
//            var ui = this._dumpState();
//            ui.change = 'remove';
//            this._trigger("targetChanged", event, ui);
        },

        moveRight: function (items, event) {
            if (this.options.disabled) return;
            items.detach();
            this.target.prepend(items);
//            var ui = this._dumpState();
//            ui.change = 'remove';
//            this._trigger("targetChanged", event, ui);
        },


        /** Initialisation methods **/

        _addDomElements: function() {
            this._addParents();
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
                    .html("<i class='icon-arrow-left'></i>")
                    .bind('click.orderingList', $.proxy(this._leftAllHandler, this))
            )
                .append(
                button.clone()
                    .addClass('up')
                    .html("<i class='icon-arrow-left'></i>")
                    .bind('click.orderingList', $.proxy(this._leftHandler, this))
            )
                .append(
                button.clone()
                    .addClass('down')
                    .html("<i class='icon-arrow-right'></i>")
                    .bind('click.orderingList', $.proxy(this._rightHandler, this))
            )
                .append(
                button
                    .clone()
                    .addClass('last')
                    .html("<i class='icon-arrow-right'></i>")
                    .bind('click.orderingList', $.proxy(this._rightAllHandler, this))
            );
            this.source.parent().after(
                $('<div />').addClass('middle buttonColumnPickList span1').append(buttonStack));
        },

        _repositionButtons: function() {
            var buttonGroup = this.outer.find('.buttonColumnPickList > .btn-group-vertical');
            buttonGroup.position({of: this.outer.find('.right > .orderingList > .content'), my: "right center", at: "left center", using: function(position) {
                buttonGroup.css('top', position.top);
            } })
        },

        _addParents: function() {
            this.element.addClass("row-fluid").wrap(
                $("<div />").addClass('pickList outer container-fluid')
            );
            this.outer = this.element.parents(".outer").first();
            this.outer.prepend(
                $("<div />").addClass("row-fluid").append(
                    $("<div />").addClass('span12 header').append(
                        $("<h3/>").html(this.options.header)
                    )
                )
            );
            this.source.wrap(
                $("<div />").addClass('left span5')
            )
            this.target.wrap(
                $("<div />").addClass('right span6')
            )
            this.content = this.element;

        },

        /** Event Handlers **/

        _leftAllHandler: function (event) {
            var items = $('.ui-selectee', this.target);
            this.moveLeft(items, event);
            this.source.orderingList('selectItem', items);
        },

        _leftHandler: function (event) {
            this.moveLeft($('.ui-selected', this.target), event);
        },

        _rightHandler: function (event) {
            this.moveRight($('.ui-selected', this.source), event);
        },

        _rightAllHandler: function (event) {
            var items = $('.ui-selectee', this.source);
            this.moveRight(items, event);
            this.target.orderingList('selectItem', items);
        }

});

}(jQuery));