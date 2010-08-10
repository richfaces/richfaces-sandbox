package org.ajax4jsf;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;


@Name("authenticator")
public class Authenticator
{
    @Logger Log log;
    
    @In Identity identity;
   
    public boolean authenticate()
    {
        log.info("authenticating #0", identity.getUsername());
        //write your authentication logic here,
        //return true if the authentication was
        //successful, false otherwise
        identity.addRole("admin");
        return true;
    }
}
