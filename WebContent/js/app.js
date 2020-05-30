var ViewApp = angular.module('ViewApp', ['ngRoute', 'chart.js']);

ViewApp.config(['$routeProvider', function($routeProvider){
    $routeProvider
    .when('/', {
      templateUrl: 'templates/menu.html',
      controller: 'MenuController'
    })
    .when('/menu', {
      templateUrl: 'templates/menu.html',
      controller: 'MenuController'
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
    .when('/error', {
      templateUrl: 'templates/error.html'
    })
    .otherwise({
      redirectTo: '/'
    });
}]);



ViewApp.controller('LoginController', ['$scope', '$http', '$window','$httpParamSerializerJQLike',
	 function($scope, $http, $window, $httpParamSerializerJQLike){
	   $scope.username = null;
	   $scope.mdusername = null;
	   $scope.mdemail = null;
	   
	   sessionStorage.removeItem('jwt');
	
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
    	        }).then(function successCallback(response){
    	        	var resdata = response.data;
    	        	var jwt = resdata.JWT;
    	        	sessionStorage.setItem('jwt', jwt);
    	        	
    	        	$window.location.href = 'main.html';
    	        }, function errorCallback(response) {
    	        	var sts = response.status;

    	        	if(sts == 400){
      	        	   $scope.login_message = 'Password is wrong.';
    	        	}else{
         	           $scope.login_message = 'Your ID does not exist.';
    	        	}
    	      });
    	};
    	
    	$scope.register = function(){
      	  var method = "POST";	
      	  var url = 'api/resources/register';	
      		
      	  $http({
      	          method: method,
      	          headers : {
                        'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
                    },
                    transformRequest: $httpParamSerializerJQLike,
      	          url: url,
      	          data: { username: $scope.mdusername, password: $scope.mdpassword }
      	        }).then(function successCallback(response){
      	        	$scope.register_message = 'Registerd your new ID. Please login.';
      	        }, function errorCallback(response) {
      	        	$scope.register_message = 'Error occurred. Please check your input.';
      	        });
      	};

    }]);


ViewApp.controller('MenuController', ['$http', '$location','$httpParamSerializerJQLike',
	function( $http, $location, $httpParamSerializerJQLike){
	
	var method = "POST";	
	var url = 'api/resources/check';	
	var jwt = sessionStorage.getItem('jwt');

	
	$http({
          method: method,
          headers : {
              'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
          },
          transformRequest: $httpParamSerializerJQLike,
          url: url,
          data: { jwt: jwt}
        }).then(function successCallback(response){
	    }, function errorCallback(response) {
   	        $location.path('/error');
     })  
    
   }]);



ViewApp.controller('ConnectController', ['$http', '$location','$httpParamSerializerJQLike',
	function( $http, $location, $httpParamSerializerJQLike){
	var clientid = 'a643943f-fd85-4801-9bd4-6c79d3e1d3c2';
	var authcode =$location.search()["code"];
	
	var method = "POST";	
	var url = 'api/resources/token';	
	var jwt = sessionStorage.getItem('jwt');

	
	$http({
          method: method,
          headers : {
              'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
          },
          transformRequest: $httpParamSerializerJQLike,
          url: url,
          data: { clientid: clientid, authcode: authcode, jwt: jwt}
        }).then(function successCallback(response){
        	var resdata = response.data;
        	var token = resdata.access_token;
        	localStorage.setItem('access_token', token);
	      
     })  
    
   }]);



