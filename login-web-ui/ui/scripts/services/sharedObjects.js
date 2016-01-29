(function() {
  'use strict';

  //service
  angular
  	.module('app.objects', [])
  	.service('sharedObjects', ['$q', 'sweet', 'backendService', sharedObjects ]);

  function sharedObjects($q, sweet, backendService) {
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
          
          sweet.show({
            title: 'An error occured!',
            text: 'Could not load data from backend. Received status: ' + status,
            type: 'error',
            showCancelButton: false,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Ok',
            closeOnConfirm: true
          }, function() {
            deferred.reject('Error: ' + data);
            window.location = '/login';
          });
        });	
      }
      return deferred.promise;
      }
    }
  }
})(angular);