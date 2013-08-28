window.charttestutil = {};

window.charttestutil.hello = function() {
	  return "Hello World!";
}

window.charttestutil.seriesLength = function(id){
	  return $(document.getElementById(id)).chart("option","data").length;
}

window.charttestutil.dataLength = function(id,seriesIndex){
      return $(document.getElementById(id)).chart("option","data")[seriesIndex].length;
}

window.charttestutil.pointX = function(id,seriesIndex,pointIndex){
	  return $(document.getElementById(id)).chart("option","data")[seriesIndex].data[pointIndex][0];
}

window.charttestutil.pointY = function(id,seriesIndex,pointIndex){
	  return $(document.getElementById(id)).chart("option","data")[seriesIndex].data[pointIndex][1];
}
window.charttestutil.pointXPos = function(id,seriesIndex,pointIndex){
	  //line chart only for now
	  var xVal = window.charttestutil.pointX(id,seriesIndex,pointIndex);
	  var plotObj = $(document.getElementById(id)).chart("getPlotObject");
	  return plotObj.getXAxes()[0].p2c(xVal)+plotObj.offset().left;
}
window.charttestutil.pointYPos = function(id,seriesIndex,pointIndex){
	  //line chart only for now
	  var yVal = window.charttestutil.pointY(id,seriesIndex,pointIndex);
	  var plotObj = $(document.getElementById(id)).chart("getPlotObject");
	  return plotObj.getYAxes()[0].p2c(yVal)+plotObj.offset().top;
}

