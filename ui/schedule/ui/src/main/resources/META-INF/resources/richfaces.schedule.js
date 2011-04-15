/**
 * This function evaluates code in template with object in ScopeChain.
 * This is usefull if you need to evaluate code that uses member names
 * that colide with external names that the code refers to.
 * There is almost exact method in utils.js called Richfaces.eval,
 * but it swallows exception thrown during evaluation, which makes debugging
 * hard.
 */
(function ($, rf) {
    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    //    var defaultOptions = {};
    var SUBMIT_EVENT_FUNCTION = 'submitEventFunction';
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.Schedule = rf.BaseComponent.extendClass({
        // class name
        name:"Schedule",
        init: function (componentId, options) {
            if (!document.getElementById(componentId)) {
                throw "No element with id '" + componentId + "' found.";
            }
            this.options = options;
            // call constructor of parent class if needed
            $super.constructor.call(this, componentId);
            // attach component object to DOM element for
            // future cleaning and for client side API calls
            this.attachToDom(this.id);
            // ...
            /**
             * Message bundle & event handlers setup.
             */
            options = jQuery.extend({
                events: function(startDate, endDate, callback) {
                    _this.__dateRangeChange(startDate, endDate, callback)
                },
                eventDragStart: function(item, event, ui, view) {
                    _this.__itemDragStart(item, event, ui, view)
                },
                eventDragStop: function(item, event, ui, view) {
                    _this.__itemDragStop(item, event, ui, view)
                },
                eventDrop: function(item, dayDelta, minuteDelta, allDay, revertFunc, event, ui, view) {
                    _this.__itemDrop(item, dayDelta, minuteDelta, allDay, revertFunc, event, ui, view)
                },
                eventResizeStart: function(item, event, ui, view) {
                    _this.__itemResizeStart(item, event, ui, view)
                },
                eventResizeStop: function(item, event, ui, view) {
                    _this.__itemResizeStop(item, event, ui, view)
                },
                eventResize: function(item, dayDelta, minuteDelta, revertFunc, event, ui, view) {
                    _this.__itemResized(item, dayDelta, minuteDelta, revertFunc, event, ui, view)
                },
                eventClick: function(item, dayDelta, minuteDelta, revertFunc, event, ui, view) {
                    _this.__itemClick(item, dayDelta, minuteDelta, revertFunc, event, ui, view)
                },
                eventMouseover: function(item, event, view) {
                    _this.__itemMouseover(item, event, view)
                },
                eventMouseout: function(item, event, view) {
                    _this.__itemMouseout(item, event, view)
                },
                viewDisplay: function(view) {
                    _this.__viewChanged(view)
                },
                dayClick: function(date, allDay, event, view) {
                    _this.__dayClick(date, allDay, event, view)
                },
                select: function(startDate, endDate, allDay, view) {
                    _this.__dateRangeSelected(startDate, endDate, allDay, view)
                }
            }, this.messages[options['locale']], options);
            var _this = this;
            jQuery(function() {
                jQuery(document.getElementById(_this.id)).fullCalendar(options);
            });
        },
        // private functions definition
        /**
         * Utility functions.
         */
        __getResponseComponentData : function(event) {
            return event.componentData[this.id];
        },
        /**
         * Converts javascript date into integer that can be used as param
         * for new Date(long) - milliseconds since epoch.
         */
        __formatDateParam : function(date) {
            return jQuery.fullCalendar.formatDate(date, "yyyy-MM-dd HH:mm:ss");
        },
        /**
         * Compares two dates with with an accuracy of a day.
         */
        __isSameDay : function(dateA, dateB) {
            if (!dateA instanceof Date || !dateB instanceof Date) {
                throw "Both params must be Date objects";
            }
            return dateA.getYear() == dateB.getYear()
                    && dateA.getMonth() == dateB.getMonth()
                    && dateA.getDate() == dateB.getDate();
        },
        /**
         * Executes event handler passed in options.
         * @param eventName name of the event
         * @param context hash of variables that should be placed in scope when during evaluation
         */
        __executeInlineEventHandler : function(eventName, context) {
            if (this.options[eventName] != null) {
                return rf.ui.Schedule.eval("(function(){" + this.options[eventName] + "})()", context);
            } else {
                return null;
            }
        },
        __getDelegate : function() {
            return jQuery(document.getElementById(this.id));
        },
        //    ---
        /**
         * DELEGATE SETUP.
         * Delegate (fullCalendar) needs callback methods
         * for various events such as item clicking, dragging, resizing or loading
         * items.
         * Functions below can be overriden by ones declared in "options" parameter.
         */
        //    ---
        /**
         * Called by fullCalendar when it needs to load items - initial load,
         * view type change, time navigation.
         * If in ajax mode, then some initial items should be provided.
         * If so then they are used for the first invocation of this function.
         * This avoids creating additional ajax request on initial rendering.
         * Custom users code cannot raise veto so any return statements there are
         * ignored.
         */
        __dateRangeChange : function(startDate, endDate, callback) {
            var firstInvocation = this.options.initialItems != null;
            if (firstInvocation) {
                var startDateData = this.options.initialItems.startDate;
                var endDateData = this.options.initialItems.endDate;
                var initialStartDate = new Date(startDateData['year'], startDateData.month, startDateData.date);
                var initialEndDate = new Date(endDateData['year'], endDateData.month, endDateData.date);
                var items = this.options.initialItems.items;
                /**
                 * After initial load this should be cleaned so items are not cached.
                 */
                this.options.initialItems = null;
                /**
                 * In case the JSF component made a mistake in calculating initial
                 * date range we don't use initial items and just continue.
                 */
                if (this.__isSameDay(startDate, initialStartDate) && this.__isSameDay(endDate, initialEndDate)) {
                    callback(items);
                    return;
                }
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION]({} /* stub event */,
                        null,
                        'dateRangeChange',
                        null,
                        this.__formatDateParam(startDate),
                        this.__formatDateParam(endDate),
                        null, null, null,
                        function(event) {
                            var data = _this.__getResponseComponentData(event);
                            if (data != undefined) {
                                callback(data);
                            }
                            _this.__executeInlineEventHandler('ondaterangechange', { 'startDate':startDate, 'endDate':endDate, 'event':event, 'items':data });
                        }
                        );
            } else if (!firstInvocation) {
                this.__executeInlineEventHandler('ondaterangechange', { 'startDate':startDate, 'endDate':endDate, 'event':null, 'items':null });
            }
        },
        /**
         * Called by fullCalendar when item has started to be dragged.
         */
        __itemDragStart: function(item, event, ui, view) {
            this.__executeInlineEventHandler('onitemdragstart', { 'item':item, 'event':event, 'ui':ui, 'view':view });
        },
        /**
         * Called by fullCalendar when item has stopped to be dragged.
         * This is invoked between itemDragStart and itemDrop.
         */
        __itemDragStop : function(item, event, ui, view) {
            this.__executeInlineEventHandler('onitemdragstop', { 'item':item, 'event':event, 'ui':ui, 'view':view });
        },
        /**
         * Called by fullCalendar when item was dropped (dragging finished).
         * This is invoked after itemDragStop.
         * Custom users code may raise veto by returning "false". In such case
         * changes will be reverted and no event will be sent to server.
         */
        __itemDrop : function(item, dayDelta, minuteDelta, allDay, revertFunc, event, ui, view) {
            var result = this.__executeInlineEventHandler('onbeforeitemdrop', {
                'item':item,
                'dayDelta':dayDelta,
                'minuteDelta':minuteDelta,
                'allDay':allDay,
                'event':event,
                'ui':ui,
                'view':view
            });
            if (result === false) {
                revertFunc();
                return;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION](event,
                        null,
                        'itemMove',
                        item.id,
                        null,
                        null,
                        dayDelta, minuteDelta, allDay,
                        function(event) {
                            var decision = _this.__getResponseComponentData(event);

                            var vetoed = false;
                            if (decision != undefined && decision !== true) {
                                revertFunc();
                                vetoed = true;
                            }
                            _this.__executeInlineEventHandler('onitemdrop', {
                                'item':item,
                                'dayDelta':dayDelta,
                                'minuteDelta':minuteDelta,
                                'allDay':allDay,
                                'event':event,
                                'ui':ui,
                                'view':view,
                                'data':decision,
                                'vetoed':vetoed
                            });
                        });
            } else {
                this.__executeInlineEventHandler('onitemdrop', {
                    'item':item,
                    'dayDelta':dayDelta,
                    'minuteDelta':minuteDelta,
                    'allDay':allDay,
                    'event':event,
                    'ui':ui,
                    'view':view,
                    'data':null,
                    'vetoed':null
                });
            }
        },
        /**
         * Called by fullCalendar when item has started to be resized.
         */
        __itemResizeStart : function(item, event, ui, view) {
            this.__executeInlineEventHandler('onitemresizestart', { 'item':item, 'event':event, 'ui':ui, 'view':view });
        },
        /**
         * Called by fullCalendar when item has stopped to be resized.
         * This is invoked between itemResizeStart and itemResized.
         */
        __itemResizeStop : function(item, event, ui, view) {
            this.__executeInlineEventHandler('onitemresizestop', { 'item':item, 'event':event, 'ui':ui, 'view':view });
        },
        /**
         * Called by fullCalendar when item was resized.
         * This is invoked after itemResizeStop.
         * Custom users code may raise veto by returning "false". In such case
         * changes will be reverted and no event will be sent to server.
         */
        __itemResized : function(item, dayDelta, minuteDelta, revertFunc, event, ui, view) {
            var result = this.__executeInlineEventHandler('onbeforeitemresize', {
                'item':item,
                'dayDelta':dayDelta,
                'minuteDelta':minuteDelta,
                'event':event,
                'ui':ui,
                'view':view
            });
            if (result === false) {
                revertFunc();
                return;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION](event,
                        null,
                        'itemResize',
                        item.id,
                        null,
                        null,
                        dayDelta, minuteDelta, null,
                        function(event) {
                            var decision = _this.__getResponseComponentData(event);
                            var vetoed = false;
                            if (decision != undefined && decision !== true) {
                                revertFunc();
                                vetoed = true;
                            }
                            _this.__executeInlineEventHandler('onitemresize', {
                                'item':item,
                                'dayDelta':dayDelta,
                                'minuteDelta':minuteDelta,
                                'event':event,
                                'ui':ui,
                                'view':view,
                                'data':decision,
                                'vetoed':vetoed
                            });
                        }
                        );
            } else {
                this.__executeInlineEventHandler('onitemresize', {
                    'item':item,
                    'dayDelta':dayDelta,
                    'minuteDelta':minuteDelta,
                    'event':event,
                    'ui':ui,
                    'view':view,
                    'data':null,
                    'vetoed':null
                });
            }
        },
        /**
         * Called by fullCalendar when mouse moves over item.
         */
        __itemMouseover : function(item, event, view) {
            this.__executeInlineEventHandler('onitemmouseover', { 'item':item, 'event':event, 'view':view });
        },
        /**
         * Called by fullCalendar when mouse leaves item.
         */
        __itemMouseout : function(item, event, view) {
            this.__executeInlineEventHandler('onitemmouseout', { 'item':item, 'event':event, 'view':view });
        },
        /**
         * Called by fullCalendar when item is clicked.
         * Custom users code may return "false". In such case
         * changes no event will be sent to server and false will be returned
         * to fullCalendar, which will prevent redirecting to URL associated
         * with item (if such url was defined for the item).
         */
        __itemClick : function(item, event, view) {
            var result = this.__executeInlineEventHandler('onbeforeitemselect', { 'item':item, 'event':event, 'view':view });
            if (result === false) {
                return false;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION](event,
                        null,
                        'itemSelect',
                        item.id,
                        null, null, null, null, null, function(event) {
                    var data = _this.__getResponseComponentData(event);
                    _this.__executeInlineEventHandler('onitemselect', { 'item':item, 'event':event, 'view':view, 'data':data });
                }
                        );
            } else {
                this.__executeInlineEventHandler('onitemselect', { 'item':item, 'event':event, 'view':view, 'data':null });
            }
            return result;
        },
        /**
         * Called by fullCalendar when day is clicked.
         * Custom users code may raise veto by returning "false". In such case
         * changes will be reverted and no event will be sent to server.
         */
        __dayClick : function(date, allDay, event, view) {
            var result = this.__executeInlineEventHandler('onbeforedateselect', { 'date':date, 'allDay':allDay, 'event':event, 'view':view });
            if (result === false) {
                return;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION](event,
                        null,
                        'dateSelect',
                        null, this.__formatDateParam(date), null, null, null, allDay, function(event) {
                    var data = _this.__getResponseComponentData(event);
                    _this.__executeInlineEventHandler('ondateselect', { 'date':date, 'allDay':allDay, 'event':event, 'view':view, 'data':data });
                }
                        );
            } else {
                this.__executeInlineEventHandler('ondateselect', { 'date':date, 'allDay':allDay, 'event':event, 'view':view, 'data':null });
            }
        },
        /**
         * Called by fullCalendar when view or dates change.
         * We want to notify user only about view change, so we cache current view
         * on private variable "selectedView" and compare it with value passed
         * in parameter.
         * Custom users code may not raise veto so any "return" statements are
         * ignored.
         */
        __viewChanged : function(view) {
            this.__executeInlineEventHandler('onviewdisplay', { 'view':view });
            if (this.selectedView != view && this.selectedView != undefined) {
                if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                    var _this = this;
                    this.options[SUBMIT_EVENT_FUNCTION]({},
                            view.name,
                            'viewChange',
                            null, null, null, null, null, null, function(event) {
                        var data = _this.__getResponseComponentData(event);
                        _this.__executeInlineEventHandler('onviewchange', { 'view':view, 'event':event, 'data':data });
                    }
                            );
                } else {
                    this.__executeInlineEventHandler('onviewchange', {'view':view, 'event':null, 'data':null});
                }
            }
            this.selectedView = view;
        },
        /**
         * Called by fullCalendar when some date range is selected (user clicks
         * and drags over empty time cells).
         * Custom users code may raise veto by returning "false". In such case
         * changes will be reverted and no event will be sent to server.
         * What is more, selection will be cleared at the end of this function.
         * (This is bad, i guess, but for now i don't see other way to
         * hide selection marker after selection was made, event sent to server,
         * and server side listeners have created new event for that selection.
         * If no unselect would happen then we selection helper would still be there
         * and mess the looks)
         */
        __dateRangeSelected : function(startDate, endDate, allDay, view) {
            if (!this.__getDelegate().fullCalendar('option', 'selectable')) {
                return;
            }
            var result = this.__executeInlineEventHandler('onbeforedaterangeselect', { 'startDate':startDate, 'endDate':endDate, 'allDay':allDay, 'view':view });
            if (result === false) {
                return;
            }
            if (this.options[SUBMIT_EVENT_FUNCTION] != null) {
                var _this = this;
                this.options[SUBMIT_EVENT_FUNCTION]({},
                        null,
                        'dateRangeSelect',
                        null, this.__formatDateParam(startDate), this.__formatDateParam(endDate), null, null, allDay,
                        function(event) {
                            if (_this.options['autoRefreshOnDateRangeSelect'] !== false) {
                                _this.refetchItems();
                            }
                            var data = _this.__getResponseComponentData(event);
                            _this.__executeInlineEventHandler('ondaterangeselect', {
                                'startDate':startDate,
                                'endDate':endDate,
                                'allDay':allDay,
                                'view':view,
                                'event':event,
                                'data':data
                            });
                        }
                        );
            } else {
                this.__executeInlineEventHandler('ondaterangeselect', {
                    'startDate':startDate,
                    'endDate':endDate,
                    'allDay':allDay,
                    'view':view,
                    'event':null,
                    'data':null
                });
            }
        },
        // public API
        select : function(startDate, endDate, allDay) {
            this.__getDelegate().fullCalendar('select', startDate, endDate, allDay);
        },
        unselect: function() {
            this.__getDelegate().fullCalendar('unselect');
        },
        render: function() {
            this.__getDelegate().fullCalendar('render');
        },
        getView: function() {
            return this.__getDelegate().fullCalendar('getView');
        },
        changeView: function(viewName) {
            this.__getDelegate().fullCalendar('changeView', viewName);
        },
        prev: function() {
            this.__getDelegate().fullCalendar('prev');
        },
        next: function() {
            this.__getDelegate().fullCalendar('next');
        },
        prevYear: function() {
            this.__getDelegate().fullCalendar('prevYear');
        },
        nextYear: function() {
            this.__getDelegate().fullCalendar('nextYear');
        },
        today: function() {
            this.__getDelegate().fullCalendar('today');
        },
        gotoDate: function(year, month, date) {
            this.__getDelegate().fullCalendar('gotoDate', year, month, date);
        },
        incrementDate: function(years, months, days) {
            this.__getDelegate().fullCalendar('incrementDate', years, months, days);
        },
        updateItem: function(item) {
            this.__getDelegate().fullCalendar('updateItem', item);
        },
        getItems: function(idOrFilter) {
            return this.__getDelegate().fullCalendar('clientEvents', idOrFilter);
        },
        removeItems: function(idOrFilter) {
            this.__getDelegate().fullCalendar('removeEvents', idOrFilter);
        },
        refetchItems: function() {
            this.__getDelegate().fullCalendar('refetchEvents');
        },
        addItemsSource: function(source) {
            this.__getDelegate().fullCalendar('addEventSource', source);
        },
        removeItemsSource: function(source) {
            this.__getDelegate().fullCalendar('removeEventSource', source);
        },
        addItem: function(event, stick) {
            this.__getDelegate().fullCalendar('renderEvent', event, stick);
        },
        reRender: function() {
            this.__getDelegate().fullCalendar('rerenderEvents');
        },
        // destructor definition
        destroy: function () {
            // define destructor if additional cleaning is needed but
            // in most cases its not nessesary.
            // call parent’s destructor
            $super.destroy.call(this);
            this.__getDelegate().fullCalendar('destroy');
        }
    });
    rf.ui.Schedule.prototype.messages = [];
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Schedule.$super;
})(jQuery, RichFaces);
RichFaces.ui.Schedule.eval = function(template, object) {
    var value;
    with (object) {
        value = eval(template);
    }
    return value;
};