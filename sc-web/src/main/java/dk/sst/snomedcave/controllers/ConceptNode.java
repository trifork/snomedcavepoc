package dk.sst.snomedcave.controllers;

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
        hasChilds = this.childs.size() > 0;
    }

    public ConceptNode(String conceptId, String name, ConceptNode... childs) {
        this(conceptId, name, asList(childs));
    }

    public ConceptNode(String conceptId, String conceptName, boolean hasChilds) {
        this(conceptId, conceptName);
        this.hasChilds = hasChilds;
    }
}
