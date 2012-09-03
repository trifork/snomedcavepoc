package dk.sst.snomedcave.service;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.dao.IdentityRepository;
import dk.sst.snomedcave.model.CaveRegistration;
import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import dk.sst.snomedcave.model.Identity;
import org.apache.log4j.Logger;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;

import static java.util.Arrays.asList;

public class ContextChecker implements ApplicationListener<ApplicationContextEvent> {
    private static final Logger logger = Logger.getLogger(ContextChecker.class);

    @Autowired
    ConceptRepository conceptRepository;

    @Inject
    IdentityRepository identityRepository;

    @Autowired
    Neo4jTemplate neo4jTemplate;

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        logger.info("Using neo4j store: " + ((EmbeddedGraphDatabase) neo4jTemplate.getGraphDatabaseService()).getStoreDir());
        if (event instanceof ContextRefreshedEvent) {
            if (identityRepository.count() == 0) {
                identityRepository.save(
                        new Identity(
                                "1010101010",
                                "John doe",
                                new HashSet<CaveRegistration>(asList(
                                        new CaveRegistration(
                                                conceptRepository.getByConceptId("293610009"),
                                                "Blue hair",
                                                "A lot",
                                                "Monthly",
                                                "Head",
                                                true
                                        ),
                                        new CaveRegistration(
                                                conceptRepository.getByConceptId("295067003"),
                                                "Big elbows",
                                                "A lot",
                                                "weekly",
                                                "Arm",
                                                false
                                        )
                                ))
                        )
                );
            }
        }
    }

    @Transactional
    public Concept save(Concept entity) {
        return conceptRepository.save(entity);
    }
}
