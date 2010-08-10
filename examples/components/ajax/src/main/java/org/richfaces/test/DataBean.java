/**
 * 
 */
package org.richfaces.test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author asmirnov
 *
 */
public class DataBean {
	
	private final List<DataItem> data;
	
	public DataBean() {
		data = new ArrayList<DataItem>(10);
		for(int i=0;i<10;i++){
			DataItem item = new DataItem();
			item.setPrice(i);
			item.setName("Name"+i);
			ArrayList<DataItem> items = new ArrayList<DataItem>(10);
			for(int j=0;j<10;j++){
				String name = "Item "+j+" of "+i;
				DataItem childItem = new DataItem();
				childItem.setName(name);
				items.add(childItem);
			}
			item.setItems(items);
			data.add(item);
		}
	}

	/**
	 * @return the data
	 */
	public List<DataItem> getData() {
		return data;
	}
	
}
