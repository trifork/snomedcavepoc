package dk.sst.snomedcave.model;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Drug extends NodeObject {

    @Indexed
    private String drugId;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "DrugFull", fieldName = "drugName")
    private String drugName;
    @Indexed(indexType = IndexType.FULLTEXT, indexName = "DrugFull", fieldName = "substance")
    private String substance;

    @Fetch
    private Concept refersTo;

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public Concept getRefersTo() {
        return refersTo;
    }

    public void setRefersTo(Concept refersTo) {
        this.refersTo = refersTo;
    }
}
