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
                        var text = $(suggestion).text();
                        if (matcher.test( text )) {
                            matches.push( text );
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