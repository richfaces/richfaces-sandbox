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

package org.ajax4jsf.renderkit.html;

import org.ajax4jsf.renderkit.AjaxCommandRendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * Since <code>UIAjaxRegion</code> dont render itself ( only set apropriate event to parent component )
 * only <code>decode()</code> method implemented.
 * In case of Ajax Request caused by event for a given component
 * send action event.
 * @author shura (latest modification by $Author: alexsmirnov $)
 * @version $Revision: 1.1.2.1 $ $Date: 2007/02/01 15:31:24 $
 *
 */
public class AjaxSupportRenderer extends AjaxCommandRendererBase
{
    private static final Log log = LogFactory.getLog(AjaxSupportRenderer.class);

    /* (non-Javadoc)
     * @see javax.faces.render.Renderer#getRendersChildren()
     */
    public boolean getRendersChildren()
    {
        
        return false;
    }

	protected Class getComponentClass() {
		// TODO Auto-generated method stub
		return org.ajax4jsf.component.UIAjaxSupport.class;
	}

}
