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
@Name("menuAction") @Scope(ScopeType.SESSION)
public class MenuAction {
	
	private String _selectedTab=null;

	/**
	 * @return the selectedTab
	 */
	public String getSelectedTab() {
		return _selectedTab;
	}

	/**
	 * @param selectedTab the selectedTab to set
	 */
	public void setSelectedTab(String selectedTab) {
		_selectedTab = selectedTab;
	}
	
	

}
