package dk.sst.snomedcave.model;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

public class Concept {
    private String name;
    private Set<Concept> subConcepts = new HashSet<Concept>();


    public Concept(String name, Concept... subConcepts) {
        this(name, asList(subConcepts));
    }

    public Concept(String name, Collection<Concept> subConcepts) {
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

}
