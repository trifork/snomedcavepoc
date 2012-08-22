// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

var treeHtml = "<span>{{concept.fullyspecifiedName}}</span>" +
    "<ul>" +
        "<li ng-repeat=\"child in concept.childs\">" +
            "<a href=\"#\"><i class=\"icon-plus-sign\"></i></a>&nbsp;" +
            "<span concept=\"child\"></span>" +
        "</li>" +
    "</ul>"
//var treeHtml = "<span>{{concept.name}}</span><ul <li ng-repeat=\"child in concept.childs\"><div concept=\"child\"></div></li></ul>"

module.controller("IdentityCtrl", function($scope) {
    $scope.findIdentity = function() {
        $("#identityResult").slideUp("slow")
        $("#identityResult").slideDown("slow")
    }
    $scope.selectRegistration = function(number) {
        alert("YEAH: " + number)
    }
})

module.controller("ConceptCtrl", function($scope, $http) {
    $scope.findConcept = function () {
        $http.get("/concepts/search?query=" + $scope.conceptName).success(function(data, status) {
            $scope.concept = data
        })
    }
});

//TODO: consider implementing @andershessellund's example https://groups.google.com/forum/?fromgroups#!topic/angular/I5Z5oglW6Xw%5B1-25%5D
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