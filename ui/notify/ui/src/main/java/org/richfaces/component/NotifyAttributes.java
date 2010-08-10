/*
 * JBoss, Home of Professional Open Source
 * Copyright , Red Hat, Inc. and individual contributors
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

package org.richfaces.component;

import org.richfaces.cdk.annotations.Attribute;

public interface NotifyAttributes {

    @Attribute
    boolean isSticky();

    void setSticky(boolean sticky);

    @Attribute
    Integer getStayTime();

    void setStayTime(Integer time);

    @Attribute
    Integer getDelay();

    void setDelay(Integer delay);

    @Attribute
    String getStyleClass();

    void setStyleClass(String styleClass);

    @Attribute
    String getAppearAnimation();

    void setAppearAnimation(String appearAnimation);

    @Attribute
    String getHideAnimation();

    void setHideAnimation(String hideAnimation);

    @Attribute
    Integer getAnimationSpeed();

    void setAnimationSpeed(Integer animationSpeed);

    @Attribute
    boolean isShowHistory();

    void setShowHistory(boolean showHistory);

    @Attribute
    boolean isNonblocking();

    void setNonblocking(boolean nonblocking);

    @Attribute
    boolean isShowShadow();

    void setShowShadow(boolean showShadow);

    @Attribute
    boolean isShowCloseButton();

    void setShowCloseButton(boolean showCloseButton);

    @Attribute
    Double getNonblockingOpacity();

    void setNonblockingOpacity(Double nonblockingOpacity);

    @Attribute
    String getStack();

    void setStack(String stack);
}
