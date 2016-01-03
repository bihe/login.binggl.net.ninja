(function(angular) {
  'use strict';

  angular
    .module('app.auth')
    .controller('authController', ['backendService', 'sweet', authController]);

  /**
   * the main logic of the dashboard
   * @constructor
   */
  function authController(backendService, sweet) {
    /* jshint validthis: true */
    var vm = this;

    load();


    ////////////

    /**
     * load the user
     */
    function load() {
      backendService.getUser().success(function(data) {
        vm.user = data;
      }).error( function(data, status, headers) {
        console.log('Error: ' + data);
        sweet.show('An error occured!', 'Could not load data from backend. Received status: ' + status, 'error');
        
      });
    }
  }
})(angular);