var ViewApp = angular.module('ViewApp', ['ngRoute']);

ViewApp.config(['$routeProvider', function($routeProvider){
    $routeProvider
    .when('/', {
      templateUrl: 'templates/menu.html'
    })
    .when('/connect', {
      templateUrl: 'templates/connect.html',
      controller: 'ConnectController'
    })
    .when('/linechart', {
      templateUrl: 'templates/linechart.html',
      controller: 'LineController'
    })
    .when('/barchart', {
      templateUrl: 'templates/barchart.html',
      controller: 'BarController'
    })
    .when('/radarchart', {
      templateUrl: 'templates/radarchart.html',
      controller: 'RadarController'
    })
    .when('/error', {
      templateUrl: 'templates/error.html'
    })
    .otherwise({
      redirectTo: '/'
    });
}]);


var url_apis = 'api/resources/apis';
var url_token = 'api/resources/token';	

var clientid = '67d06253-09e6-426e-b180-bdc98f90c2bf';
var token = localStorage.getItem('access_token');

var targetpath_body = '/chocopon0899gmailcom-dev/sb/openapi/bodyinformation';
var targetpath_blood = '/chocopon0899gmailcom-dev/sb/openapi/bloodinformation';


ViewApp.controller('ConnectController', ['$http', '$location','$httpParamSerializerJQLike',
	function( $http, $location, $httpParamSerializerJQLike){

	var authcode =$location.search()["code"];

	$http({
          method: 'POST',
          headers : {
              'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
          },
          transformRequest: $httpParamSerializerJQLike,
          url: url_token,
          data: { clientid: clientid, authcode: authcode}

	   }).then(function successCallback(response){
        	var resdata = response.data;
        	var token = resdata.access_token;

        	localStorage.setItem('access_token', token);
	      
     })  
    
   }]);



ViewApp.controller('LineController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike', 
	function($scope, $http, $location, $httpParamSerializerJQLike){

	  var targetpath = '/chocopon0899gmailcom-dev/sb/openapi/bodyinformation';

	    $http({
		       method: 'POST',
		       headers : {
		           'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
		       },
		       transformRequest: $httpParamSerializerJQLike,
		       url: url_apis,
		       data: { clientid: clientid, access_token: token,  targetpath: targetpath_body}
		     }).then(function successCallback(response){

		          resdata = response.data;

		          var tmp = dataArray(resdata);
			      var Labels = createLabels(tmp);
			      var Data1 = createData1(tmp);
				  var Data2 = createData2(tmp);

			      var ctx = document.getElementById("myLine1");
				  var myChart = new Chart(ctx, {
				        type: 'line',
				        data: {
				            labels: Labels,
				            datasets: [{
				                label: 'Height',
				                data: Data1,
						        backgroundColor: 'rgba(75, 192, 192, 0.2)',
						        borderColor: 'rgba(75, 192, 192, 1)',
				                borderWidth: 3
				            }]
				        },
				        options: {
					        maintainAspectRatio: true,
				            scales: {
				    	        xAxes: [
				    	          {
				    	           ticks: {
				    	              autoSkip: true,
				    	              maxTicksLimit: 10
				    	               }
				                }]
				            }
				        }
				    });

			      var ctx = document.getElementById("myLine2");
				  var myChart = new Chart(ctx, {
				        type: 'line',
				        data: {
				            labels: Labels,
				            datasets: [{
				                label: 'Weight',
				                data: Data2,
						        backgroundColor: 'rgba(153, 102, 255, 0.2)',
						        borderColor: 'rgba(153, 102, 255, 1)',
				                borderWidth: 3
				            }]
				        },
				        options: {
					        maintainAspectRatio: true,
				            scales: {
				    	        xAxes: [
				    	          {
				    	           ticks: {
				    	              autoSkip: true,
				    	              maxTicksLimit: 10
				    	               }
				                }]
				            }
				        }
				    });
				  
				  
			      var date = new Date();
			      $scope.time1 = date.toLocaleString('en-GB');
			      
   	        }, function errorCallback(response) {
   	        $location.path('/error');
		  });
	  	  
 }]);

