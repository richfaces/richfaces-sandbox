(function($) {
    $.widget("rf.multicompleteBridge", {
        
        options: {
        },
    
        _create : function() {
            var suggestions = $(this.element).hide(),
                input = this.options.input,
                layout = $(this.element)[0].tagName.toLowerCase();
            
            var suggestFn = function(searchTerm) {
                var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex(searchTerm), "i" );
                if ( searchTerm ) {
                    var matches = [];
                    if (layout === 'ul') {
                        $(suggestions).children("li").each(function(i, suggestion) {
                            var label = $(suggestion).html();
                            
                            var value = $(suggestion).data("value");
                            value = value ? value : $(suggestion).text();
                            
                            if (matcher.test( $(suggestion).text() )) {
                                matches.push({
                                    value: value,
                                    label: label
                                });
                            }
                        });
                    } else if (layout === 'table') {
                        $(suggestions).children("tbody").children("tr").each(function(i, suggestion) {
                            var label = $(suggestion).html();
                            
                            var value = $(suggestion).data("value");
                            value = value ? value : $(suggestion).text();
                            
                            if (matcher.test( $(suggestion).text() )) {
                                matches.push({
                                    value: value,
                                    label: label
                                });
                            }
                        });
                    }
                    return matches;
                }
                
            };
            
            $(input).multicomplete({
                layout: layout,
                suggest: suggestFn
            });
        }
        
    });
    
    
})(jQuery);