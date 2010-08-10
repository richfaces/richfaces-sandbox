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
package org.richfaces.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;


/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski mailto:nbelaevski@exadel.com created 25.07.2007
 * 
 */
public abstract class StackingTreeModelProvider extends UIComponentBase {

	public abstract Object getNodes();
	public abstract void setNodes(Object nodes);

	public Object getData() {
		return getNodes();
	}
	
	protected abstract StackingTreeModel createStackingTreeModel();

	private List<StackingTreeModel> createChildModelsList() {
		List<StackingTreeModel> childModels = new ArrayList<StackingTreeModel>();

		if (getChildCount() > 0) {
			Iterator<UIComponent> children = getChildren().iterator();
			while (children.hasNext()) {
				UIComponent component = children.next();
				if (component instanceof StackingTreeModelProvider) {
					StackingTreeModelProvider provider = (StackingTreeModelProvider) component;
					childModels.add(provider.getStackingModel());
				}
			}
		}
		
		return childModels;
	}
	
	protected void addChildModels(StackingTreeModel model, List<StackingTreeModel> childModels) {
		for (StackingTreeModel childModel : childModels) {
			model.addStackingModel(childModel);
		}
	}
	
	public StackingTreeModel getStackingModel() {
		StackingTreeModel stackingTreeModel = createStackingTreeModel();
		
		addChildModels(stackingTreeModel, createChildModelsList());
		
		return stackingTreeModel;
	}
}
