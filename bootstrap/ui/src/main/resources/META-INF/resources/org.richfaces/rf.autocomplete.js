(function($) {
    $.widget("rf.autocomplete", $.extend({}, $.ui.autocomplete.prototype, {
        
        options: {
            suggestions: [],
            uifocus: null,
            uiselect: null,
            tokens: "",
            source: function(request, response) {
                response( $.ui.autocomplete.filter(
                    this.options.suggestions, this._extractLastToken(request.term) ) );
            }
        },
        
        _create : function() {
            // extend jQuery UI Autocomplete
            $.ui.autocomplete.prototype._create.apply(this, arguments);
            
            var self = this;
            
            this.options.focus = function( event, ui ) {
                if (self.options.uifocus) {
                    if (!self.options.uifocus.apply(this, arguments)) {
                        return false;
                    }
                }
                return false;
            };
            
            this.options.select = function( event, ui ) {
                if (self.options.uiselect) {
                    if (!self.options.uiselect.apply(this, arguments)) {
                        return false;
                    }
                }
                
                var terms = self._tokenize( this.value );
                // remove the current input
                terms.pop();
                // add the selected item
                terms.push( ui.item.value );
                // add placeholder to get the comma-and-space at the end
                terms.push( "" );
                this.value = terms.join( self.options.tokens );
                
                return false;
            };
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
                    if (this.options.source) {
                        this._setOption('uifocus', value);
                        return;
                    }
                    break;
                case 'select':
                    // disallow to rewrite 'focus'
                    if (this.options.source) {
                        this._setOption('uiselect', value);
                        return;
                    }
                    break;
                    
            }
            $.ui.autocomplete.prototype._setOption.apply(this, arguments);
        },
        
        _tokenize: function(term) {
            if (this.options.tokens) {
                return term.split( new RegExp("\\s*" + this.options.tokens + "\\s*") );
            }
            return [term];
        },
        
        _extractLastToken: function(term) {
            return this._tokenize(term).pop();
        }
        
    }));

    $.rf.autocomplete.prototype.options = $.extend({}, $.ui.autocomplete.prototype.options, $.rf.autocomplete.prototype.options);
    
    
})(jQuery);