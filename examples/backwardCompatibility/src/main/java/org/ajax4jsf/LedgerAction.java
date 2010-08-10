/**
 * 
 */
package org.ajax4jsf;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

/**
 * @author asmirnov
 *
 */
@Name("ledgerAction") @Scope(ScopeType.SESSION)
public class LedgerAction {

	private boolean initialized=true;
	
	@DataModel private List<DistributionBean> distributionDetails;

	@Factory("distributionDetails")
	public void initCustomerList() { 
		distributionDetails = new ArrayList<DistributionBean>(30)  ;
		for(int i=0;i<30;i++){
			DistributionBean bean = new DistributionBean();
			bean.setDescription("Product "+i);
			bean.setUtilityCodeDescription("c"+i+i);
			distributionDetails.add(bean);
		}
	} 
	/**
	 * @return the initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialized the initialized to set
	 */
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public String initializeLedgerPage() {
		return null;
	}
	
	public String fetchLedgerAging() {
		return null;
	}
	
	public String initializeRepeaterPage() {
		return null;
	}
}
