package org.richfaces.chart;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean(name = "iter")
@RequestScoped
public class IterationBean implements Serializable {
    
    List<Point> points;
    
    @PostConstruct
    public void init(){
        
        points = new LinkedList<Point>();
        points.add(new Point(4,6));
        points.add(new Point(5,2));
        points.add(new Point(6,5));
        points.add(new Point(7,8));
                
    }

    public List<Point> getPoints() {
        return points;
    }

    
    
    public class Point{
        private Number x;
        private Number y;

        public Point(Number x, Number y) {
            this.x = x;
            this.y = y;
        }

        public Number getX() {
            return x;
        }

        public Number getY() {
            return y;
        }
        
    }

    
    
    
    
    
    
    
    

}
