package com.example.chtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.richfaces.sandbox.chart.PlotClickEvent;
import org.richfaces.sandbox.chart.model.ChartDataModel;
import org.richfaces.sandbox.chart.model.DateChartDataModel;
import org.richfaces.sandbox.chart.model.NumberChartDataModel;
import org.richfaces.sandbox.chart.model.StringChartDataModel;


@ManagedBean(name = "bean")
@RequestScoped
public class Bean implements Serializable {
    
    
    List<Country> countries;
     
    @PostConstruct
    public void init(){
        
        Country usa = new Country("USA");
        usa.put(1990,19.1);
        usa.put(1991,18.9);
        usa.put(1992,18.6);
        usa.put(1993,19.5);
        usa.put(1994,19.5);
        usa.put(1995,19.3);
        usa.put(1996,19.4);
        usa.put(1997,19.7);
        usa.put(1998,19.5);
        usa.put(1999,19.5);
        usa.put(2000,20.0);
        
        Country china = new Country("China");
        china.put(1990,2.2);
        china.put(1991,2.2);
        china.put(1992,2.3);
        china.put(1993,2.4);
        china.put(1994,2.6);
        china.put(1995,2.7);
        china.put(1996,2.8);
        china.put(1997,2.8);
        china.put(1998,2.7);
        china.put(1999,2.6);
        china.put(2000,2.7);
        
        
        Country japan = new Country("Japan");
        japan.put(1990, 9.4);
        japan.put(1991, 9.4);
        japan.put(1992, 9.5);
        japan.put(1993, 9.4);
        japan.put(1994, 9.9);
        japan.put(1995, 9.9);
        japan.put(1996, 10.1);
        japan.put(1997, 10.1);
        japan.put(1998, 9.7);
        japan.put(1999, 9.5);
        japan.put(2000, 9.7);
        
        Country russia = new Country("Russia");
        russia.put(1992,14);
        russia.put(1993,12.8);
        russia.put(1994,10.9);
        russia.put(1995,10.5);
        russia.put(1996,10.4);
        russia.put(1997,10);
        russia.put(1998,9.6);
        russia.put(1999,9.7);
        russia.put(2000,9.8);
        
                
        
        countries = new LinkedList<Country>();
        countries.add(usa);
        countries.add(china);
        countries.add(japan);
        countries.add(russia);
    }

    public List<Country> getCountries() {
        return countries;
    }

    
    
    

 
    
    
    
    
    
    
    
        
    
    
    

}
