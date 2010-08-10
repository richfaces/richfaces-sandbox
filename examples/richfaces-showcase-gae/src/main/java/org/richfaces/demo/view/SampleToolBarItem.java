package org.richfaces.demo.view;

public class SampleToolBarItem {
    private String name;
    private String outcome;

    public SampleToolBarItem(String name, String outcome) {
        this.name = name;
        this.outcome = outcome;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

}
