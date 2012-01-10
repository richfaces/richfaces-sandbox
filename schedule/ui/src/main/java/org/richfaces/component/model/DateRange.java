package org.richfaces.component.model;

import org.ajax4jsf.model.Range;

import java.util.Date;

public class DateRange implements Range {

    private Date startDate;
    private Date endDate;

    public DateRange() {
    }

    public DateRange(Date startDate, Date endDate) {
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
