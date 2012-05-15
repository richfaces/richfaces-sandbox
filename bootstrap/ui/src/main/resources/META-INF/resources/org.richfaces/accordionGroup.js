(function($, rf, log) {

    $(document).ready(function() {
        // clean the DOM (see richfaces.js: cleanDom)
        $('body').on('cleanDom.richfaces', function(event, element) {
            $('.rf-bs-event-target', element).each(function() {
                $(this).off(".rfBsEvent");
            });
        });
    });

})(jQuery, window.RichFaces, window.RichFaces.log);
