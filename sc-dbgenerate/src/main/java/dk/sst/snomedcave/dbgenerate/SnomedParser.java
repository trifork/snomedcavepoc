package dk.sst.snomedcave.dbgenerate;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static java.lang.System.currentTimeMillis;

public class SnomedParser {
    private static Logger logger = Logger.getLogger(SnomedParser.class);

    ConceptRepository conceptRepository;

    public SnomedParser(ConceptRepository conceptRepository) {
        this.conceptRepository = conceptRepository;
    }

    public void startImport() {
        parseConcepts();
    }

    private void parseConcepts() {
        long startTime = currentTimeMillis();
        File conceptsFile = new File("/Users/mwl/IdeaProjects/SnomedCave/data/0/sct_concepts_20120813T134009.txt");

        logger.info("SNOMED concepts filed was changed. Initiating import");

        StreamFactory factory = StreamFactory.newInstance();
        factory.load("/Users/mwl/IdeaProjects/SnomedCave/snomedcavepoc/sc-dbgenerate/src/main/resources/beanio-concepts.xml");
        BeanReader in = factory.createReader("concepts", conceptsFile);

        Object record;
        int count = 0;
        int addedCount = 0;
        int updatedCount = 0;
        while ((record = in.read()) != null) {
            if ("header".equals(in.getRecordName())) {
                //Map<String, Object> header = (Map<String, Object>) record;
                logger.debug("Parsing header: ignoring");
            } else if ("concept".equals(in.getRecordName())) {
                count++;
                Concept newConcept = (Concept) record;
                    /*
                    Concept existingConcept = conceptRepository.getByConceptId(newConcept.getConceptId());
                    if (existingConcept != null) {
                        logger.debug("Updating existing concept conceptId=" + existingConcept.getConceptId());
                        newConcept.setNodeId(existingConcept.getNodeId());
                        updatedCount++;
                    }

                    else {
                        addedCount++;
                    }
                    */
                if (count % 1000 == 0) {
                    logger.debug(String.format("time=%d, count=%d, added=%d, updated=%d", currentTimeMillis() - startTime, count, addedCount, updatedCount));
                }
                conceptRepository.save(newConcept);
            } else {
                logger.warn("unable to parse \"" + record + "\"");
            }
        }
        in.close();
        logger.info("Parsed " + count + " records in" + (currentTimeMillis() - startTime) + "ms from " + conceptsFile.getAbsolutePath());

    }
}
