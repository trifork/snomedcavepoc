package dk.sst.snomedcave.model;

import org.apache.commons.lang3.builder.*;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

@NodeEntity
public class Concept extends NodeObject {

    @Indexed
    String conceptId;

    int status;

    @Indexed
    String fullyspecifiedName;

    String ctv3Id;

    String snomedId;

    boolean primitive;

    @Fetch @RelatedTo
    private Set<ConceptRelation> childs = new HashSet<ConceptRelation>();

    public Concept() {
        //Spring-data neo4j wants a no-arg constructor
    }

    public Concept(String conceptId, String fullyspecifiedName, ConceptRelation... parents) {
        this(conceptId, fullyspecifiedName, asList(parents));
    }

    public Concept(String conceptId, String fullyspecifiedName, Collection<ConceptRelation> childs) {
        this.conceptId = conceptId;
        this.fullyspecifiedName = fullyspecifiedName;
        this.childs = new HashSet<ConceptRelation>(childs);
    }

    public void add(ConceptRelation parent) {
        childs.add(parent);
    }

    public Set<ConceptRelation> getChilds() {
        return childs;
    }

    public String getConceptId() {
        return conceptId;
    }

    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFullyspecifiedName() {
        return fullyspecifiedName;
    }

    public void setFullyspecifiedName(String fullyspecifiedName) {
        this.fullyspecifiedName = fullyspecifiedName;
    }

    public String getCtv3Id() {
        return ctv3Id;
    }

    public void setCtv3Id(String ctv3Id) {
        this.ctv3Id = ctv3Id;
    }

    public String getSnomedId() {
        return snomedId;
    }

    public void setSnomedId(String snomedId) {
        this.snomedId = snomedId;
    }

    public boolean isPrimitive() {
        return primitive;
    }

    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("conceptId", conceptId).
                append("status", status).
                append("fullyspecifiedName", fullyspecifiedName).
                append("ctv3Id", ctv3Id).
                append("snomedId", snomedId).
                append("primitive", primitive).
                append("childs", childs).
                toString();
    }
}
