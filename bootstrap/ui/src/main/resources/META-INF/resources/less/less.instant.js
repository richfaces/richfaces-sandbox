;(function ( $, window, document, undefined ) {
    
    window.less = window.less || {};
    
    var enabled = false;
    
    var interval;
    
    var clearLessCache = function() {
        if (enabled) {
            localStorage.clear();
        }
    }
    
    var suffix = " (instant LESS)";
    
    less.instant = {
        
        enable : function() {
            enabled = true;
            document.title += suffix; 
            clearLessCache();
            interval = window.setInterval(clearLessCache, 500);
            less.watch();
        },
        
        disable : function() {
            enabled = false;
            document.title = document.title.substr(0, document.title.lastIndexOf(suffix)) || document.title;
            if (interval) {
                window.clearInterval(clearLessCache);
            }
            less.unwatch();
        },
        
        toggle : function() {
            if (enabled) {
                this.disable();
            } else {
                this.enable();
            }
        }
    }
    
})( jQuery, window, document );