(function($, RichFaces) {

  $.widget('rf.orderingListBridge', $.rf.bridgeBase, {

    options : {
      clientId: null,
      hiddenInputSuffix: 'Input'
    },

    _create : function() {
      $.rf.bridgeBase.prototype._create.call( this );
      var orderingListOptions = $.extend({}, this.options, {});

      if (orderingListOptions.buttonsText) {
          orderingListOptions.buttonsText = $.parseJSON(orderingListOptions.buttonsText);
      }

      this.element.orderingList(orderingListOptions);
      this._addDomElements();
      this._registerListeners();
    },

    _addDomElements: function() {
      var clientId = (this.options.clientId) ? this.options.clientId : this.element.attr('id');
      var hiddenInputId = clientId + this.options.hiddenInputSuffix;
      this.hiddenInput = $('<input type="hidden" />').attr('id', hiddenInputId).attr('name', clientId);
      this.element.parents(".select-list").first().append(this.hiddenInput);
      var ui = this.element.data('orderingList')._dumpState();
      this._refreshInputValues(ui.orderedKeys);
    },

    _registerListeners: function() {
      var bridge = this;
      this.element.on('orderinglist_change', function(event, ui) {
        bridge._refreshInputValues(ui.orderedKeys);
      });
    },

    _refreshInputValues: function(orderedKeys) {
      var csvKeys = orderedKeys.join(',');
      this.hiddenInput.val(csvKeys);
    }

  });

}(jQuery, RichFaces));