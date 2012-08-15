function ConceptCtrl($scope) {
    $scope.findConcept = function() {
        $scope.concept = {name: "TEST: " + $scope.conceptName, childs: [{name: "Concept a", childs: 3}, {name: "Concept b", childs: 0}]}
    }
}