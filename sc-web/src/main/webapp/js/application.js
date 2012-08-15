function ConceptCtrl($scope) {
    $scope.findConcept = function() {
        $scope.concept = {name: "TEST: " + $scope.conceptName, childs: ["Concept a", "Concept b"]}
    }
}