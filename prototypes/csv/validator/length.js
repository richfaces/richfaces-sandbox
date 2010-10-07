(function($, rf) {

	rf.csv = rf.csv || {};
	
	var _messages = {};
	var _validators = {};
	var _converters = {};

	var RE_MESSAGE_PATTERN = /\{(\d+)\}/g;
	
	$.extend(rf.csv, {
		// Messages API
		addMessage: function (messagesObject) {
			$.extend(_messages, messagesObject);
		},
		getMessage: function(customMessages, id, values) {
			var message = (customMessages && customMessages[id]) || _messages[id] || "";
			if (message) {
				var msgObject = message.replace(RE_MESSAGE_PATTERN,"\n$1\n").split("\n");
				var value;
				for (var i=1; i<msgObject.length; i+=2) {
					value = values[msgObject[i]];
					msgObject[i] = typeof value == "undefined" ? "" : value;
				}
				message = msgObject.join('');
			}
			return message;
		},
		// Validators API
		addValidator: function (validatorFunctions) {
			$.extend(_validators, validatorFunctions);
		},
		validate: function (event, id, validatorList, messageComponentList, options) {
			var value;
			var element = rf.getDomElement(id);
			if (element.value) {
				value = element.value;
			} else {
				var component = rf.$(element);
				value = component && typeof component["getValue"] == "function" ? component.getValue() : "";
			}
			var result;
			var messageComponentsUpdated = false;
			var validatorFunction;
			for (var validator in validatorList) {
				validatorFunction = _validators[validator];
				if (validatorFunction) {
					result = validatorFunction(value, validatorList[validator]);
				}
				if (result && result.length>0) {
					messageComponentsUpdated = true;
					//updateMessageComponents(messageElementIds, result);
					alert(result);
					break;
				}
			}
			!messageComponentsUpdated && alert("no message from validators");//updateMessageComponents(messageElementIds, "");
		},
		addFormValidators: function (formId, callValidatorFunctions) {
			
		}
	});
})(jQuery, window.RichFaces || (window.RichFaces={}));


RichFaces.csv.addValidator({"length":
(function(rf) {
	return function (value, params) {
		var result = "";
		if (value.length<params.min) {
			result = rf.csv.getMessage(params.customMessages, 'LengthValidator.MINIMUM', [params.min,value]);	
		} else if (value.length>params.max){
			result = rf.csv.getMessage(params.customMessages, 'LengthValidator.MAXIMUM', [params.max,value]);	
		}
		return result;
	}
})(window.RichFaces || (window.RichFaces={}))
});