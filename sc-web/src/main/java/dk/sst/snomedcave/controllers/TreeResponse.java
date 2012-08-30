package dk.sst.snomedcave.controllers;


public class TreeResponse {
    ConceptNode root;

    public TreeResponse(ConceptNode node) {
        root = node;
    }

    public ConceptNode getRoot() {
        return root;
    }
}
