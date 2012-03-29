/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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

(function ($, rf) {


    var propertyTranslation = {
        'copyToClipboardConfirmationLabel':'copyToClipboardConfirmation',
        'copyToClipboardLabel':'copyToClipboard',
        'expandSourceLabel':'expandSource',
        'helpLabel':'help',
        'printLabel':'print',
        'viewSourceLabel':'viewSource',
        'collapsed':'collapse',
        'language':'brush',
        'autoLinks':'auto-links',
        'firstLine':'first-line',
        'htmlScript':'html-script',
        'tabSize':'tab-size',
        'smartTabs':'smart-tabs'
    };

    var translateProperties = function(target, source, translation) {
        for (var attr in source) {
            var targetAttr = translation[attr] != null ? translation[attr] : attr;
            target[targetAttr] = source[attr];
            if (!(target[targetAttr] instanceof Function) && !(target[targetAttr] instanceof Array) && target[targetAttr] instanceof Object) {
                target[targetAttr] = translateProperties({}, target[targetAttr], translation);
            }
        }
        return target;
    };

    rf.ui = rf.ui || {};

    /**
     * Creates new color picker component.
     * @param componentId identifier of script component with code invoking this method
     */
    rf.ui.SyntaxHighlighter = function(componentId, options) {
        var strings = options.strings || {};
        for (var key in options.strings) {
            SyntaxHighlighter.config.strings[propertyTranslation[key]] = options.strings[key];
        }
        jQuery(function() {
            SyntaxHighlighter.highlight(translateProperties({}, options, propertyTranslation), jQuery(".rf-syn-cd", document.getElementById(componentId))[0]);
        });
    };
    rf.BaseComponent.extend(rf.ui.SyntaxHighlighter);

    // define super class link
    var $super = rf.ui.SyntaxHighlighter.$super;
})(jQuery, RichFaces);

