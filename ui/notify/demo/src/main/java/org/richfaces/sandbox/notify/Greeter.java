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

package org.richfaces.sandbox.notify;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class Greeter implements Serializable {

    private String greeting;
    private boolean nonblocking;
    private boolean showDetail;
    private boolean showSummary = true;
    private boolean sticky;
    private boolean showHistory = true;
    private boolean showShadow = true;
    private boolean showCloseButton = true;
    private Integer stayTime = 3000;
    private Integer animationSpeed = 100;
    private Integer interval = 800;
    private Integer delay = 0;
    private int messagesCount = 3;
    private Double nonblockingOpacity = .3;
    private String appearAnimation = "fade";
    private String hideAnimation = "show";
    private String stackDir1 = "down";
    private String stackDir2 = "left";
    private String stackPush = "bottom";
    private String styleClass;
    private String stackStyleClass;
    private String title = "Sample title";
    private String text = "Sample text, not too short, but not too long.";

    public void sayHello() {
        for (int i = 0; i < getMessagesCount(); i++) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, i + ". Hello", greeting));
        }
    }

    public void warnMe() {
        for (int i = 0; i < getMessagesCount(); i++) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, i + ". Stop!", greeting));
        }
    }

    public void sayError() {
        for (int i = 0; i < getMessagesCount(); i++) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, i + ". This is outrage", greeting));
        }
    }

    public void sayFatal() {
        for (int i = 0; i < getMessagesCount(); i++) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, i + ". Fatality", greeting));
        }
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public boolean isNonblocking() {
        return nonblocking;
    }

    public void setNonblocking(boolean nonblocking) {
        this.nonblocking = nonblocking;
    }

    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
    }

    public void setShowSummary(boolean showSummary) {
        this.showSummary = showSummary;
    }

    public boolean isSticky() {
        return sticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public Integer getStayTime() {
        return stayTime;
    }

    public void setStayTime(Integer stayTime) {
        this.stayTime = stayTime;
    }

    public boolean isShowSummary() {
        return showSummary;
    }

    public String getAppearAnimation() {
        return appearAnimation;
    }

    public void setAppearAnimation(String appearAnimation) {
        this.appearAnimation = appearAnimation;
    }

    public boolean isShowHistory() {
        return showHistory;
    }

    public void setShowHistory(boolean showHistory) {
        this.showHistory = showHistory;
    }

    public boolean isShowShadow() {
        return showShadow;
    }

    public void setShowShadow(boolean showShadow) {
        this.showShadow = showShadow;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
    }

    public Double getNonblockingOpacity() {
        return nonblockingOpacity;
    }

    public void setNonblockingOpacity(Double nonblockingOpacity) {
        this.nonblockingOpacity = nonblockingOpacity;
    }

    public String getStackDir1() {
        return stackDir1;
    }

    public void setStackDir1(String stackDir1) {
        this.stackDir1 = stackDir1;
    }

    public String getStackDir2() {
        return stackDir2;
    }

    public void setStackDir2(String stackDir2) {
        this.stackDir2 = stackDir2;
    }

    public String getStackPush() {
        return stackPush;
    }

    public void setStackPush(String stackPush) {
        this.stackPush = stackPush;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public Integer getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(Integer animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public String getHideAnimation() {
        return hideAnimation;
    }

    public void setHideAnimation(String hideAnimation) {
        this.hideAnimation = hideAnimation;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public String getStackStyleClass() {
        return stackStyleClass;
    }

    public void setStackStyleClass(String stackStyleClass) {
        this.stackStyleClass = stackStyleClass;
    }

    public int getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }
}
