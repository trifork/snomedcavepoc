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

module.controller("IdentityCtrl", function($scope, $log) {
    $scope.findIdentity = function() {
        $scope.identityResult = $scope.identityCpr
    }
    $scope.selectRegistration = function(number) {
        $scope.selectedRegistration = "Some" + number
    }

    $scope.findConcept = function () {
        $http.get("/concepts/search?query=" + $scope.conceptName).success(function(data, status) {
            $scope.concept = data
        })
    }
    $scope.$watch("identityResult", function(newValue, oldValue) {
        $scope.selectedRegistration = undefined
        if (newValue) {
            $("#identityResult").slideUp()
            $("#identityResult").slideDown()
        }
        else {
            $("#identityResult").slideUp()
        }
    })
    $scope.$watch("selectedRegistration", function(newValue, oldValue) {
        if (newValue) {
            $("#conceptBrowser").slideUp()
            $("#conceptBrowser").slideDown()
        }
        else {
            $("#conceptBrowser").slideUp()
        }
    })
})

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

$(document).ready(function() {
    $("#conceptSearch").typeahead({
        source: function(query, process) {
            process(["Alabama","Alaska","Arizona","Arkansas","California","Colorado","Connecticut","Delaware","Florida","Georgia","Hawaii","Idaho","Illinois","Indiana","Iowa","Kansas","Kentucky","Louisiana","Maine","Maryland","Massachusetts","Michigan","Minnesota","Mississippi","Missouri","Montana","Nebraska","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Dakota","North Carolina","Ohio","Oklahoma","Oregon","Pennsylvania","Rhode Island","South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","West Virginia","Wisconsin","Wyoming"])
        }
    })
})
