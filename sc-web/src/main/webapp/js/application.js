// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

var treeHtml = "<span>{{concept.fullyspecifiedName}}</span>" +
    "<ul>" +
        "<li ng-repeat=\"child in concept.childs\">" +
            "<a href=\"#\"><i class=\"icon-plus-sign\"></i></a>&nbsp;" +
            "<span concept=\"child\"></span>" +
        "</li>" +
    "</ul>"

module.controller("IdentityCtrl", function($scope, $log, $http) {
    $scope.findIdentity = function() {
        $scope.identityResult = $scope.identityCpr
    }
    $scope.selectRegistration = function(number) {
        $scope.selectedRegistration = "Some" + number
    }

    $scope.findConcept = function () {
        var conceptName = $scope.conceptName;
        $log.info("Will lookup " + conceptName)
        $http.get("/concepts/search?query=" + conceptName).success(function(data, status) {
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
        link: function(scope, elm, attrs) {
            return elm.append($compile(treeHtml)(scope))
        }
    };
});

module.directive('typeahead', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attr, ngModel) {
            $(elm).typeahead({
                    source: function(query, process) {
                        process([
                            "Allergi over for lægemidler",
                            "Allergier over for billige lægemidler",
                            "Allergier over for ampicillin",
                            "Allergier over for amoxecillin",
                            "Allergier over for hvide lægemidler",
                            "Allergier over for Panodil",
                            "Allergier over for placebo"
                        ])
                    },
                    updater: function(item) {
                        scope.$apply(function() {
                            ngModel.$setViewValue(item);
                        })
                        return item
                    }
                });
        }
    };
});
