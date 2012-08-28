package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.ConceptRelation;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ConceptRelationRepository extends GraphRepository<ConceptRelation> {

}
