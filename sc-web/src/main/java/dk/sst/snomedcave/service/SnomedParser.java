package dk.sst.snomedcave.service;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public class SnomedParser {
    private static Logger logger = Logger.getLogger(SnomedParser.class);
    private long lastModifiedConcept = 0L;

    @Scheduled(fixedDelay = 1000L) //One second between runs
    public void importJob() {
        File concepts = new File(".");
        if (concepts.lastModified() > lastModifiedConcept) {
            logger.info("SNOMED concepts filed was changed. Initiating import");
            lastModifiedConcept = concepts.lastModified();

        }
    }
}
