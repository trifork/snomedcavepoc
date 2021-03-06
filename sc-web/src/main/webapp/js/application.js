// Declare app level module which depends on filters, and services
var module = angular.module('myApp', []);
//ng-class="{info: selectedRegistration == registration}"
var treeHtml = "<span>" +
    "<a ng-click='expandToggle(conceptTree)' ng-show='conceptTree.hasChilds'><i class=\"icon-{{plusMinus(conceptTree)}}-sign treeexpander\"></i></a>" +
        "<span ng-click='selectConcept(conceptTree)' ng-class=\"{selected: conceptTree.conceptId == selectedRegistration.allergyId, endconcept: !conceptTree.hasChilds}\" style='cursor: pointer;'>" +
            "<span ng-class='{foundconcept: conceptTree.conceptId == foundConcept}'>{{conceptTree.name}}</span>" +
        "</span>" +
    "</span>" +
    "<ul class='unstyled' style='padding-left: 20px;'>" +
        "<li ng-repeat=\"child in conceptTree.childs | orderBy:'name'\">" +
            "<span concept-tree=\"child\" selected-registration=\"selectedRegistration\" found-concept=\"foundConcept\"></span>" +
        "</li>" +
    "</ul>"

module.controller("IdentityCtrl", function($scope, $location, $log, $http, $timeout) {
    $scope.findIdentity = function() {
        $http.get("/identities/" + $scope.identityCpr).success(function(data, status) {
            $scope.identity = data;
        })
    }
    $scope.saveIdentity = function() {
        $http.put("/identities/" + $scope.identity.cpr, $scope.identity).success(function(data, status) {
            $(".savedindicator").addClass("animation")
            $(".savedindicator").addClass("active")
            $timeout(function () {
                $(".savedindicator").removeClass("active")
            }, 5000);
        })
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
            $("#identity").hide()
            $("#identity").slideDown()
        }
        else {
            $("#identity").hide()
        }
    })
    $scope.$watch("selectedRegistration", function(newValue, oldValue) {
        if (newValue) {
            $scope.allergyTree = undefined
            $("#registrationForm").hide()
            $("#registrationForm").slideDown("slow")

        }
        else {
            $("#registrationForm").hide()
        }
    })
})

module.directive("tooltip", function($log) {
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
        element.bind("click", function() {
            $log.info("Will destroy " + element);
            //element.tooltip('hide');
            $(".tooltip").detach()
        })
    }
})

module.directive("caveRegistration", function($http, $log) {
    return function(scope, element, attrs) {
        var registration;

        scope.$watch(attrs.caveRegistration, function(value) {
            registration = value;
            scope.drugQuery = ""
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

        scope.findDrug = function (query) {
            scope.allergyTree = undefined;
            $log.info("Will lookup " + query)
            $http.get("/drugs/concepttree?name=" + query).success(function (allergyId, status) {
                getConcept(allergyId)
            }).error(function(data, status) {
                    scope.foundConceptId = 0;
                    scope.allergyTree = []
                })
        }

        function getConcept(allergyId) {
            $http.get("/concepts/tree?id=" + allergyId).success(function(data, status) {
                scope.foundConceptId = allergyId
                scope.allergyTree = data
            })
        }

        scope.hasResult = function() {
            return scope.allergyTree != undefined && scope.allergyTree.length === 0;
        }
    }
})

//TODO: consider implementing @andershessellund's example https://groups.google.com/forum/?fromgroups#!topic/angular/I5Z5oglW6Xw%5B1-25%5D
module.directive("conceptTree", function($compile, $http) {
    function ConceptTreeCtrl($scope) {
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
            if (concept && angular.isArray(concept.childs) && concept.childs.length > 0) {
                return "minus"
            }
            else {
                return "plus"
            }
        }
        $scope.selectConcept = function(newConcept) {
            $scope.selectedRegistration.allergyId = newConcept.conceptId;
            $scope.selectedRegistration.allergyTerm = newConcept.name;
        }
    }

    return {
        controller: ConceptTreeCtrl,
        scope: {
            conceptTree: "=",
            selectedRegistration: "=",
            foundConcept: "="
        },
        link: function(scope, elm, attrs) {
            return elm.append($compile(treeHtml)(scope))
        }
    };
});

module.directive('typeahead', function($http, $log) {
    return {
        require: 'ngModel',
        link: function(scope, elm, attr, ngModel) {
            $(elm).typeahead({

                    source: function(query, process) {
                        $http.get("/drugs/search?q=" + query).success(function(data, status) {
                            if (query === scope.drugQuery) {
                                process(data)
                            }
                            else {
                                $log.log("Threw away response for \"" + query + "\", actual: " + scope.drugQuery)
                            }
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
