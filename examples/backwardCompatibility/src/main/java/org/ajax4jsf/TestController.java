/**
 * 
 */
package org.ajax4jsf;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;

import javax.faces.component.UIInput;

@Name("testController")
public class TestController {
    @In
    FacesMessages facesMessages;
    private UIInput input;
    private String value;

    public UIInput getInput() { return input; }

    public void setInput(UIInput input) { this.input = input; }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }
} 