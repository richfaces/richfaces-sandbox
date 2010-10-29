/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

/**
 * @author Maksim Kaszynski
 * 
 */
public class DisabledCalendarIcon extends CalendarIcon {

    @Override
    protected BufferedImage createImage(int width, int height) {
        ColorSpace gsColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        ComponentColorModel ccm = new ComponentColorModel(gsColorSpace, true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        WritableRaster raster = ccm.createCompatibleWritableRaster(width, height);

        return new BufferedImage(ccm, raster, ccm.isAlphaPremultiplied(), null);
    }

}
