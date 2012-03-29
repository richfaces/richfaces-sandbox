package org.richfaces.sandbox.syntaxhighlighter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@SessionScoped
@ManagedBean
public class SyntaxHighlighterBean implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private boolean autoLinks = true;

    private String code = "/**\n * View http://richfaces.org\n *\ttabbed\t\ttabbed twice\n */public boolean isReady()\n{\n\treturn true;\t//Some comment\n}";

    private boolean collapsed;

    private Integer firstLine = 2;

    private boolean gutter = true;

    private String highlight = "2,4";

    private boolean htmlScript;

    private String language = "java";

    private boolean smartTabs = true;

    private boolean stripBrs = true;

    private Integer tabSize = 4;

    private String theme = "default";

    private boolean toolbar = true;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getCode() {
        return code;
    }

    public void setCode(String intValue) {
        this.code = intValue;
    }

    public Integer getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(Integer firstLine) {
        this.firstLine = firstLine;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getTabSize() {
        return tabSize;
    }

    public void setTabSize(Integer tabSize) {
        this.tabSize = tabSize;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean isAutoLinks() {
        return autoLinks;
    }

    public void setAutoLinks(boolean autoLinks) {
        this.autoLinks = autoLinks;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public boolean isGutter() {
        return gutter;
    }

    public void setGutter(boolean gutter) {
        this.gutter = gutter;
    }

    public boolean isHtmlScript() {
        return htmlScript;
    }

    public void setHtmlScript(boolean htmlScript) {
        this.htmlScript = htmlScript;
    }

    public boolean isSmartTabs() {
        return smartTabs;
    }

    public void setSmartTabs(boolean smartTabs) {
        this.smartTabs = smartTabs;
    }

    public boolean isStripBrs() {
        return stripBrs;
    }

    public void setStripBrs(boolean stripBrs) {
        this.stripBrs = stripBrs;
    }

    public boolean isToolbar() {
        return toolbar;
    }

    public void setToolbar(boolean toolbar) {
        this.toolbar = toolbar;
    }
}
