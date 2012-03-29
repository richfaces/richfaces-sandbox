package org.richfaces.sandbox.outputfile;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@ManagedBean
public class OutputFileBean {
// ------------------------------ FIELDS ------------------------------

    private List<String> files = Arrays.asList("/menu.xhtml", "/sample_1.xhtml");

    private String selectedFile;

    public OutputFileBean() {
        selectedFile = files.get(1);
    }
// --------------------- GETTER / SETTER METHODS ---------------------

    public List<String> getFiles() {
        return files;
    }

    public String getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(String selectedFile) {
        this.selectedFile = selectedFile;
    }
}
