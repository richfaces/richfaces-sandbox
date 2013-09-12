package com.example.chtest;

import java.util.LinkedList;
import java.util.List;




public class Country{
    private String name;
    //CO2 production year-> metric tons per capita
    private List<Record> data;
    
    public Country(String name){
        this.name = name;
        this.data = new LinkedList<Record>();
    }
    public void put(int year, double tons){
        this.data.add(new Record(year, tons));
    }
    
    public List<Record> getData() {
        return data;
    }

    public String getName() {
        return name;
    }
    
    public class Record{
        private int year;
        private double tons;

        public Record(int year, double tons) {
            this.year = year;
            this.tons = tons;
        }

        public double getTons() {
            return tons;
        }

        public int getYear() {
            return year;
        }
        
    }
            
    
}
