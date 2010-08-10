package org.richfaces.demo.tables;

import java.io.Serializable;
import java.util.Comparator;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.component.SortOrder;
import org.richfaces.demo.tables.model.capitals.Capital;

@ManagedBean
@ViewScoped
public class CapitalsSortingBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6237417487105926855L;
    private static final String TIMEZONE_GMT_SEPARATOR = "-";
    private SortOrder capitalsOrder = SortOrder.unsorted;
    private SortOrder statesOrder = SortOrder.unsorted;
    private SortOrder timeZonesOrder = SortOrder.unsorted;

    public void sortByCapitals() {
        statesOrder = SortOrder.unsorted;
        timeZonesOrder = SortOrder.unsorted;
        if (capitalsOrder.equals(SortOrder.ascending)) {
            setCapitalsOrder(SortOrder.descending);
        } else {
            setCapitalsOrder(SortOrder.ascending);
        }
    }

    public void sortByStates() {
        capitalsOrder = SortOrder.unsorted;
        timeZonesOrder = SortOrder.unsorted;
        if (statesOrder.equals(SortOrder.ascending)) {
            setStatesOrder(SortOrder.descending);
        } else {
            setStatesOrder(SortOrder.ascending);
        }
    }

    public void sortByTimeZones() {
        statesOrder = SortOrder.unsorted;
        capitalsOrder = SortOrder.unsorted;
        if (timeZonesOrder.equals(SortOrder.ascending)) {
            setTimeZonesOrder(SortOrder.descending);
        } else {
            setTimeZonesOrder(SortOrder.ascending);
        }
    }

    public Comparator<Capital> getTimeZoneComparator() {
        return new Comparator<Capital>() {

            public int compare(Capital o1, Capital o2) {
                int tz1Int = Integer.valueOf(o1.getTimeZone().split(TIMEZONE_GMT_SEPARATOR)[1]);
                int tz2Int = Integer.valueOf(o2.getTimeZone().split(TIMEZONE_GMT_SEPARATOR)[1]);
                if (tz1Int == tz2Int) {
                    return 0;
                }
                if (tz1Int > tz2Int) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
    }

    public SortOrder getCapitalsOrder() {
        return capitalsOrder;
    }

    public void setCapitalsOrder(SortOrder capitalsOrder) {
        this.capitalsOrder = capitalsOrder;
    }

    public SortOrder getStatesOrder() {
        return statesOrder;
    }

    public void setStatesOrder(SortOrder statesOrder) {
        this.statesOrder = statesOrder;
    }

    public SortOrder getTimeZonesOrder() {
        return timeZonesOrder;
    }

    public void setTimeZonesOrder(SortOrder timeZonesOrder) {
        this.timeZonesOrder = timeZonesOrder;
    }

}
