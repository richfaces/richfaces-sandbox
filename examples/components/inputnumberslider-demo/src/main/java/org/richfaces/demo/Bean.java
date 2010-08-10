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
package org.richfaces.demo;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.richfaces.renderkit.html.PositionType;

@ManagedBean(name = "bean")
@SessionScoped
public class Bean {

    private static final String[] SKINS = {"blueSky", "deepMarine", "emeraldTown", "NULL", "ruby", "classic",
        "DEFAULT", "japanCherry", "plain", "wine" };
    private String skin = "classic";
    
    private double value;
    private String accesskey;
    private int delay = 200;
    private boolean disabled = false;
    private boolean enableManualInput = true;
    private PositionType inputPosition = PositionType.right;
    private int inputSize = 3;
    private double maxValue = 100;
    private double minValue = 0;
    private boolean showArrows = false;
    private boolean showBoundaryValues = true;
    private boolean showInput = true;
    private boolean showToolTip = true;
    private double step = 1;
    
    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setAccesskey(String accesskey) {
        this.accesskey = accesskey;
    }

    public String getAccesskey() {
        return accesskey;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setEnableManualInput(boolean enableManualInput) {
        this.enableManualInput = enableManualInput;
    }

    public boolean getEnableManualInput() {
        return enableManualInput;
    }

    public void setInputPosition(PositionType inputPosition) {
        this.inputPosition = inputPosition;
    }

    public PositionType getInputPosition() {
        return inputPosition;
    }

    public PositionType[] getPositionTypes() {
        return PositionType.values();
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    
    public boolean isShowArrows() {
        return showArrows;
    }

    public void setShowArrows(boolean showArrows) {
        this.showArrows = showArrows;
    }

    public boolean isShowBoundaryValues() {
        return showBoundaryValues;
    }

    public void setShowBoundaryValues(boolean showBoundaryValues) {
        this.showBoundaryValues = showBoundaryValues;
    }

    public boolean isShowInput() {
        return showInput;
    }

    public void setShowInput(boolean showInput) {
        this.showInput = showInput;
    }

    public boolean isShowToolTip() {
        return showToolTip;
    }

    public void setShowToolTip(boolean showToolTip) {
        this.showToolTip = showToolTip;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSkin() {
        return skin;
    }
    
    public String[] getSkins() {
        return SKINS;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public int getInputSize() {
        return inputSize;
    }
}
