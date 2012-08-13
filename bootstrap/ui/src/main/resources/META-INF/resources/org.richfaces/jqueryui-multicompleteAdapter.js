(function($) {
    $.widget("rf.multicompleteBridge", {
        
        options: {
        },
    
        _create : function() {
            var suggestions = $(this.element).hide(),
                input = this.options.input,
                layout = $(this.element)[0].tagName.toLowerCase();
            
            var swapCss = function(elem, newProps) {
                var old = {};
                for (var prop in newProps) {
                    old[prop] = $(elem).css(prop);
                }
                $(elem).css(newProps);
                return old;
            };
            
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
                        var oldCss = swapCss(suggestions, { position: 'absolute', left: '-9999px', top: '-9999px' });
                        $(suggestions).show();
                        
                        $(suggestions).children("tbody").children("tr").each(function(i, tr) {
                            var label = $(tr).html();
                            
                            var value = $(tr).data("value");
                            value = value ? value : $(tr).text();
                            
                             
                            $(tr).children("td").each(function(j, td) {
                                $(td).data("width", $(td).width());
                            });
                            
                            
                            if (matcher.test( $(tr).text() )) {
                                matches.push({
                                    value: value,
                                    label: label,
                                    tr: tr
                                });
                            }
                        });
                        
                        $(suggestions).hide();
                        swapCss(suggestions, oldCss);
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