(function() {
    var element = document.createElement("div");
    element.innerHTML = "Body script works";
    document.getElementsByTagName("body")[0].appendChild(element);
})();