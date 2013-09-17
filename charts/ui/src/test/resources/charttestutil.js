window.charttestutil = {

   //suffix chart is added automatically to ID		
   hello : function() {
	  return "Hello World!";
   },

   seriesLength : function(id){
	  return $(document.getElementById(id+'Chart')).chart("option","data").length;
   },

   dataLength : function(id,seriesIndex){
      return $(document.getElementById(id+'Chart')).chart("option","data")[seriesIndex].length;
   },

   pointX : function(id,seriesIndex,pointIndex){
	  return $(document.getElementById(id+'Chart')).chart("option","data")[seriesIndex].data[pointIndex][0];
   },

   pointY : function(id,seriesIndex,pointIndex){
	  return $(document.getElementById(id+'Chart')).chart("option","data")[seriesIndex].data[pointIndex][1];
   },
   
   pointXPos : function(id,seriesIndex,pointIndex){
	  //line chart only for now
	  var xVal = window.charttestutil.pointX(id,seriesIndex,pointIndex);
	  var plotObj = $(document.getElementById(id+'Chart')).chart("getPlotObject");
	  return plotObj.getXAxes()[0].p2c(xVal)+plotObj.offset().left;
   },
   
   pointYPos : function(id,seriesIndex,pointIndex){
	  //line chart only for now
	  var yVal = window.charttestutil.pointY(id,seriesIndex,pointIndex);
	  var plotObj = $(document.getElementById(id+'Chart')).chart("getPlotObject");
	  return plotObj.getYAxes()[0].p2c(yVal)+plotObj.offset().top-10;
   }

}

