package dk.sst.snomedcave.dbgenerate;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

@Transactional
public class SnomedParser {
    private static Logger logger = Logger.getLogger(SnomedParser.class);
    StreamFactory factory = StreamFactory.newInstance();

    ConceptRepository conceptRepository;
    Neo4jTemplate neo4jTemplate;

    Map<String, Long> idMap = new HashMap<String, Long>();

    long lookupCount = 0L;
    long lookupTime = 0L;
    long saveCount = 0L;
    long saveTime = 0L;

    public SnomedParser(ConceptRepository conceptRepository, Neo4jTemplate neo4jTemplate) {
        this.conceptRepository = conceptRepository;
        this.neo4jTemplate = neo4jTemplate;
    }

    private BeanReader getBeanReader(String file, String config) {
        File conceptsFile = new File("/Users/mwl/IdeaProjects/SnomedCave/data" + file);
        factory.load("/Users/mwl/IdeaProjects/SnomedCave/snomedcavepoc/sc-dbgenerate/src/main/resources/beanio-" + config + ".xml");
        return factory.createReader(config, conceptsFile);
    }

    public void importConcept() {
        long startTime = currentTimeMillis();
        BeanReader in = getBeanReader("/0/sct_concepts_20120813T134009.txt", "concepts");

        Object record;
        int count = 0;
        while ((record = in.read()) != null) {
            if ("header".equals(in.getRecordName())) {
                //Map<String, Object> header = (Map<String, Object>) record;
                logger.debug("Parsing header: ignoring");
            } else if ("concept".equals(in.getRecordName())) {
                count++;
                Concept newConcept = (Concept) record;
                if (count % 1000 == 0) {
                    logger.debug(String.format("time=%d, count=%d", currentTimeMillis() - startTime, count));
                }
                conceptRepository.save(newConcept);
            } else {
                logger.warn("unable to parse \"" + record + "\"");
            }
        }
        in.close();
        logger.info("Parsed " + count + " records in " + (currentTimeMillis() - startTime) + " ms");
    }

    public void importRelationships() {
        BeanReader in = getBeanReader("/0/sct_relationships_20120813T134009.txt", "relationships");

        long idCount = 0;
        for (Concept concept : conceptRepository.findAll()) {
            idMap.put(concept.getConceptId(), concept.getNodeId());
            idCount++;
        }
        logger.info("Added " + idCount + " ids");


        Object record;
        long startTime = currentTimeMillis();
        int count = 0;
        while ((record = in.read()) != null) {
            if ("header".equals(in.getRecordName())) {
                //Map<String, Object> header = (Map<String, Object>) record;
                logger.debug("Parsing header: ignoring");
            } else if ("relationship".equals(in.getRecordName())) {
                count++;
                Map<String, Object> rMap = (Map<String, Object>) record;
                if (count % 1000 == 0) {
                    logger.debug(String.format("time=%d, count=%d, avgLookup=%d, avgSave=%d", currentTimeMillis() - startTime, count, lookupTime/lookupCount, saveTime/saveCount));
                }
                String id1 = (String) rMap.get("ConceptId1");
                String id2 = (String) rMap.get("ConceptId2");
                String idRelationshipType = (String) rMap.get("RelationshipType");
                Concept concept1 = getConcept(id1);
                Concept concept2 = getConcept(id2);
                Concept relationshipType = getConcept(idRelationshipType);
                if (concept1 == null) {
                    logger.error("Could not find concept1 with id=" + id1);
                }
                else if (concept2 == null) {
                    logger.error("Could not find concept2 with id=" + id2);
                }
                else if (relationshipType == null) {
                    logger.error("Could not find relationshipType with id=" + idRelationshipType);
                }
                else {
                    long saveStart = currentTimeMillis();
                    Transaction transaction = neo4jTemplate.beginTx();
                    try {
                        concept1.add(new ConceptRelation(relationshipType, concept2));
                        conceptRepository.save(concept1);
                        transaction.success();
                    } finally {
                        transaction.finish();
                    }
                    saveTime += currentTimeMillis() - saveStart;
                    saveCount++;
                }
            } else {
                logger.warn("unable to parse \"" + record + "\"");
            }
        }
        in.close();
        logger.info("Parsed " + count + " records in " + (currentTimeMillis() - startTime) + " ms");

    }

    private Concept getConcept(String id) {
        long start = currentTimeMillis();
        Long nodeId = idMap.get(id);
        Concept one = conceptRepository.findOne(nodeId);
        lookupTime += currentTimeMillis() - start;
        lookupCount++;
        return one;
    }
}
