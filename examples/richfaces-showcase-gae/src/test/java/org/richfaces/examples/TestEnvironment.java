package org.richfaces.examples;

import com.google.apphosting.api.ApiProxy;

import java.util.Map;

/**
 * project test environment
 */
class TestEnvironment implements ApiProxy.Environment {
    /**
     * get GAE app id
     *
     * @return app id
     */
    public String getAppId() {
        return "gae-showcase";
    }

    /**
     * get version id
     *
     * @return version
     */
    public String getVersionId() {
        return "4.0.0-SNAPSHOT";
    }

    public void setDefaultNamespace(String s) {
    }

    public String getRequestNamespace() {
        return null;
    }

    public String getDefaultNamespace() {
        return null;
    }

    public String getAuthDomain() {
        return null;
    }

    public boolean isLoggedIn() {
        return false;
    }

    public String getEmail() {
        return null;
    }

    public boolean isAdmin() {
        return false;
    }

    public Map<String, Object> getAttributes() {
        return null;  
    }
}
