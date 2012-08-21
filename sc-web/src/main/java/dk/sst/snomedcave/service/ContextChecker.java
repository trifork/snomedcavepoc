package dk.sst.snomedcave.service;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import org.apache.log4j.Logger;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.neo4j.support.Neo4jTemplate;

public class ContextChecker implements ApplicationListener<ApplicationContextEvent> {
    private static final Logger logger = Logger.getLogger(ContextChecker.class);

    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    Neo4jTemplate neo4jTemplate;

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        logger.info("Using neo4j store: " + ((EmbeddedGraphDatabase) neo4jTemplate.getGraphDatabaseService()).getStoreDir());
        if (event instanceof ContextRefreshedEvent) {
            if (conceptRepository.getByConceptId("1001") == null) {
                logger.info("Will bootstrap application with Concepts");
                Concept rootConcept = new Concept(
                        "1001",
                        "Allergi over for lægemidler",
                        new Concept[]{
                                new Concept("1002", "Allergier over for billige lægemidler", new Concept[] {
                                        new Concept("1003", "Allergier over for ampicillin"),
                                        new Concept("1004", "Allergier over for amoxecillin"),
                                }),
                                new Concept("1005", "Allergier over for hvide lægemidler", new Concept[] {
                                        new Concept("1006", "Allergier over for Panodil")
                                }),
                                new Concept("1004", "Allergier over for placebo")
                        }
                );

                conceptRepository.save(rootConcept);
            }
        }

    }
}
