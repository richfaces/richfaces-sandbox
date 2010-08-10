/**
 * 
 */
package org.richfaces.model.entity;

/**
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 30.07.2007
 *
 */
public class Named {
	private String name;
	private int tag;
	public String getName() {
		return name;
	}
	public int getTag() {
		return tag;
	}
	public Named(String name, int tag) {
		super();
		this.name = name;
		this.tag = tag;
	}
	
}
