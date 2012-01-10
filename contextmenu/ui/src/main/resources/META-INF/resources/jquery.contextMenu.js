(function($) {
    $.extend($.fn, {

        contextMenu: function(o, callback) {
            // Defaults
            if (o.menu == undefined) return false;
            var jmenu = jQuery(o.menu);
            if (o.inSpeed == undefined) o.inSpeed = 150;
            if (o.outSpeed == undefined) o.outSpeed = 75;
            // 0 needs to be -1 for expected results (no fade)
            if (o.inSpeed == 0) o.inSpeed = -1;
            if (o.outSpeed == 0) o.outSpeed = -1;
            // Loop each context menu
            $(this).each(function() {
                var el = $(this);
                var offset = $(el).offset();
                // Simulate a true right click
                $(this).mousedown(function(e) {
                    var evt = e;
                    $(this).mouseup(function(e) {
                        var srcElement = $(this);
                        $(this).unbind('mouseup');
                        if (evt.button == 2) {
                            // Get this context menu
                            var menu = jmenu;

                            if ($(el).hasClass('disabled')) return false;

                            // Detect mouse position
                            var d = {}, x, y;
                            if (self.innerHeight) {
                                d.pageYOffset = self.pageYOffset;
                                d.pageXOffset = self.pageXOffset;
                                d.innerHeight = self.innerHeight;
                                d.innerWidth = self.innerWidth;
                            } else if (document.documentElement &&
                                document.documentElement.clientHeight) {
                                d.pageYOffset = document.documentElement.scrollTop;
                                d.pageXOffset = document.documentElement.scrollLeft;
                                d.innerHeight = document.documentElement.clientHeight;
                                d.innerWidth = document.documentElement.clientWidth;
                            } else if (document.body) {
                                d.pageYOffset = document.body.scrollTop;
                                d.pageXOffset = document.body.scrollLeft;
                                d.innerHeight = document.body.clientHeight;
                                d.innerWidth = document.body.clientWidth;
                            }
                            (e.pageX) ? x = e.pageX : x = e.clientX + d.scrollLeft;
                            (e.pageY) ? y = e.pageY : y = e.clientY + d.scrollTop;

                            // Show the menu
                            $(document).unbind('click');
                            $(menu).css({ top: y, left: x }).fadeIn(o.inSpeed);

                            // Hide bindings
                            setTimeout(function() { // Delay for Mozilla
                                $(document).click(function() {
                                    $(document).unbind('click').unbind('keypress');
                                    $(menu).fadeOut(o.outSpeed);
                                    return false;
                                });
                            }, 0);
                        }
                    });
                });

                // Disable text selection
                if ($.browser.mozilla) {
                    jmenu.each(function() {
                        $(this).css({ 'MozUserSelect' : 'none' });
                    });
                } else if ($.browser.msie) {
                    jmenu.each(function() {
                        $(this).bind('selectstart.disableTextSelect', function() {
                            return false;
                        });
                    });
                } else {
                    jmenu.each(function() {
                        $(this).bind('mousedown.disableTextSelect', function() {
                            return false;
                        });
                    });
                }
                // Disable browser context menu (requires both selectors to work in IE/Safari + FF/Chrome)
                $(el).add(jmenu).bind('contextmenu', function() {
                    return false;
                });

            });
            return $(this);
        }
    });
})(jQuery);