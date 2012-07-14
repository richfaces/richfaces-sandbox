jQuery(document).ready(function() {
    if (less) {
        less.watch();
        if (localStorage) {
            window.setInterval(function() {
                localStorage.clear()
            }, 500);
        }
    }
});