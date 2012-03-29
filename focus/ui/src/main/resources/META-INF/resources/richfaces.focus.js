/**
 * This function evaluates code in template with object in ScopeChain.
 * This is usefull if you need to evaluate code that uses member names
 * that colide with external names that the code refers to.
 * There is almost exact method in utils.js called Richfaces.eval,
 * but it swallows exception thrown during evaluation, which makes debugging
 * hard.
 */
(function ($, rf) {
    // Create (for example) ui container for our component class
    rf.ui = rf.ui || {};
    // Default options definition if needed for the component
    //    var defaultOptions = {};
    var m_focus;
    var m_priority = 999999;
    var m_domLoaded = false;
    var m_timing = false;
    var m_focusStore = {};
    /** Constants */
    var TIMING_ON_JS_CALL = "onJScall";
    var TIMING_ON_LOAD = "onload";

    var focus = function(element) {
        try {
            if (element == null) {
                if (m_focus == null) {
                    return;
                }
                element = document.getElementById(m_focus);
            }
            if (element != null) {
                if (typeof element === "string") {
                    element = document.getElementById("j_idt30:dateInputDate");
                }
                element.focus();
                element.select(element);
            }
        } finally {
            rf.ui.Focus.clearFocus();
        }
    };
    // Extending component class with new properties and methods using extendClass
    // $super - reference to the parent prototype, will be available inside those methods
    rf.ui.Focus = {
        // class name
        name:"Focus",
        // private functions definition
        focus:  focus,
        focusStored: function(focusComponentId) {
            var element = document.getElementById(m_focusStore[focusComponentId]);
            if (element != null) {
                focus(element);
            }
        },
        getFocus : function() {
            return m_focus;
        },
        setFocus : function(id, priority, focusComponentId, timing) {
            if (priority == null) {
                priority = 99999;
            }
            if (timing != TIMING_ON_JS_CALL) {
                timing = TIMING_ON_LOAD;
            }
            if (focusComponentId != null) {
                m_focusStore[focusComponentId] = id;
            }
            if (m_focus == null || priority < m_priority) {
                m_focus = id;
                m_priority = priority == null ? 0 : priority;
                m_timing = timing;
                if (m_domLoaded && timing == TIMING_ON_LOAD) {
                    focus(null);
                }
            }
        },
        clearFocus : function() {
            m_focus = null;
            m_priority = 999999;
            m_timing = null;
        }

    };
    jQuery(function() {
        if (m_timing == TIMING_ON_LOAD) {
            focus(null);
        }
        m_domLoaded = true;
    });
    // define super class reference - reference to the parent prototype
    var $super = rf.ui.Focus.$super;
})(jQuery, RichFaces);