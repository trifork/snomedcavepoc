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
    private String name;

    @Indexed
    private long conceptId;

    private Set<Concept> subConcepts = new HashSet<Concept>();

    public Concept() {
        //Spring-data neo4j wants a no-arg constructor
    }

    public Concept(long conceptId, String name, Concept... subConcepts) {
        this(conceptId, name, asList(subConcepts));
    }

    public Concept(long conceptId, String name, Collection<Concept> subConcepts) {
        this.conceptId = conceptId;
        this.name = name;
        this.subConcepts = new HashSet<Concept>(subConcepts);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Concept concept) {
        subConcepts.add(concept);
    }

    public Set<Concept> getSubConcepts() {
        return subConcepts;
    }

    public String toJson() {
        return String.format(
                "{\"name\": \"%s\", \"childs\": [%s]}",
                name,
                join(CollectionUtils.collect(subConcepts, new Transformer<Concept, Object>() {
                    @Override
                    public Object transform(Concept concept) {
                        return concept.toJson();
                    }
                }), ","));
    }

    public long getConceptId() {
        return conceptId;
    }
}
