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

import java.util.HashSet;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.naming.InitialContext;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.netty.NettyAcceptorFactory;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;

/**
 * @author Nick Belaevski
 * 
 */
public class HornetQInitializer implements SystemEventListener {

    private JMSServerManager serverManager;

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        if (event instanceof PostConstructApplicationEvent) {
            try {
                startHornetQ();
            } catch (Exception e) {
                throw new AbortProcessingException(e);
            }

            Application application = FacesContext.getCurrentInstance().getApplication();
            application.subscribeToEvent(PreDestroyApplicationEvent.class, this);
        } else {
            try {
                stopHornetQ();
            } catch (Exception e) {
                throw new AbortProcessingException(e);
            }
        }
    }

    /**
     * @throws Exception
     * 
     */
    private void stopHornetQ() throws Exception {
        serverManager.stop();
        serverManager = null;
    }

    /**
     * @throws Exception
     * 
     */
    private void startHornetQ() throws Exception {
        // Step 2. Create the Configuration, and set the properties accordingly
        Configuration configuration = new ConfigurationImpl();
        configuration.setPersistenceEnabled(false);
        configuration.setSecurityEnabled(false);

        TransportConfiguration transpConf = new TransportConfiguration(NettyAcceptorFactory.class.getName());

        HashSet<TransportConfiguration> setTransp = new HashSet<TransportConfiguration>();
        setTransp.add(transpConf);

        configuration.setAcceptorConfigurations(setTransp);

        // Step 3. Create and start the server
        HornetQServer server = HornetQServers.newHornetQServer(configuration);

        serverManager = new JMSServerManagerImpl(server);

        // if you want to use JNDI, simple inject a context here or don't call this method and make sure the JNDI
        // parameters are set.
        serverManager.setContext(new InitialContext());
        serverManager.start();

        serverManager.createConnectionFactory("ConnectionFactory", new TransportConfiguration(
            NettyConnectorFactory.class.getName()), "ConnectionFactory");

        serverManager.createTopic(false, "chat", "/topic/chat");
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

}