ViewApp.controller('BarController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike', 
	function($scope, $http, $location, $httpParamSerializerJQLike){

		  $http({
			       method: 'POST',
			       headers : {
			           'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
			       },
			       transformRequest: $httpParamSerializerJQLike,
			       url: url_apis,
			       data: { clientid: clientid, access_token: token, targetpath: targetpath_body}

		        }).then(function successCallback(response){

			        resdata = response.data;

			        var tmp = dataArray(resdata);
				    var Labels = createLabels(tmp);
				    var Data3 = createData3(tmp);
				      
				    var ctx = document.getElementById("myBar");
				    var myChart = new Chart(ctx, {
				        type: 'bar',
				        data: {
				            labels: Labels,
				            datasets: [{
				                label: 'BMI',
				                data: Data3,
				                backgroundColor: 'rgba(255, 206, 86, 0.2)',
				                borderColor: 'rgba(255, 206, 86, 1)',
				                borderWidth: 3
				            }]
				        },
				        options: {
					        maintainAspectRatio: false,
				            scales: {
				    	        xAxes: [
				    	          {
				    	           ticks: {
				    	              autoSkip: true,
				    	              maxTicksLimit: 10
				    	               }
				                }]
				            }
				        }
				    });
				    
				    var date = new Date();
				    $scope.time3 = date.toLocaleString('en-GB');

			     }, function errorCallback(response) {
		   	   	     $location.path('/error');
				      
			  })
	  	  
}]);

ViewApp.controller('RadarController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike', 
	function($scope, $http, $location, $httpParamSerializerJQLike){


		$http({
		       method: 'POST',
		       headers : {
		           'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
		       },
		       transformRequest: $httpParamSerializerJQLike,
		       url: url_apis,
			   data: { clientid: clientid, access_token: token, targetpath: targetpath_blood}
			   
		        }).then(function successCallback(response){

			        resdata = response.data;

			        var GTP = resdata[0].gtp;
			        var HDL = resdata[0].hdl;
			        var LDL = resdata[0].ldl;
			        var TG = resdata[0].tg;
			        var FPG = resdata[0].fpg;

			        var ctx = document.getElementById("myRadar");
			        var myChart = new Chart(ctx, {
			            type: 'radar',
			            data: {
			                labels: ["γ-GTP", "HDL", "LDL", "TG", "FPG"],
			                datasets: [{
			                    label: 'Your Data',
			                    data: [GTP, HDL, LDL, TG, FPG ],
					            borderColor: [
					                'rgba(255, 99, 132, 1)',
					            ],
					            backgroundColor: [
					                'rgba(255, 99, 132, 0.2)'
					            ],
			                    pointStyle: "circle",                 
			                    pointRadius: 3,                       
					            borderWidth: 3,
			                    pointBorderColor: "rgba(255, 99, 132, 1)",              
			                    pointBorderWidth: 2,                  
			                    pointBackgroundColor: "rgba(255, 99, 132, 0.2)"  
			                },{
					        label: 'Standard Value',
		                    data: [50, 79.5, 89.5, 89.5, 99 ],
				            borderColor: [
				                'rgba(54, 162, 235, 1)',
				            ],
				            backgroundColor: [
				                'rgba(54, 162, 235, 0.2)',
				            ],
		                    pointStyle: "circle",                 
		                    pointRadius: 3,                       
				            borderWidth: 3,
		                    pointBorderColor: "rgba(54, 162, 235, 1)",              
		                    pointBorderWidth: 2,                  
		                    pointBackgroundColor: "rgba(54, 162, 235, 0.2)"  }]
			            },
			            options: {
					        maintainAspectRatio: false,
					    	tooltips: {
				                mode: 'point'
				            },
					        scale: {
				               pointLabels: {      
				                    fontSize: 15    
				                },
				                ticks: {
					                 stepSize: 20,
					                 min:10,
					                 max:100
					             }
					        }
			            }
			        });			        

			        var date = new Date();
				    $scope.time4 = date.toLocaleString('en-GB');

			     }, function errorCallback(response) {
		   	   	     $location.path('/error');
				      
			  })
  	  
  }]);
