window.RichFaces = window.RichFaces || {};
//TODO test ajax reRender
RichFaces.Schedule = function(id, locale, options, dateRangeChangeEventName, itemSelectEventName, itemMoveEventName, itemResizeEventName, viewChangeEventName, dateSelectEventName, dateRangeSelectEventName, submitEventFunction) {

    var _this = this;
    this.scheduleNode = document.getElementById(id);
    this.scheduleNode.richfaces = {};
    this.scheduleNode.richfaces.component = this;
    if (!this.scheduleNode) {
        throw "No element with id '" + id + "' found.";
    }

    /**
     * Message bundle setup.
     */
    options = jQuery.extend({}, this.messages[locale], options);

    jQuery(document).ready(function() {
        _this.delegate = jQuery(_this.scheduleNode).fullCalendar(options);
    });


    //    ---
    /**
     * Utility functions.
     */
    //    ---
    /**
     * Converts javascript date into integer that can be used as param
     * for new Date(long) - milliseconds since epoch.
     */
    var formatDateParam = function(date) {
        return Math.round(date.getTime() / 1000);
    };
    /**
     * Compares two dates with with an accuracy of a day.
     */
    var isSameDay = function(dateA, dateB) {
        if (!dateA instanceof Date || !dateB instanceof Date) {
            throw "Both params must be Date objects";
        }
        return dateA.getYear() == dateB.getYear()
                && dateA.getMonth() == dateB.getMonth()
                && dateA.getDate() == dateB.getDate();
    };
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
    var dateRangeChange = function(startDate, endDate, callback) {
        var firstInvocation = options.initialItems != null;
        if (firstInvocation) {
            var startDateData = options.initialItems.startDate;
            var endDateData = options.initialItems.endDate;
            var initialStartDate = new Date(startDateData.year, startDateData.month, startDateData.date);
            var initialEndDate = new Date(endDateData.year, endDateData.month, endDateData.date);
            var items = options.initialItems.items;
            /**
             * After initial load this should be cleaned so items are not cached.
             */
            options.initialItems = null;
            /**
             * In case the JSF component made a mistake in calculating initial
             * date range we don't use initial items and just continue.
             */
            if (isSameDay(startDate, initialStartDate) && isSameDay(endDate, initialEndDate)) {
                callback(items);
                return;
            }
        }
        if (submitEventFunction != null) {
            submitEventFunction({} /* stub event */,
                    null,
                    dateRangeChangeEventName,
                    null,
                    formatDateParam(startDate),
                    formatDateParam(endDate),
                    null, null, null,
                    function(event, data) {
                        if (data != undefined) {
                            callback(data);
                        }
                        if (options.ondaterangechange != null) {
                            RichFaces.Schedule.eval("(function(){" + options.ondaterangechange + "})()", {
                                'startDate':startDate,
                                'endDate':endDate,
                                'data':data,
                                'items':data
                            });
                        }
                    }
                    );
        } else if (!firstInvocation && options.ondaterangechange != null) {
            RichFaces.Schedule.eval("(function(){" + options.ondaterangechange + "})()", {
                'startDate':startDate,
                'endDate':endDate
            });
        }
    };
    /**
     * Called by fullCalendar when item has started to be dragged.
     */
    var itemDragStart = function(item, event, ui, view) {
        if (options.onitemdragstart != null) {
            RichFaces.Schedule.eval("(function(){" + options.onitemdragstart + "})()", {
                'item':item,
                'event':event,
                'ui':ui,
                'view':view
            });
        }
    };
    /**
     * Called by fullCalendar when item has stopped to be dragged.
     * This is invoked between itemDragStart and itemDrop.
     */
    var itemDragStop = function(item, event, ui, view) {
        if (options.onitemdragstop != null) {
            RichFaces.Schedule.eval("(function(){" + options.onitemdragstop + "})()", {
                'item':item,
                'event':event,
                'ui':ui,
                'view':view
            });
        }
    };
    /**
     * Called by fullCalendar when item was dropped (dragging finished).
     * This is invoked after itemDragStop.
     * Custom users code may raise veto by returning "false". In such case
     * changes will be reverted and no event will be sent to server.
     */
    var itemDrop = function(item, dayDelta, minuteDelta, allDay, revertFunc, event, ui, view) {
        var result;
        if (options.onbeforeitemdrop != null) {
            result = RichFaces.Schedule.eval("(function(){" + options.onbeforeitemdrop + "})()", {
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
        }
        if (submitEventFunction != null) {
            submitEventFunction(event,
                    null,
                    itemMoveEventName,
                    item.id,
                    null,
                    null,
                    dayDelta, minuteDelta, allDay,
                    function(event, decision) {
                        var vetoed = false;
                        if (decision != undefined && decision !== true) {
                            revertFunc();
                            vetoed = true;
                        }
                        if (options.onitemdrop != null) {
                            RichFaces.Schedule.eval("(function(){" + options.onitemdrop + "})()", {
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
                        }
                    }
                    );
        }
    };
    /**
     * Called by fullCalendar when item has started to be resized.
     */
    var itemResizeStart = function(item, event, ui, view) {
        if (options.onitemresizestart != null) {
            RichFaces.Schedule.eval("(function(){" + options.onitemresizestart + "})()", {
                'item':item,
                'event':event,
                'ui':ui,
                'view':view
            });
        }
    };
    /**
     * Called by fullCalendar when item has stopped to be resized.
     * This is invoked between itemResizeStart and itemResized.
     */
    var itemResizeStop = function(item, event, ui, view) {
        if (options.onitemresizestop != null) {
            RichFaces.Schedule.eval("(function(){" + options.onitemresizestop + "})()", {
                'item':item,
                'event':event,
                'ui':ui,
                'view':view
            });
        }
    };
    /**
     * Called by fullCalendar when item was resized.
     * This is invoked after itemResizeStop.
     * Custom users code may raise veto by returning "false". In such case
     * changes will be reverted and no event will be sent to server.
     */
    var itemResized = function(item, dayDelta, minuteDelta, revertFunc, event, ui, view) {
        var result;
        if (options.onbeforeitemresize != null) {
            result = RichFaces.Schedule.eval("(function(){" + options.onbeforeitemresize + "})()", {
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
        }
        if (submitEventFunction != null) {
            submitEventFunction(event,
                    null,
                    itemResizeEventName,
                    item.id,
                    null,
                    null,
                    dayDelta, minuteDelta, null,
                    function(event, decision) {
                        var vetoed = false;
                        if (decision != undefined && decision !== true) {
                            revertFunc();
                            vetoed = true;
                        }
                        if (options.onitemresize != null) {
                            RichFaces.Schedule.eval("(function(){" + options.onitemresize + "})()", {
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
                    }
                    );
        }
    };
    /**
     * Called by fullCalendar when mouse moves over item.
     */
    var itemMouseover = function(item, event, view) {
        if (options.onitemmouseover != null) {
            RichFaces.Schedule.eval("(function(){" + options.onitemmouseover + "})()", {
                'item':item,
                'event':event,
                'view':view
            });
        }
    };
    /**
     * Called by fullCalendar when mouse leaves item.
     */
    var itemMouseout = function(item, event, view) {
        if (options.onitemmouseout != null) {
            RichFaces.Schedule.eval("(function(){" + options.onitemmouseout + "})()", {
                'item':item,
                'event':event,
                'view':view
            });
        }
    };
    /**
     * Called by fullCalendar when item is clicked.
     * Custom users code may return "false". In such case
     * changes no event will be sent to server and false will be returned
     * to fullCalendar, which will prevent redirecting to URL associated
     * with item (if such url was defined for the item).
     */
    var itemClick = function(item, event, view) {
        var result;
        if (options.onbeforeitemselect != null) {
            result = RichFaces.Schedule.eval("(function(){" + options.onbeforeitemselect + "})()", {
                'item':item,
                'event':event,
                'view':view
            });
        }
        if (result === false) {
            return false;
        }
        if (submitEventFunction != null) {
            submitEventFunction(event,
                    null,
                    itemSelectEventName,
                    item.id,
                    null, null, null, null, null, function(event, data) {
                if (options.onitemselect != null) {
                    RichFaces.Schedule.eval("(function(){" + options.onitemselect + "})()", {
                        'item':item,
                        'event':event,
                        'view':view,
                        'data':data
                    });
                }
            }
                    );
        }
        return result;
    };
    /**
     * Called by fullCalendar when day is clicked.
     * Custom users code may raise veto by returning "false". In such case
     * changes will be reverted and no event will be sent to server.
     */
    var dayClick = function(date, allDay, event, view) {
        if (options.onbeforedateselect != null) {
            var result = RichFaces.Schedule.eval("(function(){" + options.onbeforedateselect + "})()", {
                'date':date,
                'allDay':allDay,
                'event':event,
                'view':view
            });
            if (result === false) {
                return;
            }
        }
        if (submitEventFunction != null) {
            submitEventFunction(event,
                    null,
                    dateSelectEventName,
                    null, formatDateParam(date), null, null, null, allDay, function(event, data) {
                if (options.ondateselect != null) {
                    RichFaces.Schedule.eval("(function(){" + options.ondateselect + "})()", {
                        'date':date,
                        'allDay':allDay,
                        'event':event,
                        'view':view,
                        'data':data
                    });
                }
            }
                    );
        }
    };
    var selectedView;
    /**
     * Called by fullCalendar when view or dates change.
     * We want to notify user only about view change, so we cache current view
     * on private variable "selectedView" and compare it with value passed
     * in parameter.
     * Custom users code may not raise veto so any "return" statements are
     * ignored.
     */
    var viewChanged = function(view) {
        if (selectedView != view && selectedView != undefined) {
            if (submitEventFunction != null) {
                submitEventFunction({},
                        view.name,
                        viewChangeEventName,
                        null, null, null, null, null, null, function(event, data) {
                    if (options.onviewchange != null) {
                        RichFaces.Schedule.eval("(function(){" + options.onviewchange + "})()", {
                            'view':view,
                            'data':data
                        });
                    }
                }
                        );
            } else if (options.onviewchange != null) {
                RichFaces.Schedule.eval("(function(){" + options.onviewchange + "})()", {
                    'view':view
                });
            }
        }
        selectedView = view;
    };
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
    var dateRangeSelected = function(startDate, endDate, allDay, view) {
        if (!_this.delegate.fullCalendar('option', 'selectable')) {
            return;
        }
        var result;
        if (options.onbeforedaterangeselect != null) {
            result = RichFaces.Schedule.eval("(function(){" + options.onbeforedaterangeselect + "})()", {
                'startDate':startDate,
                'endDate':endDate,
                'allDay':allDay,
                'view':view
            });
        }
        if (result === false) {
            return;
        }
        if (submitEventFunction != null) {
            submitEventFunction({},
                    null,
                    dateRangeSelectEventName,
                    null, formatDateParam(startDate), formatDateParam(endDate), null, null, allDay,
                    function(event, data) {
                        _this.refetchItems();
                        if (options.ondaterangeselect != null) {
                            RichFaces.Schedule.eval("(function(){" + options.ondaterangeselect + "})()", {
                                'startDate':startDate,
                                'endDate':endDate,
                                'allDay':allDay,
                                'view':view,
                                'data':data
                            });
                        }
                    }
                    );
        }
    };
    options = jQuery.extend({
        events: dateRangeChange,
        eventDragStart: itemDragStart,
        eventDragStop: itemDragStop,
        eventDrop: itemDrop,
        eventResizeStart: itemResizeStart,
        eventResizeStop: itemResizeStop,
        eventResize: itemResized,
        eventClick: itemClick,
        eventMouseover: itemMouseover,
        eventMouseout: itemMouseout,
        viewDisplay: viewChanged,
        dayClick: dayClick,
        select: dateRangeSelected
    }, options);

};
RichFaces.Schedule.prototype.messages = {};
/**
 * This function evaluates code in template with object in ScopeChain.
 * This is usefull if you need to evaluate code that uses member names
 * that colide with external names that the code refers to.
 * There is almost exact method in utils.js called Richfaces.eval,
 * but it swallows exception thrown during evaluation, which makes debugging
 * hard.
 */
RichFaces.Schedule.eval = function(template, object) {
    var value;
    with (object) {
        value = eval(template);
    }
    return value;
};
RichFaces.Schedule.prototype.select = function(startDate, endDate, allDay) {
    this.delegate.fullCalendar('select', startDate, endDate, allDay);
};
RichFaces.Schedule.prototype.unselect = function() {
    this.delegate.fullCalendar('unselect');
};
RichFaces.Schedule.prototype.render = function() {
    this.delegate.fullCalendar('render');
};
RichFaces.Schedule.prototype.destroy = function() {
    this.delegate.fullCalendar('destroy');
};
RichFaces.Schedule.prototype.getView = function() {
    return this.delegate.fullCalendar('getView');
};
RichFaces.Schedule.prototype.changeView = function(viewName) {
    this.delegate.fullCalendar('changeView', viewName);
};
RichFaces.Schedule.prototype.prev = function() {
    this.delegate.fullCalendar('prev');
};
RichFaces.Schedule.prototype.next = function() {
    this.delegate.fullCalendar('next');
};
RichFaces.Schedule.prototype.prevYear = function() {
    this.delegate.fullCalendar('prevYear');
};
RichFaces.Schedule.prototype.nextYear = function() {
    this.delegate.fullCalendar('nextYear');
};
RichFaces.Schedule.prototype.today = function() {
    this.delegate.fullCalendar('today');
};
RichFaces.Schedule.prototype.gotoDate = function(year, month, date) {
    this.delegate.fullCalendar('gotoDate', year, month, date);
};
RichFaces.Schedule.prototype.incrementDate = function(years, months, days) {
    this.delegate.fullCalendar('incrementDate', years, months, days);
};
RichFaces.Schedule.prototype.updateItem = function(item) {
    this.delegate.fullCalendar('updateItem', item);
};
RichFaces.Schedule.prototype.getItems = function(idOrFilter) {
    return this.delegate.fullCalendar('clientEvents', idOrFilter);
};
RichFaces.Schedule.prototype.removeItems = function(idOrFilter) {
    this.delegate.fullCalendar('removeEvents', idOrFilter);
};
RichFaces.Schedule.prototype.refetchItems = function() {
    this.delegate.fullCalendar('refetchEvents');
};
RichFaces.Schedule.prototype.addItemsSource = function(source) {
    this.delegate.fullCalendar('addEventSource', source);
};
RichFaces.Schedule.prototype.removeItemsSource = function(source) {
    this.delegate.fullCalendar('removeEventSource', source);
};
RichFaces.Schedule.prototype.addItem = function(event, stick) {
    this.delegate.fullCalendar('renderEvent', event, stick);
};
RichFaces.Schedule.prototype.reRender = function() {
    this.delegate.fullCalendar('rerenderEvents');
};