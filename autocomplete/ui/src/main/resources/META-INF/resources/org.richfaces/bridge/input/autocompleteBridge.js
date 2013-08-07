(function($, RichFaces) {

  $.widget('rf.richAutocompleteBridge', $.rf.bridgeBase, {

    options : {
      mode : 'cachedAjax',
      showButton : false,
      layout : 'list',
      minChars : 0,
      selectFirst : true
    },

    _create : function() {
      var clientId = this.element.attr('id');
      var bridge = this;

      var autocompleteOptions = $.extend({}, this.options, {
        minLength: this.options.minLength,
        autoFocus: this.options.selectFirst,
        
        source : '[id="' + clientId + 'Suggestions"]',
        update : function(request, done) {
          if (bridge.options.mode.match(/client/i)) {
            done();
            return;
          }
          var params = {};
          params[clientId + 'SearchTerm'] = request.term;
          RichFaces.ajax(clientId, null, {
            parameters : params,
            error : done,
            complete : done
          });
        }
      });

      $(document.getElementById(clientId + 'Input')).richAutocomplete(autocompleteOptions);
    }
  });

}(jQuery, RichFaces));