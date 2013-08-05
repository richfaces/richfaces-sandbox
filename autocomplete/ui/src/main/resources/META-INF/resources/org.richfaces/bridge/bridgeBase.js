(function($) {

  $.widget('rf.bridgeBase', {

    options : {
      pluginNames : null
    },

    _create : function() {
      this._registerCleanDomListener(this.element, this.options.pluginNames);
    },

    destroy : function() {
      this._unRegisterCleanDomListener();
    },

    _registerCleanDomListener : function(element, pluginNames) {
      $('body').on("cleanDom.orderingList.bootstrap.RICH", function(event, ui) {
        if ($.contains(ui.target, element)) {
          $.each(pluginNames, function() {
            var pluginName = this;
            $(element).data(pluginName).destroy();
          })

        }
      });
    },

    _unRegisterCleanDomListener : function() {
      $('body').off("cleanDom.orderingList.bootstrap.RICH");
    }

  });

}(jQuery));