package dk.sst.snomedcave.model;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Drug extends NodeObject {

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "DrugFull", fieldName = "name")
    private String name;

    @Fetch
    private Concept refersTo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Concept getRefersTo() {
        return refersTo;
    }

    public void setRefersTo(Concept refersTo) {
        this.refersTo = refersTo;
    }
}
