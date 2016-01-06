(function(angular) {
  'use strict';

  angular
    .module('app.auth')
    .controller('authController', ['sweet', 'sharedObjects', authController]);

  /**
   * the main logic of the dashboard
   * @constructor
   */
  function authController(sweet, sharedObjects) {
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