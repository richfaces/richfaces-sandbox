(function ($) {
    $('body').on("afterDomClean" + ".RICH", function (event) {
        $(event.target).remove();
    });
}(jQuery));
