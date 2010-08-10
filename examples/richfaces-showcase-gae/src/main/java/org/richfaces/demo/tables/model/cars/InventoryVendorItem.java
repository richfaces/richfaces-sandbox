package org.richfaces.demo.tables.model.cars;

import java.io.Serializable;
import java.math.BigDecimal;

public class InventoryVendorItem implements Serializable {

    private static final long serialVersionUID = -5424674835711375626L;
    
    BigDecimal activity;
    BigDecimal changePrice;
    BigDecimal changeSearches;
    int daysLive;
    BigDecimal exposure;
    BigDecimal inquiries;
    BigDecimal mileage;
    BigDecimal mileageMarket;
    String model;
    Integer price;
    BigDecimal priceMarket;
    BigDecimal printed;
    String stock;
    String vin;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public BigDecimal getMileage() {
        return mileage;
    }

    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }

    public BigDecimal getMileageMarket() {
        return mileageMarket;
    }

    public void setMileageMarket(BigDecimal mileageMarket) {
        this.mileageMarket = mileageMarket;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BigDecimal getPriceMarket() {
        return priceMarket;
    }

    public void setPriceMarket(BigDecimal priceMarket) {
        this.priceMarket = priceMarket;
    }

    public int getDaysLive() {
        return daysLive;
    }

    public void setDaysLive(int daysLive) {
        this.daysLive = daysLive;
    }

    public BigDecimal getChangeSearches() {
        return changeSearches;
    }

    public void setChangeSearches(BigDecimal changeSearches) {
        this.changeSearches = changeSearches;
    }

    public BigDecimal getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(BigDecimal changePrice) {
        this.changePrice = changePrice;
    }

    public BigDecimal getExposure() {
        return exposure;
    }

    public void setExposure(BigDecimal exposure) {
        this.exposure = exposure;
    }

    public BigDecimal getActivity() {
        return activity;
    }

    public void setActivity(BigDecimal activity) {
        this.activity = activity;
    }

    public BigDecimal getPrinted() {
        return printed;
    }

    public void setPrinted(BigDecimal printed) {
        this.printed = printed;
    }

    public BigDecimal getInquiries() {
        return inquiries;
    }

    public void setInquiries(BigDecimal inquiries) {
        this.inquiries = inquiries;
    }
}
