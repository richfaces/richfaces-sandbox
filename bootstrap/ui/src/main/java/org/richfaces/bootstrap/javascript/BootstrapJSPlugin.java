package org.richfaces.bootstrap.javascript;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the component with the details of the underlying Bootstrap JavaScript plug-in.
 * 
 * @author Lukas Fryc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface BootstrapJSPlugin {

    String name();
}