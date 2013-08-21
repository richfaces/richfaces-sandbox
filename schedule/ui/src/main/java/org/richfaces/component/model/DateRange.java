package org.richfaces.component.model;

import java.util.Date;

import org.richfaces.model.Range;

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
