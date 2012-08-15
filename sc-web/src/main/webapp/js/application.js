// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

//var treeHtml = "<span>{{concept.name}}</span> <ul class=\"unstyled\"> <li ng-repeat=\"child in concept.childs\"> <a href=\"#"><i class="icon-plus-sign"></i></a> <div concept="child"></div> </li> </ul>"
var treeHtml = "<span>{{concept.name}}</span><ul <li ng-repeat=\"child in concept.childs\"><div concept=\"child\"></div></li></ul>"

module.controller("ConceptCtrl", function($scope) {
    $scope.findConcept = function () {
        $scope.concept = {name:"TEST: " + $scope.conceptName, childs:[
            {name:"Concept a", childs:[
                {name:"Concept c"}

            ]},
            {name:"Concept b", childs:[]}
        ]
        }
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