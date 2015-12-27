(function(angular) {
  'use strict';

  // factory
  angular
    .module('app.backend', [])
    .factory('backendService', ['$http', backendService]);


  function backendService($http) {

    var service = {
      getUser: getUser
    };
    return service;

    ////////////

    /**
     * return the overview beats
     * @returns {HttpPromise}
     */
    function getUser() {
      return $http.get('/dashboard/user');
    }
  }
  ;
  
})(angular);