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
package org.ircclient.listeners;

import java.text.MessageFormat;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.servlet.http.HttpServletRequest;

import org.ircclient.controller.ChatBean;
import org.richfaces.application.push.EventAbortedException;
import org.richfaces.application.push.Session;
import org.richfaces.application.push.SessionPreSubscriptionEvent;
import org.richfaces.application.push.SessionSubscriptionEvent;
import org.richfaces.application.push.SessionTopicListener;
import org.richfaces.application.push.SessionUnsubscriptionEvent;
import org.richfaces.application.push.Topic;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.application.push.impl.DefaultMessageDataSerializer;

/**
 * @author Nick Belaevski
 * 
 */
public class TopicsInitializer implements SystemEventListener {

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        TopicsContext topicsContext = TopicsContext.lookup();

        Topic topic = topicsContext.getOrCreateTopic(new TopicKey("chat"));

        topic.setMessageDataSerializer(DefaultMessageDataSerializer.instance());

        topic.addTopicListener(new SessionTopicListener() {

            public void processUnsubscriptionEvent(SessionUnsubscriptionEvent event) throws EventAbortedException {
                TopicKey topicKey = event.getTopicKey();
                Session session = event.getSession();
                System.out.println(MessageFormat.format("Session {0} disconnected from {1}", session.getId(),
                    topicKey.getTopicAddress()));
            }

            public void processSubscriptionEvent(SessionSubscriptionEvent event) throws EventAbortedException {
                TopicKey topicKey = event.getTopicKey();
                Session session = event.getSession();

                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpServletRequest hsr = (HttpServletRequest) facesContext.getExternalContext().getRequest();

                System.out.println(MessageFormat.format("Session {0} connected to {1} from {2}", session.getId(),
                    topicKey.getTopicAddress(), hsr.getRemoteAddr()));
            }

            public void processPreSubscriptionEvent(SessionPreSubscriptionEvent event) throws EventAbortedException {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                ChatBean chatBean = (ChatBean) externalContext.getSessionMap().get("chatBean");
                if (chatBean == null || !(chatBean.isConnected())) {
                    throw new EventAbortedException("We are not connected to IRC");
                }
            }
        });
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

}
