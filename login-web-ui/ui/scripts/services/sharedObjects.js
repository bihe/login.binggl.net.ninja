(function() {
  'use strict';

  //service
  angular
  	.module('app.objects', [])
  	.service('sharedObjects', ['$q', 'backendService', sharedObjects ]);

  function sharedObjects($q, backendService) {
   
	var user = undefined;
	  
	return {
	  getUser : function() {
		var deferred = $q.defer();
		
		if(user) {
		  deferred.resolve(user);
		} else {
		  backendService.getUser().success(function(data) {
		    user = data;
	        deferred.resolve(data);
		  }).error( function(data, status, headers) {
		    console.log('Error: ' + data);
			deferred.reject('Error: ' + data);
			sweet.show(
			{
			      title: 'An error occured!',
			      text: 'Could not load data from backend. Received status: ' + status,
			      type: 'error',
			      showCancelButton: false,
			      confirmButtonColor: '#DD6B55',
			      confirmButtonText: 'Ok',
			      closeOnConfirm: true
			    }, function() {
			    	window.location = '/login';
			    }
			  );
			});	
		 }
		 return deferred.promise;
	  }
	}
  }
})(angular);