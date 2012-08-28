package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.model.Concept;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

public class ConceptNode {
    String conceptId;
    String name;
    List<ConceptNode> childs = new ArrayList<ConceptNode>();
    boolean hasChilds;

    public ConceptNode(String conceptId, String name, Collection<ConceptNode> childs) {
        this.conceptId = conceptId;
        this.name = name;
        this.childs = new ArrayList<ConceptNode>(childs);
    }

    public ConceptNode(String conceptId, String name, ConceptNode... childs) {
        this(conceptId, name, asList(childs));
    }

    public ConceptNode(Concept concept) {
        conceptId = concept.getConceptId();
        name = concept.getFullyspecifiedName();
        hasChilds = concept.getChilds() != null && concept.getChilds().size() > 0;
    }
}
