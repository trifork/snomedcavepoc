package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.Identity;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface IdentityRepository extends GraphRepository<Identity> {

    Identity getByCpr(String cpr);
}
