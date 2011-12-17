/*!
 jQuery-Accesskey
 Copyright (C) 2010 Denis Sokolov http://sokolov.cc

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 Known issues
 - accesskey on label adds accesskey char to the input
 */
(function($) {
    $.accesskeyHelper = function(options) {
        var defaults = {shortcutKeyCode:9,timeout:6000,hideOnAnyKey:true};
        options = $.extend(defaults, options);
        // I would use jQuery.sub() to have our own jQuery, but
        // it did not work with .extend($.expr[':']) for whatever reason.
        var $$ = {
            // Tells if the given element is focused now
            'focused': function(el) {
                // Works both with jQuery and DOM elements
                if (el.jquery && el.length) {
                    el = el[0];
                }
                return el == document.activeElement;
            },
            // Tells if you can enter characters in the field
            // Absolutely incomplete.
            // Just thinking about input[type="range"] and similar makes me shiver
            // Anyway, I stay on the safe side
            'writable': function(el) {
                el = $(el);
                var tag = el.get(0).tagName.toLowerCase();
                if (tag == 'textarea') {
                    return true;
                }
                return tag == 'input' && el.attr('type') != 'submit';

            }
        };


        // Init adds labels to the elements
        // We do not need to do this hard work if the user does not use keyboard
        // Thus we only init on first click of tab
        var init = function() {
            // Init only once
            init = null;

            $('[accesskey]').each(function() {
                var me = $(this);
                me.after('<kbd class="accesskey">' + me.attr('accesskey') + '</kbd>');
            });

            // Trigger redraw to enable transitions
            // Do not understand? Just comment this line and see the result, it's hard to explain
            $('body').width();
        };


        // Main internal controller
        var hints = (function() {
            var body = $('body');
            var shown = false;
            var timeoutId;
            var keydownHandler = function() {
                api.hide();
            };
            var api = {
                'show': function() {
                    body.addClass('accesskey-shown');
                    shown = true;
                    if (options.hideOnAnyKey) {
                        $(window).keydown(keydownHandler);
                    }
                },
                'hide': function() {
                    body.removeClass('accesskey-shown');
                    shown = false;
                    $(window).unbind("keydown", keydownHandler);
                },
                'shown': function() {
                    return shown
                }
            };
            api['preview'] = function() {
                if (shown) {
                    return true;
                }
                api.show();
                if (timeoutId != null) {
                    clearTimeout(timeoutId);
                }
                timeoutId = setTimeout(function() {
                    api.hide();
                }, options.timeout);
            };
            return api
        })();


        $(window).bind('keydown.jquery-accesskey', function(e) {
            if (e.which == 27 && $$.writable(document.activeElement)) {
                // Esc to friendly blur
                if (init) {
                    init();
                }
                $(document.activeElement).blur();
                hints.preview();
            }
            if (e.which == options.shortcutKeyCode) {
                // Tab button trigger
                // We show hints for 6 seconds when somebody presses the Tab key
                if (init) {
                    init();
                }
                hints.preview();
            }
        });

        // Working with DOM
        var elements = {
            'getByKey': function(sym) {
                sym = sym.toLowerCase();
                try {
                    var els = $('[accesskey="' + sym + '"]');
                } catch (err) {
                    return false;
                }
                if (els.length == 0) {
                    return false;
                }
                return els;
            },
            'getNextTarget': function(els) {
                // If only one element, choose itself
                if (els.length == 1) {
                    return els.eq(0);
                }

                // Find a focused element
                var f = els.filter(function() {
                    return $$.focused(this)
                });
                if (f.length == 0 || $$.focused(els.eq(-1)))
                // Focus could be somewhere else, then just focus the first one
                {
                    return els.eq(0);
                } else
                // Move on to the next element after the focused
                {
                    return els.eq(els.index(f) + 1);
                }
            }
        };

        // Shortcut keys
        $(window).bind('keypress.jquery-accesskey', function(e) {
            // If we are in the field, just let the user type. :)
            if ($$.writable(e.target)) {
                return true;
            }

            // Find elements with this accesskey
            var els = elements.getByKey(String.fromCharCode(e.which));
            if (!els) {
                return true;
            }

            var target = elements.getNextTarget(els).focus();
            // Prevent the letter from being put into the field
            if ($$.writable(target)) {
                return false;
            }
        });
    }
})(jQuery);