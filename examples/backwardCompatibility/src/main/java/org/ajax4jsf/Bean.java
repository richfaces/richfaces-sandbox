/**
 * License Agreement.
 *
 * Rich Faces - Natural Ajax for Java Server Faces (JSF)
 *
 * Copyright (C) 2007 Exadel, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */

package org.ajax4jsf;

import java.io.Serializable;
import java.util.Date;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author $Autor$
 *
 */
@Name("seamBean") @Scope(ScopeType.CONVERSATION) public class Bean implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4209339000953631111L;
    private String text;
    private String text2;
    
    private Date date;
    
    private Date date1 = new Date();

    /**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the date1
	 */
	public Date getDate1() {
		return date1;
	}

	/**
	 * @param date1 the date1 to set
	 */
	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	/**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }
    
    @Begin
    public String start() {
	return "start";
    }
	
    @End
    public String stop() {
	return "stop";
    }
    
    public String clearSession(){
	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	HttpSession session = (HttpSession) externalContext.getSession(false);
	if(null != session){
	    session.setMaxInactiveInterval(5);
	}
	return null;
    }

	/**
	 * @return the text2
	 */
	public String getText2() {
		return text2;
	}

	/**
	 * @param text2 the text2 to set
	 */
	public void setText2(String text2) {
		this.text2 = text2;
	}
}