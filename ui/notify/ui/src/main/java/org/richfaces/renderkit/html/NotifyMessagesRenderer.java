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

package org.richfaces.renderkit.html;

import org.richfaces.cdk.annotations.JsfRenderer;
import org.richfaces.component.AbstractNotify;
import org.richfaces.component.AbstractNotifyMessages;
import org.richfaces.renderkit.HtmlConstants;
import org.richfaces.renderkit.util.RendererUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

@JsfRenderer(family = AbstractNotifyMessages.COMPONENT_FAMILY, type = NotifyMessagesRenderer.RENDERER_TYPE)
public class NotifyMessagesRenderer extends NotifyRenderer {

    public static final String RENDERER_TYPE = "org.richfaces.NotifyMessagesRenderer";

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        AbstractNotifyMessages messagesComponent = (AbstractNotifyMessages) component;
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement(HtmlConstants.DIV_ELEM, null);
        writer.writeAttribute(HtmlConstants.ID_ATTRIBUTE, RendererUtils.getInstance().clientId(context, component), "type");
        Integer delay = messagesComponent.getDelay();
        if (delay == null) {
            delay = 0;
        }
        Integer interval = messagesComponent.getInterval();
        if (interval == null) {
            interval = 0;
        }

        Iterator<FacesMessage> messages = messagesComponent.isGlobalOnly()
            ? context.getMessages(null) : context.getMessages();
        while (messages.hasNext()) {
            FacesMessage msg = messages.next();
            AbstractNotify notify = (AbstractNotify) context.getApplication()
                .createComponent(AbstractNotify.COMPONENT_TYPE);
            notify.setAnimationSpeed(messagesComponent.getAnimationSpeed());
            notify.setAppearAnimation(messagesComponent.getAppearAnimation());
            notify.setDelay(delay);
            notify.setHideAnimation(messagesComponent.getHideAnimation());
            notify.setNonblocking(messagesComponent.isNonblocking());
            notify.setNonblockingOpacity(messagesComponent.getNonblockingOpacity());
            notify.setShowCloseButton(messagesComponent.isShowCloseButton());
            notify.setShowHistory(messagesComponent.isShowHistory());
            notify.setShowShadow(messagesComponent.isShowShadow());
            notify.setStack(messagesComponent.getStack());
            notify.setStayTime(messagesComponent.getStayTime());
            notify.setSticky(messagesComponent.isSticky());
            if (messagesComponent.isShowSummary()) {
                notify.setTitle(msg.getSummary());
            }
            if (messagesComponent.isShowDetail() && msg.getDetail() != null && !msg.getDetail().equals(msg.getSummary())) {
                notify.setText(msg.getDetail());
            }
            String styleClass = messagesComponent.getStyleClass();
            if (styleClass == null) {
                styleClass = "";
            }
            if (FacesMessage.SEVERITY_INFO.equals(msg.getSeverity())) {
                styleClass += " rf-ny-info";
            } else if (FacesMessage.SEVERITY_WARN.equals(msg.getSeverity())) {
                styleClass += " rf-ny-warn";
            } else if (FacesMessage.SEVERITY_ERROR.equals(msg.getSeverity())) {
                styleClass += " rf-ny-error";
            } else if (FacesMessage.SEVERITY_FATAL.equals(msg.getSeverity())) {
                styleClass += " rf-ny-fatal";
            }
            styleClass = styleClass.trim();
            notify.setStyleClass(styleClass);
            notify.encodeAll(context);
            delay += interval;
            msg.rendered();
        }
        writer.endElement(HtmlConstants.DIV_ELEM);
    }
}
