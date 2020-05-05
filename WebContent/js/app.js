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



ViewApp.controller('ViewController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike', 
	function($scope, $http, $location, $httpParamSerializerJQLike){

	  var method = "POST";	
	  var url = 'api/resources/token';	

	  var clientid = 'a643943f-fd85-4801-9bd4-6c79d3e1d3c2';
	  var authcode =$location.search()["code"];
	  
	  $http({
          method: method,
          headers : {
              'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
          },
          transformRequest: $httpParamSerializerJQLike,
          url: url,
          data: { clientid: clientid, authcode: authcode}
        }).then(function successCallback(response){

        	resdata = response.data;
	      	var tmp = dataArray(resdata); 
	  	    var tmpLabels = [], tmpData1 = [], tmpData2 = [], tmpData3 = [];
	  	    
			for (var row in tmp){
			   tmpLabels.push(tmp[row][0]);
			   tmpData1.push(tmp[row][1])
			   tmpData2.push(tmp[row][2])
				     
			   var bmi = tmp[row][2]/((tmp[row][1]/100)*(tmp[row][1]/100));
			   var bmi = Math.round(bmi*100)/100;
			   tmpData3.push(bmi)

		    }

		  $scope.labels = tmpLabels;
	      $scope.data = [tmpData1];
	      
	      $scope.datasetOverride = [{
	    	  label: "Height",
	          borderWidth: 3,
	          backgroundColor: 'rgba(60, 160, 220, 0.3)',
	          borderColor: 'rgba(60, 160, 220, 0.8)'
	       }];
	      
		  $scope.labels2 = tmpLabels;
	      $scope.data2 = [tmpData2];
	      $scope.datasetOverride2 = [{
	    	  label: "Weight",
	          borderWidth: 3,
	          backgroundColor: 'rgba(60, 190, 20, 0.3)',
	          borderColor: 'rgba(60, 190, 20, 0.8)'
           }];

	      
	      $scope.colors3 = ['#ff6384'];
	      $scope.labels3 = tmpLabels;
	      $scope.data3 = [tmpData3];
	      
	      $scope.datasetOverride3 = [{
	    	  label: "BMI",
	          borderWidth: 1,
	          type: 'bar'
	       }];
	      
     })  

	  
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

}]);