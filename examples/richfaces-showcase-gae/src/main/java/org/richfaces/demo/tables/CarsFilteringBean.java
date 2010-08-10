package org.richfaces.demo.tables;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.demo.tables.model.cars.InventoryItem;
import org.richfaces.model.Filter;

@ManagedBean
@SessionScoped
public class CarsFilteringBean implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -5680001353441022183L;
    private String vinFilter;
    private String vendorFilter;
    private Long mileageFilter;

    public Filter<?> getMileageFilterImpl() {
        return new Filter<InventoryItem>() {
            public boolean accept(InventoryItem item) {
                Long mileage = getMileageFilter();
                if (mileage == null || mileage==0 || mileage.compareTo(item.getMileage().longValue()) >= 0) {
                    return true;
                }
                return false;
            }
        };
    }
    public Filter<?> getFilterVendor(){
        return new Filter<InventoryItem>() {
            public boolean accept(InventoryItem t) {
                String vendor = getVendorFilter();
                if (vendor == null || vendor.length() == 0 || vendor.equals(t.getVendor())) {
                    return true;
                }
                return false;
            }
        };
    }

    public Long getMileageFilter() {
        return mileageFilter;
    }

    public void setMileageFilter(Long mileageFilter) {
        this.mileageFilter = mileageFilter;
    }

    public String getVendorFilter() {
        return vendorFilter;
    }

    public void setVendorFilter(String vendorFilter) {
        this.vendorFilter = vendorFilter;
    }

    public String getVinFilter() {
        return vinFilter;
    }

    public void setVinFilter(String vinFilter) {
        this.vinFilter = vinFilter;
    }
}
