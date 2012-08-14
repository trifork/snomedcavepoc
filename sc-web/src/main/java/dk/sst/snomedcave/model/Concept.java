package dk.sst.snomedcave.model;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Concept {
    @Indexed
    private String name;
}
