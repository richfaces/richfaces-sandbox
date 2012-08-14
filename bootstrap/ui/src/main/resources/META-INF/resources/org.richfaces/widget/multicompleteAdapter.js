(function($) {
    $.widget("rf.multicompleteBridge", {
        
        options: {
        },
    
        _create : function() {
            var suggestions = $(this.element).hide(),
                input = this.options.input;
            
            var suggestFn = function(searchTerm) {
                var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex(searchTerm), "i" );
                var matches = [];
                $(suggestions).children("li").each(function(i, item) {
                    
                    if (!searchTerm || matcher.test( $(item).text() )) {
                        var label = $(item).data("label");
                        label = label ? label : $(item).text();
                        
                        var content = $(item).html();
                        
                        matches.push({
                            label: label,
                            content: content
                        });
                    }
                });
                return matches;
            };
            
            $(input).multicomplete({
                suggest: suggestFn
            });
        }
        
    });
    
    
})(jQuery);