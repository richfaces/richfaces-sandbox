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

    var DEFAULT_OPTIONS = {switchMode: 'ajax'};

    rf.ui.CollapsiblePanelItem = rf.ui.TogglePanelItem.extendClass({

            init : function (componentId, options) {
                rf.ui.TogglePanelItem.call(this, componentId, $.extend({}, DEFAULT_OPTIONS, options));

                this.headerClass = "rf-cp-hdr-" + this.__state();
            },

            __enter : function () {
                this.__content().show();
                this.__header().addClass(this.headerClass);

                return true;
            },

            __leave : function () {
                this.__content().hide();

                if (this.options.switchMode == 'client') {
                    this.__header().removeClass(this.headerClass);
                }

                return true;
            },

            __state : function () {
                return this.getName() === "true" ? "exp" : "colps";
            },

            __content : function () {
                return $(rf.getDomElement(this.id));
            },

            __header : function () {
                return $(rf.getDomElement(this.togglePanelId + ":header"));
            }
        });
})(jQuery, RichFaces);
