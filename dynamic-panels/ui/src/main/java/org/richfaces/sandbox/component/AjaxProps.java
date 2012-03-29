package org.richfaces.sandbox.component;

import org.richfaces.cdk.annotations.Attribute;
import org.richfaces.cdk.annotations.EventName;

public interface AjaxProps {
    @Attribute
    Object getExecute();

    @Attribute
    Object getRender();

    @Attribute(defaultValue = "false")
    boolean isLimitRender();

    @Attribute
    String getStatus();

    @Attribute
    Object getData();

    @Attribute(events = @EventName("begin"))
    String getOnbegin();

    @Attribute(events = @EventName("beforedomupdate"))
    String getOnbeforedomupdate();

    @Attribute(events = @EventName("complete"))
    String getOncomplete();
}
