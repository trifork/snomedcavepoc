// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);

var treeHtml = "<span><a ng-click='expandToggle(concept)' ng-show='concept.hasChilds'><i class=\"icon-{{plusMinus(concept)}}-sign\"></i></a>&nbsp;" +
        "<span ng-click='selectConcept(concept)' ng-class=\"{selected: concept.conceptId == selectedConceptId}\">{{concept.name}}</span>" +
    "</span>" +
    "<ul class='unstyled' style='padding-left: 20px;'>" +
        "<li ng-repeat=\"child in concept.childs\">" +
            "<span concept=\"child\"></span>" +
        "</li>" +
    "</ul>"

module.controller("IdentityCtrl", function($scope, $location, $log, $http) {
    $scope.findIdentity = function() {
        $http.get("/identities/" + $scope.identityCpr).success(function(data, status) {
            $scope.identity = data;
        })
    }
    $scope.saveIdentity = function() {
        alert("Should save...")
    }

    $scope.selectRegistration = function(registration) {
        $log.info("Selecting registration=" + registration.nodeId)
        $scope.selectedRegistration = registration
    }
    $scope.deleteRegistration = function(registration) {
        $scope.identity.caveRegistrations = _.without($scope.identity.caveRegistrations, registration)
    }
    $scope.addRegistration = function() {
        var registration = {};
        $scope.identity.caveRegistrations.push(registration)
        $scope.selectedRegistration = registration
    }
    $scope.hideRegistration = function() {
        $scope.selectedRegistration = undefined
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
            $scope.selectedRegistration = newValue
            $("#conceptBrowser").slideDown()

        }
        else {
            $("#conceptBrowser").slideUp()
        }
    })
})

module.directive("tooltip", function() {
    return function(scope, element, attrs) {
        var placement = attrs.tooltipPlacement;
        scope.$watch(
            function() {return attrs.tooltip},
            function(newValue) {
                element.tooltip({
                    title: newValue,
                    placement: placement
                })
            })
    }
})

module.directive("caveRegistration", function($http, $log) {
    return function(scope, element, attrs) {
        var registration;

        scope.$watch(attrs.caveRegistration, function(value) {
            registration = value;
            update()
        })

        function update() {
            scope.registration = registration
            if (registration) {
                getConcept(registration.allergyId)
            }
            else {
                scope.allergyTree = {}
            }
        }

        scope.findDrug = function(query) {
            scope.allergyTree = undefined;
            $log.info("Will lookup " + query)
            $http.get("/drugs/concepttree?name=" + query).success(function(drug, status) {
                getConcept(drug.allergyId)
            })
        }

        function getConcept(allergyId) {
            selectedConceptId = allergyId
            $http.get("/concepts/tree?id=" + allergyId).success(function(data, status) {
                scope.allergyTree = data
            })
        }
    }
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
        $scope.selectConcept = function(newConcept) {
            $scope.selectedConceptId = newConcept.conceptId;
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
