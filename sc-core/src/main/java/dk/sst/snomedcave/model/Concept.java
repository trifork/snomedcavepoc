package dk.sst.snomedcave.model;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

@NodeEntity
public class Concept {
    @GraphId
    private Long nodeId;

    @Indexed
    long conceptId;

    int status;

    @Indexed
    String fullyspecifiedName;

    String ctv3Id;

    String snomedId;

    boolean primitive;

    private Set<Concept> subConcepts = new HashSet<Concept>();

    public Concept() {
        //Spring-data neo4j wants a no-arg constructor
    }

    public Concept(long conceptId, String fullyspecifiedName, Concept... subConcepts) {
        this(conceptId, fullyspecifiedName, asList(subConcepts));
    }

    public Concept(long conceptId, String fullyspecifiedName, Collection<Concept> subConcepts) {
        this.conceptId = conceptId;
        this.fullyspecifiedName = fullyspecifiedName;
        this.subConcepts = new HashSet<Concept>(subConcepts);
    }

    public void add(Concept concept) {
        subConcepts.add(concept);
    }

    public Set<Concept> getSubConcepts() {
        return subConcepts;
    }

    public long getConceptId() {
        return conceptId;
    }

    public void setConceptId(long conceptId) {
        this.conceptId = conceptId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
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

    public String toJson() {
        return String.format(
                "{\"fullyspecifiedName\": \"%s\", \"childs\": [%s]}",
                fullyspecifiedName,
                join(CollectionUtils.collect(subConcepts, new Transformer<Concept, Object>() {
                    @Override
                    public Object transform(Concept concept) {
                        return concept.toJson();
                    }
                }), ","));
    }
}
