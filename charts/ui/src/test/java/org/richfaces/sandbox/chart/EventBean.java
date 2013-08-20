package org.richfaces.sandbox.chart;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.sandbox.chart.PlotClickEvent;
import org.richfaces.sandbox.chart.model.ChartDataModel;
import org.richfaces.sandbox.chart.model.StringChartDataModel;


@ManagedBean
@ViewScoped
public class EventBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String msg;
    List<Country> countries;
    StringChartDataModel cars;
    List<GDPRecord> gdp;
    
    
    @PostConstruct
    public void init(){
        msg="no server-side event";
        
        
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
        
        cars = new StringChartDataModel(ChartDataModel.ChartType.bar);
        cars.put("San Marino", 1263);
        cars.put("United States", 797);
        cars.put("Denmark", 480);
        cars.put("Vietnam", 13);
        cars.put("Croatia",380);
        
        gdp = new LinkedList<GDPRecord>();
        gdp.add(new GDPRecord("United States", 188217, 2995787, 12500746));
        gdp.add(new GDPRecord("China", 830931, 3726848, 3669259));
        gdp.add(new GDPRecord("Japan", 71568, 1640091, 4258274));
        gdp.add(new GDPRecord("Germany", 27205, 955563, 2417812));
        
        
    }

    

   

    public List<Country> getCountries() {
        return countries;
    }

    public StringChartDataModel getCars() {
        return cars;
    }

    public List<GDPRecord> getGdp() {
        return gdp;
    }
    
    public void handler(PlotClickEvent event){
        msg="Server's speaking:"+event.toString();
    }

    public String getMsg() {
        return msg;
    }
    
    
    
    
    
    public class Country{
        private String name;
        //CO2 production year-> metric tons per capita
        private List<Country.Record> data;
        
        public Country(String name){
            this.name = name;
            this.data = new LinkedList<Country.Record>();
        }
        public void put(int year, double tons){
            this.data.add(new Country.Record(year, tons));
        }
        
        public List getData() {
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
    
    public class GDPRecord{
        private String state;
        private int agricult;
        private int industry;
        private int service;

        public GDPRecord(String country, int agricult, int industry, int service) {
            this.state = country;
            this.agricult = agricult;
            this.industry = industry;
            this.service = service;
        }

        public String getState() {
            return state;
        }

        public int getAgricult() {
            return agricult;
        }

        public int getIndustry() {
            return industry;
        }

        public int getService() {
            return service;
        }
        
        
    }
   
    
    
    
    
    
    
    
    

}
