'use strict';

var imageApp = angular.module('imageApp', ['infinite-scroll', 'ngResource']);

imageApp.controller('ImageLoaderController', function($scope, ImageLoader) {
    $scope.imageLoader = new ImageLoader();
});
