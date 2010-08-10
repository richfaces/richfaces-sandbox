/**
 * 
 */
package org.ajax4jsf;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 * @author asmirnov
 *
 */
@Name("stressBean") @Scope(ScopeType.SESSION)
public class StressBean {
	
	private int counter;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public String next() {
		counter++;
		return null;
	}
}
