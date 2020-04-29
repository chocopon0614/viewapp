var ViewApp = angular.module('ViewApp', ['ngRoute', 'chart.js']);

ViewApp.config(['$routeProvider', function($routeProvider){
    $routeProvider
    .when('/', {
      templateUrl: 'templates/login.html',
      controller: 'LoginController'
    })
    .when('/menu', {
      templateUrl: 'templates/menu.html'
    })
    .when('/view', {
      templateUrl: 'templates/view.html',
      controller: 'ViewController'
    })
    .otherwise({
      redirectTo: '/'
    });
}]);



ViewApp.controller('LoginController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike',
	 function($scope, $http, $location, $httpParamSerializerJQLike){
	
    	$scope.username = '';
    	$scope.password = '';
    	
    	$scope.submit = function(){
    	  var method = "POST";	
    	  var url = 'api/resources/login';	
    		
    	  $http({
    	          method: method,
    	          headers : {
                      'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
                  },
                  transformRequest: $httpParamSerializerJQLike,
    	          url: url,
    	          data: { username: $scope.username, password: $scope.password }
    	        })

    	        .success(function(data, status, headers, config){
    	        	$location.path('/menu');
    	        })
    	        
    	        .error(function(data, status, headers, config){
    	        });
    	};
    }]);



ViewApp.controller('ViewController', ['$scope', function($scope){
	

	  $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
	  $scope.series = ['Series A', 'Series B'];
	  $scope.data = [
	    [65, 59, 80, 81, 56, 55, 40],
	    [28, 48, 40, 19, 86, 27, 90]
	  ];
	  $scope.onClick = function (points, evt) {
	    console.log(points, evt);
	  };
	  
//	  // Simulate async data update
//	  $timeout(function () {
//	    $scope.data = [
//	      [28, 48, 40, 19, 86, 27, 90],
//	      [65, 59, 80, 81, 56, 55, 40]
//	    ];
//	  }, 3000);
	  

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
//	  const chartDataSet = {
//	    type: 'line',
//	    data: {
//	      labels: labels,
//	      datasets: [{
//	        label: 'Height',
//	        data: d1,
//	        backgroundColor: 'rgba(60, 160, 220, 0.3)',
//	        borderColor: 'rgba(60, 160, 220, 0.8)'
//	      }, {
//	        label: 'Weight',
//	        data: d2,
//	        backgroundColor: 'rgba(60, 190, 20, 0.3)',
//	        borderColor: 'rgba(60, 190, 20, 0.8)'
//	      }]
//	    },
//	    options: {
//	    }
//	  };

	  
//	  const ctx = document.createElement('canvas');
//	  document.getElementById('chart-area1').appendChild(ctx);
//	  new Chart(ctx, chartDataSet);
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
		

//		var listdata = JSON.parse('[{"Registration time":"23/12/2012 09:00:23","Height":181.0,"Weight":72.0}]');
//		drawChart(listdata)
	
}]);
	
 
ViewApp.controller("LineCtrl", ['$scope', '$timeout', function ($scope, $timeout) {

	  $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
	  $scope.series = ['Series A', 'Series B'];
	  $scope.data = [
	    [65, 59, 80, 81, 56, 55, 40],
	    [28, 48, 40, 19, 86, 27, 90]
	  ];
	  $scope.onClick = function (points, evt) {
	    console.log(points, evt);
	  };
	  
	  // Simulate async data update
	  $timeout(function () {
	    $scope.data = [
	      [28, 48, 40, 19, 86, 27, 90],
	      [65, 59, 80, 81, 56, 55, 40]
	    ];
	  }, 3000);

}]);