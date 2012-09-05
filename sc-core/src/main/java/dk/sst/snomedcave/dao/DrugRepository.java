package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.Drug;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.List;

public interface DrugRepository extends GraphRepository<Drug> {
    Drug getByDrugId(String drugId);

    List<Drug> findByNameLike(String name);

}
