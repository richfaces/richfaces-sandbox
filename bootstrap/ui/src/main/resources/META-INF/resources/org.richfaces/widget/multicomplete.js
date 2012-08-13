//Optional parameter includeMargin is used when calculating outer dimensions
(function($) {
$.fn.getHiddenDimensions = function(includeMargin) {
    var $item = this,
        props = { position: 'absolute', visibility: 'hidden', display: 'block' },
        dim = { width:0, height:0, innerWidth: 0, innerHeight: 0,outerWidth: 0,outerHeight: 0 },
        $hiddenParents = $item.parents().andSelf().not(':visible'),
        includeMargin = (includeMargin == null)? false : includeMargin;

    var oldProps = [];
    $hiddenParents.each(function() {
        var old = {};

        for ( var name in props ) {
            old[ name ] = this.style[ name ];
            this.style[ name ] = props[ name ];
        }

        oldProps.push(old);
    });

    dim.width = $item.width();
    dim.outerWidth = $item.outerWidth(includeMargin);
    dim.innerWidth = $item.innerWidth();
    dim.height = $item.height();
    dim.innerHeight = $item.innerHeight();
    dim.outerHeight = $item.outerHeight(includeMargin);

    $hiddenParents.each(function(i) {
        var old = oldProps[i];
        for ( var name in props ) {
            this.style[ name ] = old[ name ];
        }
    });

    return dim;
}
}(jQuery));

(function($) {
    $.widget("rf.multicomplete", $.extend({}, $.ui.autocomplete.prototype, {
        
        options: {
            suggestions: [],
            autoFill: false,
            tokens: ',',
            layout: 'ul',
            
            source: function(request, response) {
                var searchTerm = this._extractLastToken(request.term);
                response( this.options.suggest.call(this, searchTerm) );
            },
            suggest: function(term) {
                var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex(term), "i" );
                return $.grep( this.options.suggestions, function(value) {
                    return matcher.test( value.label || value.value || value );
                });
            }
        },
    
        _create : function() {
            // extend jQuery UI Autocomplete
            $.ui.autocomplete.prototype._create.apply(this, arguments);
            
            var self = this;
            
            $(this.element).on("autocompletefocus", function( event, ui ) {
                if (!self.options.autoFill) {
                    // do nothing on suggestion focus
                    return false;
                } else {
                    var terms = self._tokenize( this.value );
                    // remove the current input
                    var lastTerm = terms.pop();
                    var lastTermCompletion = lastTerm + ui.item.value.substring(lastTerm.length);
                    terms.push(lastTermCompletion);
                    
                    var firstToken = self.options.tokens.charAt(0);
                    this.value = terms.join( firstToken == " " ? " " : firstToken + " " );
                    this.setSelectionRange(this.value.length - ui.item.value.length + lastTerm.length, this.value.length);
                    return false;
                }
            });
            
            $(this.element).on("autocompleteselect", function( event, ui ) {
                var terms = self._tokenize( this.value );
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push( ui.item.value );
                // add placeholder to get the comma-and-space at the end
                terms.push( "" );
                var firstToken = self.options.tokens.charAt(0);
                this.value = terms.join( firstToken == " " ? " " : firstToken + " " );
                return false;
            });
        },
        
        _isTableLayout: function() {
            return this.options.layout === 'table';
        },
        
        _renderItem: function( container, item) {
            var li = $( "<li></li>" )
                .data( "item.autocomplete", item );
            var a = $("<a></a>").appendTo(li);
            
            if (this._isTableLayout()) {
                a.css("display", "block").addClass("clearfix");
                
                $(item.tr).children("td").each(function(i, td) {
                    $("<span></span>")
                        .width($(td).data("width"))
                        .addClass("ui-menu-item-column")
                        .css("display", "block")
                        .html($(td).html())
                        .appendTo(a);
                });
            } else {
                a.html( item.label );
            }
            
            li.appendTo( container );
        },
        
        _tokenize: function(term) {
            return term.split( new RegExp("\\s*[" + this.options.tokens + "]\\s*") );
        },
        
        _extractLastToken: function(term) {
            return this._tokenize(term).pop();
        }
        
    }));

    $.rf.multicomplete.prototype.options = $.extend({}, $.ui.autocomplete.prototype.options, $.rf.multicomplete.prototype.options);
    
    
})(jQuery);