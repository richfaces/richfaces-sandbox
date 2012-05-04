(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.AccordionGroup = function (id, options) {
        options = $.extend({}, $.fn.collapse.defaults, options)
        $super.constructor.call(this, id, options);
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.element = this.attachToDom();
        this.body = $('.accordion-body', this.element);

        if (options['onhide'] && typeof options['onhide'] == 'function') {
            rf.Event.bind(this.body, "hide" + this.namespace, options['onhide']);
        }
        if (options['onshow'] && typeof options['onshow'] == 'function') {
            rf.Event.bind(this.body, "show" + this.namespace, options['onshow']);
        }
    };

    rf.BaseComponent.extend(rf.ui.AccordionGroup);
    var $super = rf.ui.AccordionGroup.$super;

    $.extend(rf.ui.AccordionGroup.prototype, (function () {

        return {
            name:"accordion" ,

            // place shared prototype attributes and methods here.  Prepend "__" ahead of private methods

            destroy: function() {
                rf.Event.unbind(this.body, this.namespace);
                $super.destroy.call(this);
            }
        }
    })());

    /* AccordionGroup PLUGIN DEFINITION
     * ============================== */

    $.fn.accordionGroup = function ( options ) {
        return this.each(function () {
            var $this = $(this);
            var data = $this.data('accordion');
            if (!data) $this.data('accordion', (data = new rf.ui.AccordionGroup(this.id, options)))
        })
    }

    $.fn.accordionGroup.defaults = {
    }

    /* AccordionGroup DATA-API
     * ==================== */

    $(function () {
        $('.accordion-group').each(function () {
            var $accordionGroup = $(this);
            var options = $accordionGroup.data('richfaces');
            $accordionGroup.accordionGroup(options);
        });
    });

})(jQuery, window.RichFaces);
