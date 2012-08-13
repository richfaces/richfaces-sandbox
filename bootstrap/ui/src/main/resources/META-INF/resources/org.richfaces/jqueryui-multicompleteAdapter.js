(function($) {
    $.widget("rf.multicompleteBridge", {
        
        options: {
        },
    
        _create : function() {
            var suggestions = $(this.element).hide(),
                input = this.options.input;
            
            var suggestFn = function(searchTerm) {
                var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex(searchTerm), "i" );
                if ( searchTerm ) {
                    var matches = [];
                    $("li", suggestions).each(function(i, suggestion) {
                        if (matcher.test( $(suggestion).text() )) {
                            matches.push({
                                value: $(suggestion).text(),
                                label: $(suggestion).html()
                            });
                        }
                    });
                    return matches;
                }
                
            };
            
            $(input).multicomplete({
                suggest: suggestFn
            });
        }
        
    });
    
    
})(jQuery);