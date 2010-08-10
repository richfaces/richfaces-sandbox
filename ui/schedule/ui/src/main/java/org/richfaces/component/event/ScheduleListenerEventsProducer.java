package org.richfaces.component.event;

public interface ScheduleListenerEventsProducer {

    void addItemSelectedListener(ScheduleItemSelectListener listener);

    void removeItemSelectedListener(ScheduleItemSelectListener listener);

    ScheduleItemSelectListener[] getItemSelectedListeners();

    void addItemMoveListener(ScheduleItemMoveListener listener);

    void removeItemMoveListener(ScheduleItemMoveListener listener);

    ScheduleItemMoveListener[] getItemMoveListeners();

    void addItemResizeListener(ScheduleItemResizeListener listener);

    void removeItemResizeListener(ScheduleItemResizeListener listener);

    ScheduleItemResizeListener[] getItemResizeListeners();

    void addViewChangedListener(ScheduleViewChangeListener listener);

    void removeViewChangedListener(ScheduleViewChangeListener listener);

    ScheduleViewChangeListener[] getViewChangedListeners();

    void addDateRangeChangedListener(ScheduleDateRangeChangeListener listener);

    void removeDateRangeChangedListener(ScheduleDateRangeChangeListener listener);

    ScheduleDateRangeChangeListener[] getDateRangeChangedListeners();

    void addDateRangeSelectedListener(ScheduleDateRangeSelectListener listener);

    void removeDateRangeSelectedListener(ScheduleDateRangeSelectListener listener);

    ScheduleDateRangeSelectListener[] getDateRangeSelectedListeners();

    void addDateSelectedListener(ScheduleDateSelectListener listener);

    void removeDateSelectedListener(ScheduleDateSelectListener listener);

    ScheduleDateSelectListener[] getDateSelectedListeners();
}
