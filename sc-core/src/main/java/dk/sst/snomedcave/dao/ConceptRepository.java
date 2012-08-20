package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.Concept;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ConceptRepository extends GraphRepository<Concept> {

    Concept getByFullyspecifiedName(String fullyspecifiedName);

    Concept getByConceptId(String conceptId);
}
