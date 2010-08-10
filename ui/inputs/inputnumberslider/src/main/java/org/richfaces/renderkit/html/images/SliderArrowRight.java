/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.richfaces.renderkit.html.images;

import java.awt.Dimension;
import java.awt.Graphics2D;

/**
 * @author Konstantin Mishin
 * 
 */
public class SliderArrowRight extends SliderArrowBase {
    
    @Override
    public void paint(Graphics2D graphics2d, Dimension dimension) {
        super.paint(graphics2d, dimension);
        graphics2d.drawLine(2, 0, 2, 6);
        graphics2d.drawLine(3, 1, 3, 5);
        graphics2d.drawLine(4, 2, 4, 4);
        graphics2d.drawLine(5, 3, 5, 3);
    }
}
