package org.richfaces.demo.tables.model.cars;

import java.io.Serializable;

public class InventoryItem extends InventoryVendorItem implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 2052446469750935597L;
    private String vendor;

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
