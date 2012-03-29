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

    var ITEMS_SWITCHER = {

        /**
         * @param {TogglePanelItem} oldPanel
         * @param {TogglePanelItem} newPanel
         *
         * @return {void}
         * */
        exec : function (oldPanel, newPanel) {
            if (newPanel.switchMode == "server") {
                return this.execServer(oldPanel, newPanel);
            } else if (newPanel.switchMode == "ajax") {
                return this.execAjax(oldPanel, newPanel);
            } else if (newPanel.switchMode == "client") {
                return this.execClient(oldPanel, newPanel);
            } else {
                rf.log.error("SwitchItems.exec : unknown switchMode (" + newPanel.switchMode + ")");
            }
        },

        /**
         * @protected
         * @param {TogglePanelItem} oldPanel
         * @param {TogglePanelItem} newPanel
         *
         * @return {Boolean} false
         * */
        execServer : function (oldPanel, newPanel) {
            if (oldPanel) {
                var continueProcess = oldPanel.__leave();
                if (!continueProcess) {
                    return false;
                }
            }

            this.__setActiveItem(newPanel);

            var params = {};

            params[newPanel.getTogglePanel().id] = newPanel.name;
            params[newPanel.id] = newPanel.id;

            $.extend(params, newPanel.getTogglePanel().options["ajax"] || {});

            rf.submitForm(this.__getParentForm(newPanel), params);

            return false;
        },

        /**
         * @protected
         * @param {TogglePanelItem} oldPanel
         * @param {TogglePanelItem} newPanel
         *
         * @return {Boolean} false
         * */
        execAjax : function (oldPanel, newPanel) {
            var options = $.extend({}, newPanel.getTogglePanel().options["ajax"], {});

            this.__setActiveItem(newPanel);
            rf.ajax(newPanel.id, null, options);

            if (oldPanel) {
                this.__setActiveItem(oldPanel);
            }

            return false;
        },

        /**
         * @protected
         * @param {TogglePanelItem} oldPanel
         * @param {TogglePanelItem} newPanel
         *
         * @return {undefined}
         *             - false - if process has been terminated
         *             - true  - in other cases
         * */
        execClient : function (oldPanel, newPanel) {
            if (oldPanel) {
                var continueProcess = oldPanel.__leave();
                if (!continueProcess) {
                    return false;
                }
            }

            this.__setActiveItem(newPanel);

            newPanel.__enter();
            newPanel.getTogglePanel().__fireItemChange(oldPanel, newPanel);

            return true;
        },

        /**
         * @private
         * */
        __getParentForm : function (comp) {
            return $(rf.getDomElement(comp.id)).parents('form:first');
        },

        /**
         * @private
         * */
        __setActiveItem : function (item) {
            rf.getDomElement(item.togglePanelId + "-value").value = item.getName(); // todo it is should be toogle panel method
            item.getTogglePanel().activeItem = item.getName();
        }
    };


    rf.ui.TabPanel = rf.ui.TogglePanel.extendClass({
            // class name
            name:"TabPanel",

            /**
             * @class TabPanel
             * @name TabPanel
             *
             * @constructor
             * @param {String} componentId - component id
             * @param {Hash} options - params
             * */
            init : function (componentId, options) {
                rf.ui.TogglePanel.call(this, componentId, options);
                this.items = [];

                this.isKeepHeight = options["isKeepHeight"] || false
            },

            __itemsSwitcher : function () {
                return ITEMS_SWITCHER;
            }

        });
})(jQuery, RichFaces);
