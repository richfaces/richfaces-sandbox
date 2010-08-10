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

import java.io.Serializable;

/**
 * That is intended for internal use
 * 
 * @author Nick Belaevski
 */
public class StackingTreeModelKey<T> implements Serializable, ComplexTreeRowKey {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6821854350257816571L;
	protected T modelKey;
	protected String modelId;

	public StackingTreeModelKey(String modelId, T modelKey) {
		super();
		this.modelId = modelId;
		this.modelKey = modelKey;
	}

	public String toString() {
		return this.modelId + AbstractTreeDataModel.SEPARATOR + this.modelKey;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((modelId == null) ? 0 : modelId.hashCode());
		result = prime * result
				+ ((modelKey == null) ? 0 : modelKey.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final StackingTreeModelKey other = (StackingTreeModelKey) obj;
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		if (modelKey == null) {
			if (other.modelKey != null)
				return false;
		} else if (!modelKey.equals(other.modelKey))
			return false;
		return true;
	}

	public String getModelId() {
		return modelId;
	}
	
	public Object getModelKey() {
		return modelKey;
	}
	
	public int getKeySegmentsCount() {
		return 2;
	}
	
	public Object getKeySegment(int i) {
		switch (i) {
		case 0:
			return modelId;
		case 1:
			return modelKey;
			
		default:
			throw new IllegalArgumentException(String.valueOf(i));
		}
	}
}