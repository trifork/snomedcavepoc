package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.CaveRegistration;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface CaveRegistrationRepository extends GraphRepository<CaveRegistration> {
}
