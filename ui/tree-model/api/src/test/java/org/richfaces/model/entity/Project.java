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
public class Project extends Named {
	public Project(String name, int tag) {
		super(name, tag);
	}

	private List directories = new ArrayList();
	
	public List getDirectories() {
		return directories;
	}
	
	public void addDirectory(Directory directory) {
		directories.add(directory);
	}
}
