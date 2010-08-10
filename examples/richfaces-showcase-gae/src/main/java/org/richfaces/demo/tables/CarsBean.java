/**
 *
 */
package org.richfaces.demo.tables;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import org.richfaces.demo.common.data.RandomHelper;
import org.richfaces.demo.tables.model.cars.InventoryItem;
import org.richfaces.demo.tables.model.cars.InventoryVendorItem;
import org.richfaces.demo.tables.model.cars.InventoryVendorList;

@ManagedBean(name = "carsBean")
@SessionScoped
public class CarsBean implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -3832235132261771583L;
    private static final int DECIMALS = 1;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    private List<InventoryItem> allInventoryItems = null;
    private List<InventoryVendorList> inventoryVendorLists = null;

    public List<SelectItem> getVendorOptions() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("",""));
        for (InventoryVendorList vendorList : getInventoryVendorLists()) {
            result.add(new SelectItem(vendorList.getVendor()));
        }
        return result;
    }
    
    public List<String> getAllVendors() {
        List<String> result = new ArrayList<String>();
        for (InventoryVendorList vendorList : getInventoryVendorLists()) {
            result.add(vendorList.getVendor());
        }
        return result;
    }

    public List<InventoryVendorList> getInventoryVendorLists() {
        synchronized (this) {
            if (inventoryVendorLists == null) {
                inventoryVendorLists = new ArrayList<InventoryVendorList>();
                List<InventoryItem> inventoryItems = getAllInventoryItems();

                Collections.sort(inventoryItems, new Comparator<InventoryItem>() {
                    public int compare(InventoryItem o1, InventoryItem o2) {
                        return o1.getVendor().compareTo(o2.getVendor()); 
                    }
                });
                Iterator<InventoryItem> iterator = inventoryItems.iterator();
                InventoryVendorList vendorList = new InventoryVendorList();
                vendorList.setVendor(inventoryItems.get(0).getVendor());
                while (iterator.hasNext()) {
                    InventoryItem item = iterator.next();
                    InventoryVendorItem newItem = new InventoryVendorItem();
                    itemToVendorItem(item, newItem);
                    if (!item.getVendor().equals(vendorList.getVendor())) {
                        inventoryVendorLists.add(vendorList);
                        vendorList = new InventoryVendorList();
                        vendorList.setVendor(item.getVendor());
                    }
                    vendorList.getVendorItems().add(newItem);
                }
                inventoryVendorLists.add(vendorList);
            }
        }
        return inventoryVendorLists;
    }

    private void itemToVendorItem(InventoryItem item, InventoryVendorItem newItem) {
        newItem.setActivity(item.getActivity());
        newItem.setChangePrice(item.getChangePrice());
        newItem.setChangeSearches(item.getChangeSearches());
        newItem.setDaysLive(item.getDaysLive());
        newItem.setExposure(item.getExposure());
        newItem.setInquiries(item.getInquiries());
        newItem.setMileage(item.getMileage());
        newItem.setMileageMarket(item.getMileageMarket());
        newItem.setModel(item.getModel());
        newItem.setPrice(item.getPrice());
        newItem.setPriceMarket(item.getPriceMarket());
        newItem.setPrinted(item.getPrinted());
        newItem.setStock(item.getStock());
        newItem.setVin(item.getVin());
    }

    public List<InventoryItem> getAllInventoryItems() {
        synchronized (this) {
            if (allInventoryItems == null) {
                allInventoryItems = new ArrayList<InventoryItem>();

                for (int k = 0; k <= 5; k++) {
                    try {
                        switch (k) {
                            case 0:
                                allInventoryItems.addAll(createCar("Chevrolet", "Corvette", 5));
                                allInventoryItems.addAll(createCar("Chevrolet", "Malibu", 8));
                                allInventoryItems.addAll(createCar("Chevrolet", "Tahoe", 6));

                                break;

                            case 1:
                                allInventoryItems.addAll(createCar("Ford", "Taurus", 12));
                                allInventoryItems.addAll(createCar("Ford", "Explorer", 11));

                                break;

                            case 2:
                                allInventoryItems.addAll(createCar("Nissan", "Maxima", 9));
                                allInventoryItems.addAll(createCar("Nissan", "Frontier", 6));

                                break;

                            case 3:
                                allInventoryItems.addAll(createCar("Toyota", "4-Runner", 7));
                                allInventoryItems.addAll(createCar("Toyota", "Camry", 15));
                                allInventoryItems.addAll(createCar("Toyota", "Avalon", 13));

                                break;

                            case 4:
                                allInventoryItems.addAll(createCar("GMC", "Sierra", 8));
                                allInventoryItems.addAll(createCar("GMC", "Yukon", 10));

                                break;

                            case 5:
                                allInventoryItems.addAll(createCar("Infiniti", "G35", 6));
                                allInventoryItems.addAll(createCar("Infiniti", "EX35", 5));

                                break;

                            default:
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("!!!!!!loadallInventoryItems Error: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        return allInventoryItems;
    }

    public List<InventoryItem> createCar(String vendor, String model, int count) {
        ArrayList<InventoryItem> iiList = null;

        try {
            int arrayCount = count;
            InventoryItem[] demoInventoryItemArrays = new InventoryItem[arrayCount];

            for (int j = 0; j < demoInventoryItemArrays.length; j++) {
                InventoryItem ii = new InventoryItem();

                ii.setVendor(vendor);
                ii.setModel(model);
                ii.setStock(RandomHelper.randomstring(6, 7));
                ii.setVin(RandomHelper.randomstring(14, 15));
                ii.setMileage(new BigDecimal(RandomHelper.rand(5000, 80000)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setMileageMarket(new BigDecimal(RandomHelper.rand(25000, 45000)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setPrice(new Integer(RandomHelper.rand(15000, 55000)));
                ii.setPriceMarket(new BigDecimal(RandomHelper.rand(15000, 55000)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setDaysLive(RandomHelper.rand(1, 90));
                ii.setChangeSearches(new BigDecimal(RandomHelper.rand(0, 5)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setChangePrice(new BigDecimal(RandomHelper.rand(0, 5)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setExposure(new BigDecimal(RandomHelper.rand(0, 5)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setActivity(new BigDecimal(RandomHelper.rand(0, 5)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setPrinted(new BigDecimal(RandomHelper.rand(0, 5)).setScale(DECIMALS, ROUNDING_MODE));
                ii.setInquiries(new BigDecimal(RandomHelper.rand(0, 5)).setScale(DECIMALS, ROUNDING_MODE));
                demoInventoryItemArrays[j] = ii;
            }

            iiList = new ArrayList<InventoryItem>(Arrays.asList(demoInventoryItemArrays));
        } catch (Exception e) {
            System.out.println("!!!!!!createCategory Error: " + e.getMessage());
            e.printStackTrace();
        }

        return iiList;
    }
}
