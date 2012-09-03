package dk.sst.snomedcave.model;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class ConceptRelation extends NodeObject {
    private String relationId;
    private Concept type;

    private Concept child;

    public ConceptRelation() {
    }

    public ConceptRelation(Concept type, Concept child) {
        this.type = type;
        this.child = child;
    }

    //<editor-fold desc="GettersAndSetters">

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public Concept getChild() {
        return child;
    }

    public void setChild(Concept child) {
        this.child = child;
    }

    public Concept getType() {
        return type;
    }

    public void setType(Concept type) {
        this.type = type;
    }

    //</editor-fold>
}
