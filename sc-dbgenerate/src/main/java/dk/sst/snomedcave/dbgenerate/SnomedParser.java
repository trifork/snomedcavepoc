package dk.sst.snomedcave.dbgenerate;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

@Transactional
public class SnomedParser {
    private static Logger logger = Logger.getLogger(SnomedParser.class);
    StreamFactory factory = StreamFactory.newInstance();

    ConceptRepository conceptRepository;

    public SnomedParser(ConceptRepository conceptRepository) {
        this.conceptRepository = conceptRepository;
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
        long startTime = currentTimeMillis();
        BeanReader in = getBeanReader("/0/sct_relationships_20120813T134009.txt", "relationships");

        Object record;
        int count = 0;
        while ((record = in.read()) != null) {
            if ("header".equals(in.getRecordName())) {
                //Map<String, Object> header = (Map<String, Object>) record;
                logger.debug("Parsing header: ignoring");
            } else if ("relationship".equals(in.getRecordName())) {
                count++;
                Map<String, Object> rMap = (Map<String, Object>) record;
                if (count % 1000 == 0) {
                    logger.debug(String.format("time=%d, count=%d", currentTimeMillis() - startTime, count));
                }
                String id1 = (String) rMap.get("ConceptId1");
                String id2 = (String) rMap.get("ConceptId2");
                Concept concept1 = conceptRepository.getByConceptId(id1);
                Concept concept2 = conceptRepository.getByConceptId(id2);
                if (concept1 == null) {
                    logger.error("Could not find concept1 with id=" + id1);
                }
                else if (concept2 == null) {
                    logger.error("Could not find concept2 with id=" + id2);
                }
                else {
                    concept1.add(concept2);
                    conceptRepository.save(concept1);
                }
            } else {
                logger.warn("unable to parse \"" + record + "\"");
            }
        }
        in.close();
        logger.info("Parsed " + count + " records in " + (currentTimeMillis() - startTime) + " ms");

    }
}
