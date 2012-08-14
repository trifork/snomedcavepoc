package dk.sst.snomedcave.dao;


import dk.sst.snomedcave.model.Concept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConceptRepositoryTest {
    @Autowired
    ConceptRepository conceptRepository;

    @Test
    public void canStoreConcept() throws Exception {
        conceptRepository.save(new Concept());
    }

    @Test
    public void canFindConceptByName() throws Exception {
        final Concept concept = new Concept();
        final String name = "test";
        concept.setName(name);

        conceptRepository.save(concept);

        final Concept foundConcept = conceptRepository.findByPropertyValue("name", name);

        assertEquals(concept.getName(), foundConcept.getName());
    }
}
