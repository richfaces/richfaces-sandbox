package org.richfaces.demo.push;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ajax4jsf.event.PushEventListener;

@ManagedBean(name = "choicesBean")
@SessionScoped
public class ChoicesBean implements Runnable, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 4734010583616555915L;

    transient PushEventListener listener;
    
    private boolean enabled = false;
    private List<Choice> choices;
    private List<Choice> lastVotes;
    private Date startDate;
    private transient Thread thread;
    private String updateInfo;

    public ChoicesBean() {
        choices = new ArrayList<Choice>();
        lastVotes = new ArrayList<Choice>();
        choices.add(new Choice("Orange"));
        choices.add(new Choice("Pineapple"));
        choices.add(new Choice("Banana"));
        choices.add(new Choice("Kiwifruit"));
        choices.add(new Choice("Apple"));
        lastVotes.add(new Choice("Orange"));
        lastVotes.add(new Choice("Pineapple"));
        lastVotes.add(new Choice("Banana"));
        lastVotes.add(new Choice("Kiwifruit"));
        lastVotes.add(new Choice("Apple"));
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public void addListener(EventListener listener) {
        if (this.listener != listener) {
            this.listener = (PushEventListener) listener;
        }
    }

    public Date getTimeStamp() {
        return new Date();
    }

    public synchronized void start() {
        if (thread == null) {
            setStartDate(new Date());
            setEnabled(true);
            thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    public synchronized void stop() {
        if (thread != null) {
            setStartDate(null);
            setEnabled(false);
            thread = null;
        }
    }

    public static int rand(int lo, int hi) {
        Random rn2 = new Random();
        int n = hi - lo + 1;
        int i = rn2.nextInt() % n;

        if (i < 0) {
            i = -i;
        }

        return lo + i;
    }

    public void run() {
        while (thread != null) {
            try {
                if (((new Date()).getTime() - startDate.getTime()) >= 60000) {
                    stop();
                }

                // changing votes count
                for (Choice choice : lastVotes) {
                    choice.setVotesCount(rand(0, 3));
                }

                // System.out.println("New Event!");
                listener.onEvent(new EventObject(this));
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void processUpdates() {
        for (Choice choice : lastVotes) {
            if (choice.getVotesCount() > 0) {
                int index = lastVotes.indexOf(choice);

                choices.get(index).increment(choice.getVotesCount());
            }
        }

        updateInfo = "[ ";

        for (Choice choice : lastVotes) {
            updateInfo += choice.getVotesCount() + " ";
        }

        updateInfo += "] ";

        // System.out.println("ChoicesBean.processUpdates()");
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isEnabled() {

        // System.out.println("ChoicesBean.isEnabled()");
        // System.out.println(enabled);
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public List<Choice> getLastVotes() {
        return lastVotes;
    }
}
