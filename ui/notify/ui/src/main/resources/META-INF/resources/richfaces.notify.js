/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

window.RichFaces = window.RichFaces || {};
RichFaces.NotifyStack = (function() {
    var stacks = {};
    return {
        register: function(id, stack) {
            var existingStack = stacks[id];
            if (existingStack != null) {
                stack = jQuery.extend(existingStack, stack);
                delete stack.addpos1;
                delete stack.addpos2;
                delete stack.animation;
                delete stack.firstpos1;
                delete stack.firstpos2;
                delete stack.nextpos1;
                delete stack.nextpos2;
            }
            stack.id = id;
            stacks[id] = stack;
        },
        getStack: function(id) {
            var stack = stacks[id];
            if (stack == null) {
                stack = jQuery.extend({}, jQuery.pnotify.defaults.pnotify_stack);
                this.register(id, stack);
            }
            return stack;
        }
    }
})();

RichFaces.Notify = function(options) {
    /**
     * Copies attributes from one objects to other object, but
     * can change the name of target attributes.
     */
    function extend(target, source, translation) {
        for (var attr in source) {
            var targetAttr = translation[attr] != null ? translation[attr] : attr;
            target[targetAttr] = source[attr];
            if (attr != 'stack' && target[targetAttr] instanceof Object) {
                target[targetAttr] = extend({}, target[targetAttr], translation);
            }
        }
        return target;
    }

    options = jQuery.extend({stack:'default'}, options);
    if (options != null && typeof options.stack == "string") {
        options.stack = RichFaces.NotifyStack.getStack(options.stack);
    }
    var delegateOptions = extend({}, options, {
        'title':'pnotify_title' ,
        'text': 'pnotify_text',
        'styleClass': 'pnotify_addclass',
        'nonblocking': 'pnotify_nonblock',
        'nonblockingOpacity': 'pnotify_nonblock_opacity',
        'showHistory': 'pnotify_history',
        'animation': 'pnotify_animation',
        'appearAnimation': 'effect_in',
        'hideAnimation': 'effect_out',
        'animationSpeed': 'pnotify_animate_speed',
        'opacity': 'pnotify_opacity',
        'showShadow': 'pnotify_shadow',
        'showCloseButton': 'pnotify_closer',
        'sticky': 'pnotify_hide',
        'stayTime': 'pnotify_delay',
        'stack': 'pnotify_stack'
    });
    if (options.sticky !== null) {
        delegateOptions.pnotify_hide = !options.sticky;
    }
    jQuery(document).ready(function() {
        if (options.delay) {
            setTimeout(function() {
                jQuery.pnotify(delegateOptions);
            }, options.delay);
        } else {
            jQuery.pnotify(delegateOptions);
        }
    });
};

//TODO remove this fix when it gets in to jquery.js
(function() {
    var safariCompatMode;
    var getCompatMode = function() {
        var compatMode = document.compatMode;
        if (!compatMode && jQuery.browser.safari) {
            if (!safariCompatMode) {
                //detect compatMode as described in http://code.google.com/p/doctype/wiki/ArticleCompatMode
                var width = jQuery(document.createElement("div")).attr('style', 'position:absolute;width:0;height:0;width:1')
                        .css('width');
                safariCompatMode = compatMode = (width == '1px' ? 'BackCompat' : 'CSS1Compat');
            } else {
                compatMode = safariCompatMode;
            }
        }

        return compatMode;
    };


    // Create innerHeight, innerWidth, outerHeight and outerWidth methods
    jQuery.each([ "Height", "Width" ], function(i, name) {

        var tl = i ? "Left" : "Top",  // top or left
                br = i ? "Right" : "Bottom", // bottom or right
                lower = name.toLowerCase();

        // innerHeight and innerWidth
        jQuery.fn["inner" + name] = function() {
            return this[0] ?
                    jQuery.css(this[0], lower, false, "padding") :
                    null;
        };

        // outerHeight and outerWidth
        jQuery.fn["outer" + name] = function(margin) {
            return this[0] ?
                    jQuery.css(this[0], lower, false, margin ? "margin" : "border") :
                    null;
        };

        var type = name.toLowerCase();

        jQuery.fn[ type ] = function(size) {
            // Get window width or height
            return this[0] == window ?
                // Everyone else use document.documentElement or document.body depending on Quirks vs Standards mode
                    getCompatMode() == "CSS1Compat" && document.documentElement[ "client" + name ] ||
                            document.body[ "client" + name ] :

                // Get document width or height
                    this[0] == document ?
                        // Either scroll[Width/Height] or offset[Width/Height], whichever is greater
                            Math.max(
                                    document.documentElement["client" + name],
                                    document.body["scroll" + name], document.documentElement["scroll" + name],
                                    document.body["offset" + name], document.documentElement["offset" + name]
                                    ) :

                        // Get or set width or height on the element
                            size === undefined ?
                                // Get width or height on the element
                                    (this.length ? jQuery.css(this[0], type) : null) :

                                // Set the width or height on the element (default to pixels if value is unitless)
                                    this.css(type, typeof size === "string" ? size : size + "px");
        };

    });
}());