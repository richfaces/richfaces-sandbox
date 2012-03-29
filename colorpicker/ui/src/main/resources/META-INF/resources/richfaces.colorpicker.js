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

    rf.ui = rf.ui || {};

    /**
     * Creates new color picker component.
     * @param componentId identifier of script component with code invoking this method
     */
    rf.ui.ColorPicker = function(componentId, options) {
        options = $.extend({okLabel:"Ok",cancelLabel:"Cancel",onchange:null}, options);
        /**
         * Small square that is used to show or hide picker.
         */
        var toggler = $(document.getElementById(componentId));
        /**
         * Hidden input that will be used to submit value to server
         */
        var valueInput = $(".rf-cp-i", toggler);
        /**
         * Container for picker plugin
         */
        var pickerContainer = $(".rf-cp-h", toggler);
        /**
         * Picker plugin will be stored with this delegate
         */
        this.delegate = $.farbtastic(pickerContainer);
        /**
         * In some places we won't be able to get to this.delegate cause this will be something different so we store it in local variable
         */
        var delegate = this.delegate;
        valueInput.change(function() {
            if (options.onchange != null) {
                options.onchange();
            }
        });
        /**
         * Setup OK button.
         */
        $("<input type='button'/>").appendTo(pickerContainer).addClass(".rf-cp-btn-o").val(options.okLabel).click(function() {
            /**
             * We update value and trigger change event if value really changed
             */
            var oldValue = valueInput.val();
            var valueDiffers = false;
            if ((oldValue != null && delegate.color != null && oldValue.toLowerCase() != delegate.color.toLowerCase()) || (oldValue == null && delegate.color
                != null || oldValue != null && delegate.color == null)) {
                valueDiffers = true;
                valueInput.val(delegate.color);
                valueInput.change();
            }
            pickerContainer.hide();
        });
        /**
         * Cancel function used to restore original color and hide the picker
         */
        var cancel = function() {
            delegate.setColor(valueInput.val());
            pickerContainer.hide();
        };
        /**
         * Setup cancel button
         */
        $("<input type='button'/>").appendTo(pickerContainer).addClass(".rf-cp-btn-c").val(options.cancelLabel).click(cancel);
        /**
         * Attach click handler to toggler. If color picker is visible then we bind event to document so that user can cancel by clicking anywhere on the screen
         */
        toggler.click(function(e) {
            e.stopPropagation();
            pickerContainer.toggle();
            if (pickerContainer.css("display") != "none") {
                var handler = function() {
                    cancel();
                    $(document).unbind('click', handler);
                };
                $(document).click(handler);
            }
        });
        /**
         * If we click on color picker then we dont want the event to be propagated to document which would invoke cancel
         */
        pickerContainer.click(function(e) {
            e.stopPropagation();
        });
        /**
         * We want to change color of toggler when user playing with colors so it can act as preview
         */
        this.delegate.linkTo(function(color) {
            toggler.css("background-color", color);
        });
        /**
         * We set initial color to the picker
         */
        this.delegate.setColor(valueInput.val());
    };
    rf.BaseComponent.extend(rf.ui.ColorPicker);

    // define super class link
    var $super = rf.ui.ColorPicker.$super;
})(jQuery, RichFaces);

