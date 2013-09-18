(function($, RichFaces) {

  $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

    options : {
    },

    _create : function() {
      var clientId = this.element.attr('id');
      var bridge = this;

      var orderingListOptions = $.extend({}, this.options, {
      });

      if (orderingListOptions.buttonsText) {
          orderingListOptions.buttonsText = $.parseJSON(orderingListOptions.buttonsText);
      }

      this.element.orderingList(orderingListOptions);
    }
  });

}(jQuery, RichFaces));