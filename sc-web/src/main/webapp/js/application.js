// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

var treeHtml = "<span><a ng-click='expandToggle(concept)' ng-show='concept.hasChilds'><i class=\"icon-{{plusMinus(concept)}}-sign\"></i></a>&nbsp;{{concept.name}}, {{concept.conceptId}}</span>" +
    "<ul>" +
        "<li ng-repeat=\"child in concept.childs\">" +
            "<span concept=\"child\"></span>" +
        "</li>" +
    "</ul>"

module.controller("IdentityCtrl", function($scope, $log, $http) {
    $scope.findIdentity = function() {
        $scope.identityResult = $scope.identityCpr
    }
    $scope.selectRegistration = function(number) {
        $scope.selectedRegistration = number
    }

    $scope.findConcept = function () {
        var conceptName = $scope.conceptName;
        $log.info("Will lookup " + conceptName)
        $http.get("/concepts/tree?id=" + conceptName).success(function(data, status) {
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
module.directive("concept", function($compile, $http) {
    function ConceptCtrl($scope) {
        $scope.expandToggle = function(concept) {
            if (concept.childs.length == 0) {
                $http.get("/concepts/node?id=" + concept.conceptId).success(function(data, status) {
                    concept.childs = data.childs;
                })
            }
            else {
                concept.childs = []
            }
        }
        $scope.plusMinus = function(concept) {
            if (concept && concept.childs.length > 0) {
                return "minus"
            }
            else {
                return "plus"
            }
        }
    }

    return {
        controller: ConceptCtrl,
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
                            "Allergier over for placebo",
                            "Penicillin overdose (disorder)",
                            "Allergy to penicillin (disorder)"
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
