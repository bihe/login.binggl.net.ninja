(function(angular) {
  'use strict';

  angular.module('app.variables', [
    
  ])

  .directive('appYear', [function(version) {
    return function(scope, elm, attrs) {
      elm.text(window.moment().format('YYYY'));
    };
  }])
  
  .directive('appTitle', [function(version) {
    return function(scope, elm, attrs) {
      elm.text('login.binggl.net');
    };
  }])
  
  ;
  
})(angular);

