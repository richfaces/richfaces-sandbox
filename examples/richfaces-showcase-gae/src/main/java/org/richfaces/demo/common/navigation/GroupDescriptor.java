package org.richfaces.demo.common.navigation;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class GroupDescriptor extends BaseDescriptor {

    private static final long serialVersionUID = -3481702232804120885L;

    private List<DemoDescriptor> demos;

    @XmlElementWrapper(name="demos")
    @XmlElement(name="demo")
    public List<DemoDescriptor> getDemos() {
        return demos;
    }

    public void setDemos(List<DemoDescriptor> demos) {
        this.demos = demos;
    }

}
