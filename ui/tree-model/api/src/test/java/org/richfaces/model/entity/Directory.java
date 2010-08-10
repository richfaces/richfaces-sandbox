/**
 * 
 */
package org.richfaces.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 30.07.2007
 *
 */
public class Directory extends Named {

	public Directory(String name, int tag) {
		super(name, tag);
	}

	private List files = new ArrayList();
	
	public List getFiles() {
		return files;
	}
	
	public void addFile(File file) {
		this.files.add(file);
	}
}
