(function ($) {
    $('body').on("afterDomClean" + ".RICH", function (event) {
        $(event.target).find("[data-jboss-cleanup='true']").triggerHandler('cleanup.RICH');
    });
}(jQuery));
