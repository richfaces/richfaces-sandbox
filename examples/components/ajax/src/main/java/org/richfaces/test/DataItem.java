/**
 * 
 */
package org.richfaces.test;

import java.util.List;

/**
 * @author asmirnov
 *
 */
public class DataItem {
	
	private String name;
	
	private int price;
	
	private List<DataItem> items;

	/**
	 * @return the items
	 */
	public List<DataItem> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<DataItem> items) {
		this.items = items;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

}
