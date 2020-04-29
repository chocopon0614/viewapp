function dataArray(str) {
	var tmpResult = [];

	for (var i = 0; i < str.length; i++) {

	 var tmpData = [];
	 var tmp1 = str[i];

	  for (var k in tmp1){
	    tmpData.push(tmp1[k]);
	  }

	  tmpResult.push(tmpData);
	  
	}
	 return tmpResult;
	
}

function drawChart(data) {

	alert(data);
	alert('!EWEW');


	  var tmp = dataArray(data); 
      var tmpLabels = [], tmpData1 = [], tmpData2 = [], tmpData3 = [];
	  
	  for (var row in tmp){
		 tmpLabels.push(tmp[row][0]);
	     tmpData1.push(tmp[row][1])
	     tmpData2.push(tmp[row][2])
	     
	     var bmi = tmp[row][2]/((tmp[row][1]/100)*(tmp[row][1]/100));
	     var bmi = Math.round(bmi*100)/100;
	     tmpData3.push(bmi)

	  }
	  
	loadCharts_line(tmpLabels, tmpData1, tmpData2);
	loadCharts_bar(tmpLabels, tmpData3);
	
}

const loadCharts_line = function (labels, d1, d2) {
  const chartDataSet = {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Height',
        data: d1,
        backgroundColor: 'rgba(60, 160, 220, 0.3)',
        borderColor: 'rgba(60, 160, 220, 0.8)'
      }, {
        label: 'Weight',
        data: d2,
        backgroundColor: 'rgba(60, 190, 20, 0.3)',
        borderColor: 'rgba(60, 190, 20, 0.8)'
      }]
    },
    options: {
    }
  };
 
  const ctx = document.createElement('canvas');
  document.getElementById('chart-area1').appendChild(ctx);
  new Chart(ctx, chartDataSet);
};

const loadCharts_bar = function (labels, d1) {
	  const chartDataSet = {
	    type: 'bar',
	    data: {
	      labels: labels,
	      datasets: [{
	        label: 'BMI',
	        data: d1,
	        borderWidth: 2,
	        backgroundColor: 'rgba(233, 200, 27, 0.3)',
	        borderColor: 'rgba(233, 200, 27, 0.8)'
	      }]
	    },
	    options: {
	    }
	  };
	 
	  const ctx = document.createElement('canvas');
	  document.getElementById('chart-area2').appendChild(ctx);
	  new Chart(ctx, chartDataSet);
	};
	

	window.onload = function () {
		var listdata = JSON.parse('[{"Registration time":"23/12/2012 09:00:23","Height":181.0,"Weight":72.0}]');
		drawChart(listdata)
		};
