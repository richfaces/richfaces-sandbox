#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.Serializable;

public class RichBean implements Serializable {

    private static final long serialVersionUID = -2403138958014741653L;
    private Logger logger;
    private String name;

    public RichBean() {
        logger = LoggerFactory.getLogger(RichBean.class);
        logger.info("post construct: initialize");
        name = "John";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
