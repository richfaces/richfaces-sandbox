(function ($, RichFaces) {

  $.widget('rf.richAutocompleteBridge', $.rf.bridgeBase, {

    options: {
      mode: 'cachedAjax'
    },

    _create: function () {
      var clientId = this.element.attr('id');
      var bridge = this;

      $(document.getElementById(clientId + 'Input')).richAutocomplete({
        source: '[id="' + clientId + 'Suggestions"]',
        showButton: true,
        update: function (request, done) {
          if (bridge.options.mode.match(/client/i)) {
            done();
            return;
          }
          var params = {};
          params[clientId + 'SearchTerm'] = request.term;
          RichFaces.ajax(clientId, null, {parameters: params, error: done, complete: done});
        }
      });
    }
  });

}(jQuery, RichFaces));