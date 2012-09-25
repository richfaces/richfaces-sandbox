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

    function evaluate(o) {
        if (o instanceof Function) {
            return o();
        } else {
            var value;
            with (object) {
                value = eval(o);
            }
            return value;
        }
    }

    /**
     * Creates new color picker component.
     * @param componentId identifier of script component with code invoking this method
     */
    rf.ui.ColorPicker = rf.BaseComponent.extendClass({
        name:"ColorPicker",
        init:function (componentId, options) {
            $super.constructor.call(this, componentId);
            this.attachToDom(componentId);
            options = $.extend({okLabel:"Ok", cancelLabel:"Cancel", onchange:null, onshow:null, onhide:null}, options);
            this.options = options;
            /**
             * Small square that is used to show or hide picker.
             */
            var toggler = $(document.getElementById(componentId));
            /**
             * Hidden input that will be used to submit value to server
             */
            var valueInput = $(".rf-cpi-i", toggler);
            /**
             * Container for picker plugin
             */
            var pickerContainer = $(".rf-cpi-h", toggler);
            /**
             * Picker plugin will be stored with this delegate
             */
            this.delegate = $.farbtastic(pickerContainer);
            /**
             * In some places we won't be able to get to this.delegate cause this will be something different so we store it in local variable
             */
            var delegate = this.delegate;
            var picker = this;
            valueInput.change(function (event) {
                if (picker.options.onchange != null) {
                    picker.options.onchange.apply(this, [event]);
                }
            });
            var cancelHandler = function () {
                cancel();
                $(document).unbind('click', cancelHandler);
            };
            /**
             * Setup OK button.
             */
            $("<input type='button'/>").appendTo(pickerContainer).addClass(".rf-cpi-btn-o").val(options.okLabel).click(function () {
                /**
                 * We update value and trigger change event if value really changed
                 */
                var oldValue = valueInput.val();
                if ((oldValue != null && delegate.color != null && oldValue.toLowerCase() != delegate.color.toLowerCase()) || (oldValue == null && delegate.color
                        != null || oldValue != null && delegate.color == null)) {
                    valueInput.val(delegate.color);
                    valueInput.change();
                }
                picker.hide();
            });
            /**
             * Cancel function used to restore original color and hide the picker
             */
            var cancel = function () {
                delegate.setColor(valueInput.val());
                picker.hide();
            };
            /**
             * Setup cancel button
             */
            $("<input type='button'/>").appendTo(pickerContainer).addClass(".rf-cpi-btn-c").val(options.cancelLabel).click(cancel);
            /**
             * Attach click handler to toggler. If color picker is visible then we bind event to document so that user can cancel by clicking anywhere on the screen
             */
            toggler.click(function (e) {
                e.stopPropagation();
                if (pickerContainer.css("display") == "none") {
                    picker.show();
                    $(document).click(cancelHandler);
                } else {
                    picker.hide();
                }
            });
            /**
             * If we click on color picker then we dont want the event to be propagated to document which would invoke cancel
             */
            pickerContainer.click(function (e) {
                e.stopPropagation();
            });
            /**
             * We want to change color of toggler when user playing with colors so it can act as preview
             */
            this.delegate.linkTo(function (color) {
                toggler.css("background-color", color);
            });
            /**
             * We set initial color to the picker
             */
            this.delegate.setColor(valueInput.val());

            this.show = function () {
                if (options.onshow != null) {
                    evaluate(options.onshow);
                }
                var offset = toggler.offset();
                pickerContainer.offset({top:offset.top+toggler.outerHeight(),left:offset.left});
                pickerContainer.show();

            };
            this.hide = function () {
                if (options.onhide != null) {
                    evaluate(options.onhide);
                }
                pickerContainer.hide();
                $(document).unbind('click', cancelHandler);
            }
        }
    });

    // define super class link
    var $super = rf.ui.ColorPicker.$super;
})(jQuery, RichFaces);

