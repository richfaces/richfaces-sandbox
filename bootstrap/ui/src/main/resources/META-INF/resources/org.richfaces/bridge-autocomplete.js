(function ($) {
    
    $.widget('rf.autocompleteBridge', {
        
        options: {
            token: ""
        },
        
        _create: function() {
            this.input = $(document.getElementById(this.options.inputId));
            
            this._enhanceAutocomplete();
            this._registerListeners();
        },
        
        _enhanceAutocomplete: function() {
            var options = this._getBasicAutocompleteOptions();
            
            if (this.options.token) {
                options = $.extend(options, this._getTokenizingAutocompleteOptions());
                this._preventTabbing();
            }
            
            this.input.autocomplete(options);
        },
        
        _getBasicAutocompleteOptions: function() {
            return {
                delay: 0,
                minLength: 0,
                source: this.options.suggestions
            };
        },
        
        _getTokenizingAutocompleteOptions: function() {
            var bridge = this;
            
            var split = function split( val ) {
                var regexp = new RegExp("\\s*" + bridge.options.token + "\\s*"); 
                return val.split( regexp );
            };
            
            var extractLast = function extractLast( term ) {
                return split( term ).pop();
            };
            
            return {
                source: function( request, response ) {
                    // delegate back to autocomplete, but extract the last term
                    response( $.ui.autocomplete.filter(
                        bridge.options.suggestions, extractLast( request.term ) ) );
                },
                focus: function() { return false; },
                select: function( event, ui ) {
                    var terms = split( this.value );
                    // remove the current input
                    terms.pop();
                    // add the selected item
                    terms.push( ui.item.value );
                    // add placeholder to get the comma-and-space at the end
                    terms.push( "" );
                    this.value = terms.join( bridge.options.token + " " );
                    return false;
                }
            }
        },
        
        _preventTabbing: function() {
            this.element.bind( "keydown", function( event ) {
                if ( event.keyCode === $.ui.keyCode.TAB &&
                    $( this ).data( "autocomplete" ).menu.active ) {
                    event.preventDefault();
                }
            });
        },
        
        _registerListeners: function() {
            this.input.bind("autocompletesearch", this.options.onsearch);
            this.input.bind("autocompleteopen", this.options.onopen);
            this.input.bind("autocompletefocus", this.options.onfocus);
            this.input.bind("autocompleteselect", this.options.onselect);
            this.input.bind("autocompleteclose", this.options.onclose);
            this.input.bind("autocompletechange", this.options.onchange);
        }
    });
    
}(jQuery));