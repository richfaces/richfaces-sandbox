(function($, RichFaces) {

  $.widget('rf.pickListBridge', $.rf.bridgeBase, {

    options: {
      hiddenInputSuffix: 'Input'
    },

    _create : function() {
      $.rf.bridgeBase.prototype._create.call( this );
      var pickListOptions = $.extend({}, this.options, {});

      if (pickListOptions.buttonsText) {
          pickListOptions.buttonsText = $.parseJSON(pickListOptions.buttonsText);
      }

      this.element.pickList(pickListOptions);
      this._addDomElements();
      this._registerListeners();
    },

    _addDomElements: function() {
      var clientId = this.element.attr('id');
      var hiddenInputId = clientId + this.options.hiddenInputSuffix;
      this.hiddenInput = $('<input type="hidden" />').attr('id', hiddenInputId).attr('name', clientId);
      this.element.parents(".outer").first().append(this.hiddenInput);
      var ui = this.element.data('pickList')._dumpState();
      this._refreshInputValues(ui.pickedKeys);
    },

    _registerListeners: function() {
      var bridge = this;
      this.element.on('picklist_change', function(event, ui) {
        bridge._refreshInputValues(ui.pickedKeys);
      });
    },

    _refreshInputValues: function(pickedKeys) {
      var csvKeys = pickedKeys.join(',');
      this.hiddenInput.val(csvKeys);
    }

  });

}(jQuery, RichFaces));