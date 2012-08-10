(function ($) {
    $('body').on("afterDomClean" + ".RICH", function (event) {
        var ui = {target: event.target};
        $('body').trigger('cleanDom.bootstrap.RICH', ui);
    });
}(jQuery));
