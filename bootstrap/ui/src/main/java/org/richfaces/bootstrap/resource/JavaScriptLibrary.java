package org.richfaces.bootstrap.resource;

import org.richfaces.resource.ResourceKey;
import org.richfaces.resource.ResourceLibrary;

import com.google.common.collect.ImmutableList;

public class JavaScriptLibrary implements ResourceLibrary {

    private static final ImmutableList<ResourceKey> BOOTSTRAP_MIN = ImmutableList.of(
            ResourceKey.create("rf-bootstrap-js.js", "org.richfaces.min")
        );

    @Override
    public Iterable<ResourceKey> getResources() {
        return BOOTSTRAP_MIN;
    }

}
