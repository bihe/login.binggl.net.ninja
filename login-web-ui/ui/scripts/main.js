(function(window, angular) {
  'use strict';

  angular
    .module('loginApp', [ 'ui.router'
      , 'app.variables'
      , 'app.backend'
      , 'app.dashboard'
      , 'hSweetAlert'                   // sweet alert: native alert replacement

    ])
    .constant('_', window._)
    .constant('moment', window.moment)
    .config(['$stateProvider', '$urlRouterProvider','$compileProvider',
      function ($stateProvider, $urlRouterProvider, $compileProvider) {
        //
        // for any unmatched url, redirect to /
        $urlRouterProvider.otherwise("/");

        //
        // defined states
        $stateProvider
        .state('initial', {
          url: '/',
          templateUrl: "views/dashboard.html"
        })

        //
        // speedup
        $compileProvider.debugInfoEnabled(false);

      }])
    .run(['$rootScope', '$location',
      function($rootScope, $location) {
        $rootScope.$on('$routeChangeStart', function(event, next, current) {

        });
      }]);
})(window, angular);