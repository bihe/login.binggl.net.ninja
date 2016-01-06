(function(angular) {
  'use strict';

  angular
    .module('app.dashboard')
    .controller('userController', ['sweet', 'sharedObjects', userController]);

  /**
   * the main logic of the dashboard
   * @constructor
   */
  function userController(sweet, sharedObjects) {
    /* jshint validthis: true */
    var vm = this;

    load();


    ////////////

    /**
     * load the user
     */
    function load() {
      sharedObjects.getUser().then(function(data) {
        vm.user = data;
      }, function(reason) {
        sweet(reason, 'error');
      });
    }
  }
})(angular);