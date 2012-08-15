// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

//var treeHtml = "<span>{{concept.name}}</span> <ul class=\"unstyled\"> <li ng-repeat=\"child in concept.childs\"> <a href=\"#"><i class="icon-plus-sign"></i></a> <div concept="child"></div> </li> </ul>"
var treeHtml = "<span>{{concept.name}}</span><ul <li ng-repeat=\"child in concept.childs\"><div concept=\"child\"></div></li></ul>"

module.controller("ConceptCtrl", function($scope, $http) {
    $scope.findConcept = function () {
        $http.get("/concepts/search?query=" + $scope.conceptName).success(function(data, status) {
            $scope.concept = data
        })
    }
});

module.directive("concept", function($compile) {
    return {
        scope: {
            concept: "="
        },
        link: function(scope, elem, attrs) {
            return elem.append($compile(treeHtml)(scope))
        }
    };
});