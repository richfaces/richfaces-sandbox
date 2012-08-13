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
                        $(suggestions).children("li").each(function(i, item) {
                            
                            var label = $(item).data("label");
                            label = label ? label : $(item).text();
                            
                            var content = $(item).html();
                            
                            if (matcher.test( $(item).text() )) {
                                matches.push({
                                    label: label,
                                    content: content
                                });
                            }
                        });
                    } else if (layout === 'table') {
                        var oldCss = swapCss(suggestions, { position: 'absolute', left: '-9999px', top: '-9999px' });
                        $(suggestions).show();
                        
                        $(suggestions).children("tbody").children("tr").each(function(i, tr) {
                            
                            var label = $(tr).data("label");
                            label = label ? label : $(tr).text();
                            
                            var content = $(tr).html();
                             
                            $(tr).children("td").each(function(j, td) {
                                $(td).data("width", $(td).width());
                            });
                            
                            
                            if (matcher.test( $(tr).text() )) {
                                matches.push({
                                    label: label,
                                    content: content,
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