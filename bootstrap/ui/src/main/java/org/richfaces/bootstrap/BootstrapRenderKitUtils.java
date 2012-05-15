package org.richfaces.bootstrap;

import java.util.Map;
import java.util.Map.Entry;

import org.richfaces.renderkit.RenderKitUtils;

import com.google.common.collect.Maps;

public final class BootstrapRenderKitUtils {

    private static String RICHFACES_BOOTSTRAP_EVENT_NAMESPACE = ".rfBsEvent";

    public static String toEventMap(Map<String, Object> attributeMap) {
        Map<String, Object> eventMap = Maps.newHashMap();
        for (Entry<String, Object> entry : attributeMap.entrySet()) {
            if (entry.getKey().startsWith("on")) {
                String eventName = entry.getKey().substring(2) + RICHFACES_BOOTSTRAP_EVENT_NAMESPACE;
                eventMap.put(eventName, entry.getValue());
            }
        }
        return RenderKitUtils.toScriptArgs(eventMap);
    }
}
