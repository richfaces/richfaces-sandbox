package org.richfaces.demo.tables.model.expenses;

public class ExpenseReportRecordItem {

    private String city;
    private String day;
    private double meals;
    private double hotels;
    private double transport;

    public ExpenseReportRecordItem(String day, double meals, double hotels, double transport, String city) {
        this.city = city;
        this.day = day;
        this.meals = meals;
        this.hotels = hotels;
        this.transport = transport;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getHotels() {
        return hotels;
    }

    public void setHotels(double hotels) {
        this.hotels = hotels;
    }

    public double getMeals() {
        return meals;
    }

    public void setMeals(double meals) {
        this.meals = meals;
    }

    public double getTransport() {
        return transport;
    }

    public void setTransport(double transport) {
        this.transport = transport;
    }

    public double getTotal() {
        return meals + hotels + transport;
    }
}
