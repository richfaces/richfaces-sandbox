(function($) {
    $.widget("rf.autocomplete", $.extend({}, $.ui.autocomplete.prototype, {
        
        options: {
            suggestions: [],
            uifocus: null,
            uiselect: null,
            multiselect: false,
            tokens: ",",
            source: function(request, response) {
                this._delegate()._handleSource(request, response);
            }
        },
    
        _create : function() {
            this.options.focus = this._delegate()._handleFocus;
            this.options.select = this._delegate()._handleSelect;
            
            // extend jQuery UI Autocomplete
            $.ui.autocomplete.prototype._create.apply(this, arguments);
        },
        
        _setOption: function(key, value) {
            var self = this;
            if (this.options.key === value) {
                return;
            }
            switch (key) {
                case 'source':
                    // disallow to rewrite 'source'
                    if (this.options.source) {
                        this._setOption('suggestions', value);
                        return;
                    }
                    break;
                case 'focus':
                    // disallow to rewrite 'focus'
                    if (this.options.focus) {
                        this._setOption('uifocus', value);
                        return;
                    }
                    break;
                case 'select':
                    // disallow to rewrite 'select'
                    if (this.options.select) {
                        this._setOption('uiselect', value);
                        return;
                    }
                    break;
            }
            $.ui.autocomplete.prototype._setOption.apply(this, arguments);
        },
        
        _tokenize: function(term) {
            if (this.options.tokens) {
                return term.split( new RegExp("\\s*[" + this.options.tokens + "]\\s*") );
            }
            return [term];
        },
        
        _extractLastToken: function(term) {
            return this._tokenize(term).pop();
        },
        
        _delegate: function() {
            var autocomplete = this;
            return {
                _handleSource: function(request, response) {
                    var searchTerm;
                    if (autocomplete.options.multiselect) {
                        searchTerm = autocomplete._extractLastToken(request.term);
                    } else {
                        searchTerm = request.term;
                    }
                    response( $.ui.autocomplete.filter(
                        autocomplete.options.suggestions, searchTerm ) );
                },
                
                _handleFocus: function( event, ui ) {
                    if (autocomplete.options.multiselect) {
                        // prevent focus
                        return false;
                    }
                },
                
                _handleSelect: function( event, ui ) {
                    if (autocomplete.options.multiselect) {
                        var terms = autocomplete._tokenize( this.value );
                        // remove the current input
                        terms.pop();
                        // add the selected item
                        terms.push( ui.item.value );
                        // add placeholder to get the comma-and-space at the end
                        terms.push( "" );
                        var firstToken = autocomplete.options.tokens.charAt(0);
                        this.value = terms.join( firstToken == " " ? " " : firstToken + " " );
                        
                        return false;
                    }
                }
            };
        }
        
    }));

    $.rf.autocomplete.prototype.options = $.extend({}, $.ui.autocomplete.prototype.options, $.rf.autocomplete.prototype.options);
    
    
})(jQuery);