ViewApp.controller('LineController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike', 
	function($scope, $http, $location, $httpParamSerializerJQLike){

	  var method = "POST";	
	  var url = 'api/resources/resourse';	

	  var clientid = 'a643943f-fd85-4801-9bd4-6c79d3e1d3c2';
	  var token = localStorage.getItem('access_token');
	  var jwt = sessionStorage.getItem('jwt');

	  getinfo1(clientid, token);
	  getinfo2(clientid, token);
	  
	  $scope.renew1 = function(){
		  getinfo1(clientid, token);
	  };

	  $scope.renew2 = function(){
		  getinfo2(clientid, token);
	  };

	  function getinfo1(clientid, token){
		  
			 $http({
		       method: method,
		       headers : {
		           'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
		       },
		       transformRequest: $httpParamSerializerJQLike,
		       url: url,
		       data: { clientid: clientid, access_token: token,  jwt: jwt}
		     }).then(function successCallback(response){

		          resdata = response.data;

		          var tmp = dataArray(resdata);
			      var Labels = createLabels(tmp);
			      var Data1 = createData1(tmp);

				  $scope.labels = Labels;
			      $scope.data = [Data1];
			      
			      $scope.datasetOverride = [{
			    	  label: "Height",
			          borderWidth: 3,
			          backgroundColor: 'rgba(60, 160, 220, 0.3)',
			          borderColor: 'rgba(60, 160, 220, 0.8)'
			       }];
			      
			      $scope.options = {
			    	      scales: {
			    	        xAxes: [
			    	          {
			    	           ticks: {
			    	              autoSkip: true,
			    	              maxTicksLimit: 10
			    	               }
			    	          }
			    	        ]
			    	      }
			       };
			      
			      var date = new Date();
			      $scope.time1 = date.toLocaleString('en-GB');
			      
   	        }, function errorCallback(response) {
   	        $location.path('/error');
		  })
		};

     function getinfo2(clientid, token){
			  
		$http({
		       method: method,
		       headers : {
		           'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
		       },
		       transformRequest: $httpParamSerializerJQLike,
		       url: url,
		       data: { clientid: clientid, access_token: token, jwt: jwt}
		     }).then(function successCallback(response){

		         resdata = response.data;

		         var tmp = dataArray(resdata);
			     var Labels = createLabels(tmp);
			     var Data2 = createData2(tmp);

			     $scope.labels2 = Labels;
			     $scope.data2 = [Data2];
			     $scope.datasetOverride2 = [{
			    	  label: "Weight",
			          borderWidth: 3,
			          backgroundColor: 'rgba(60, 190, 20, 0.3)',
			          borderColor: 'rgba(60, 190, 20, 0.8)'
		        }];

			     $scope.options2 = {
			    	      scales: {
			    	        xAxes: [
			    	          {
			    	           ticks: {
			    	              autoSkip: true,
			    	              maxTicksLimit: 10
			    	               }
			    	          }
			    	        ]
			    	      }
			     };

			     var date = new Date();
			     $scope.time2 = date.toLocaleString('en-GB');

		     }, function errorCallback(response) {
	   	   	        $location.path('/error');
			      
		      })
		 };
	  	  
}]);

ViewApp.controller('BarController', ['$scope', '$http', '$location', '$httpParamSerializerJQLike', 
	function($scope, $http, $location, $httpParamSerializerJQLike){

	  var method = "POST";	
	  var url = 'api/resources/resourse';	

	  var clientid = 'a643943f-fd85-4801-9bd4-6c79d3e1d3c2';
	  var token = localStorage.getItem('access_token');
	  var jwt = sessionStorage.getItem('jwt');

	  getinfo3(clientid, token);
	  
	  $scope.renew3 = function(){
		  getinfo3(clientid, token);
	  };


	function getinfo3(clientid, token){
			  
				$http({
			       method: method,
			       headers : {
			           'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
			       },
			       transformRequest: $httpParamSerializerJQLike,
			       url: url,
			       data: { clientid: clientid, access_token: token, jwt: jwt}
			     }).then(function successCallback(response){

			        resdata = response.data;

			        var tmp = dataArray(resdata);
				    var Labels = createLabels(tmp);
				    var Data3 = createData3(tmp);
				      
				    $scope.colors3 = ['#ff6384'];
				    $scope.labels3 = Labels;
				    $scope.data3 = [Data3];
				      
				    $scope.datasetOverride3 = [{
				    	  label: "BMI",
				          borderWidth: 1,
				          type: 'bar'
				    }];
				    
				     $scope.options3 = {
				    	      scales: {
				    	        xAxes: [
				    	          {
				    	           ticks: {
				    	              autoSkip: true,
				    	              maxTicksLimit: 10
				    	               }
				    	          }
				    	        ]
				    	      }
				    };

				    
				    var date = new Date();
				    $scope.time3 = date.toLocaleString('en-GB');

			     }, function errorCallback(response) {
		   	   	     $location.path('/error');
				      
			  })
	  };
	  	  
}]);