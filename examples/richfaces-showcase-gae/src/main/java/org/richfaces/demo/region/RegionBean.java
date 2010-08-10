package org.richfaces.demo.region;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.richfaces.demo.common.UserBean;

@ManagedBean
@ViewScoped
public class RegionBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3844974441732156513L;
    private UserBean user1 = new UserBean();
    private UserBean user2 = new UserBean();
    
    public UserBean getUser1() {
        return user1;
    }
    
    public UserBean getUser2() {
        return user2;
    }
    
    public void setUser1(UserBean user1) {
        this.user1 = user1;
    }
    
    public void setUser2(UserBean user2) {
        this.user2 = user2;
    }
  
}
