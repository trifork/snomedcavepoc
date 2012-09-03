// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

var treeHtml = "<span><a ng-click='expandToggle(concept)' ng-show='concept.hasChilds'><i class=\"icon-{{plusMinus(concept)}}-sign\"></i></a>&nbsp;{{concept.name}}</span>" +
    "<ul class='unstyled' style='padding-left: 20px;'>" +
        "<li ng-repeat=\"child in concept.childs\">" +
            "<span concept=\"child\"></span>" +
        "</li>" +
    "</ul>"

module.controller("IdentityCtrl", function($scope, $log, $http) {
    $scope.findIdentity = function() {
        $http.get("/identities/" + $scope.identityCpr).success(function(data, status) {
            $scope.identity = data;
        })
    }
    $scope.selectRegistration = function(registration) {
        $log.info("Selecting registration=" + registration.nodeId)
        $scope.selectedRegistration = registration
    }

    $scope.findConcept = function () {
        var drugQuery = $scope.drugQuery;
        $log.info("Will lookup " + drugQuery)
        $http.get("/drugs/concepttree?name=" + drugQuery).success(function(data, status) {
            $scope.concept = data
        })
    }
    $scope.$watch("identity", function(newValue, oldValue) {
        $scope.selectedRegistration = undefined
        if (newValue) {
            $("#identity").slideUp()
            $("#identity").slideDown()
        }
        else {
            $("#identity").slideUp()
        }
    })
    $scope.$watch("selectedRegistration", function(newValue, oldValue) {
        if (newValue) {
            $("#conceptBrowser").hide()
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

module.directive('typeahead', function($http) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attr, ngModel) {
            $(elm).typeahead({
                    source: function(query, process) {
                        $http.get("/drugs/search?q=" + scope.drugQuery).success(function(data, status) {
                            process(data)
                        })
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
