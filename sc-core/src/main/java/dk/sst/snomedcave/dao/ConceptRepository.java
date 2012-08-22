package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.Concept;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface ConceptRepository extends GraphRepository<Concept> {

    Concept getByFullyspecifiedName(String fullyspecifiedName);

    Concept getByConceptId(String conceptId);

    List<Concept> findByFullyspecifiedNameLike(String fullyspecifiedName);
}
