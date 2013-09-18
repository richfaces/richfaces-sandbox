(function($, RichFaces) {

  $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

    options : {
    },

    _create : function() {
      var clientId = this.element.attr('id');
      var bridge = this;

      var orderingListOptions = $.extend({}, this.options, {
      });

      $(document.getElementById(clientId)).orderingList(orderingListOptions);
    }
  });

}(jQuery, RichFaces));