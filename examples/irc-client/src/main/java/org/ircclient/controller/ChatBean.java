package org.ircclient.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import org.richfaces.application.push.MessageException;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.log.LogFactory;
import org.richfaces.log.Logger;

@ManagedBean
@SessionScoped
public class ChatBean extends PircBot implements Serializable {

    private static final long serialVersionUID = -4945680041914388092L;
    private static final String SERVER_URL = "irc.freenode.org";
    private static final int SERVER_PORT = 6667;
    private static final String CHANNEL_PREFIX = "#";
    private static final String DEFAULT_CHANNEL = "richfaces";
    private static final Logger LOGGER = LogFactory.getLogger(ChatBean.class);

    private String channelName;
    private String message;
    private transient TopicsContext topicsContext;

    public ChatBean() {
        LOGGER.info("TOPICS CONTEXT INITIALIZED");
        topicsContext = TopicsContext.lookup();
    }

    public void connect() {
        try {
            this.connect(SERVER_URL, SERVER_PORT);
            this.joinChannel(CHANNEL_PREFIX + DEFAULT_CHANNEL);
            channelName = DEFAULT_CHANNEL;
        } catch (NickAlreadyInUseException e) {
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, this.getName() + " nick already in use", this.getName()
                    + " nick already in use"));
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sorry, server unresponsible. Try again later.",
                    "Sorry, server unresponsible. Try again later."));
        } catch (IrcException e) {
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Sorry, we encountered IRC services problems. Try again later.",
                    "Sorry, we encountered IRC services problems. Try again later."));
        }
    }

    public void leave(){
        this.disconnect();
    }
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        String channelName = channel.replace(CHANNEL_PREFIX, "");
        try {
            topicsContext.publish(new TopicKey("chat", channelName),
                MessageFormat.format("{0,time,medium} {1}: {2}", new Date(), sender, message));
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onUserList(String channel, User[] users) {
        try {
            topicsContext.publish(new TopicKey("chat", channelName + "List"), null);
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
        try {
            topicsContext.publish(new TopicKey("chat", channelName + "List"), null);
            topicsContext.publish(new TopicKey("chat", channelName),
                MessageFormat.format("{0,time,medium} {1}: {2}", new Date(), sender, "joined channel"));
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
        try {
            topicsContext.publish(new TopicKey("chat", channelName + "List"), null);
            topicsContext.publish(new TopicKey("chat", channelName),
                MessageFormat.format("{0,time,medium} {1}: {2}", new Date(), sender, "left channel"));
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
        try {
            topicsContext.publish(new TopicKey("chat", channelName + "List"), null);
            topicsContext.publish(new TopicKey("chat", channelName),
                MessageFormat.format("{0,time,medium} {1}", new Date(), oldNick + " changed nick to " + newNick));
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
        try {
            topicsContext.publish(new TopicKey("chat", channelName + "List"), null);
            topicsContext.publish(new TopicKey("chat", channelName),
                MessageFormat.format("{0,time,medium} {1}: {2} {3}", new Date(), sourceNick, "joined channel", reason));
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public User[] getUsers() {
        return this.getUsers(CHANNEL_PREFIX + channelName);
    }

    public void send() {
        this.sendMessage(CHANNEL_PREFIX + channelName, message);
        try {
            topicsContext.publish(new TopicKey("chat", channelName),
                MessageFormat.format("{0,time,medium} {1}: {2}", new Date(), this.getName(), message));
        } catch (MessageException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    public void changeNick(ValueChangeEvent event) {
        this.changeNick((String) event.getNewValue());
    }

    public String getServerName() {
        return SERVER_URL;
    }

    public void setUserName(String userName) {
        this.setName(userName);
    }

    public String getUserName() {
        if (this.getName().equalsIgnoreCase("PircBot")) {
            return "";
        } else {
            return this.getName();
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
