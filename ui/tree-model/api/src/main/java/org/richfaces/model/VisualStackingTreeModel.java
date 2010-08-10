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

import javax.faces.component.UIComponent;

/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski
 *         mailto:nbelaevski@exadel.com
 *         created 15.08.2007
 *
 */
public class VisualStackingTreeModel extends StackingTreeModel implements TreeModelVisualComponentProvider {
	private UIComponent component;

	public VisualStackingTreeModel(UIComponent component) {
		super();

		this.component = component;
	}

	public VisualStackingTreeModel(String id, String var,
			StackingTreeModelDataProvider dataProvider, UIComponent component) {
		super(id, var, dataProvider);

		this.component = component;
	}
	
	public UIComponent getComponent() {
		if (this.component != null) {
			return this.component;
		}

		StackingTreeModel currentModel = getCurrentModel();
		if (currentModel != null && currentModel != this) {
			return ((TreeModelVisualComponentProvider) currentModel).getComponent();
		}

		return null;
	}
}
