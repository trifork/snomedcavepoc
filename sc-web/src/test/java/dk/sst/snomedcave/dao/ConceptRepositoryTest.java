package dk.sst.snomedcave.dao;


import dk.sst.snomedcave.config.AppConfig;
import dk.sst.snomedcave.model.Concept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Transactional
public class ConceptRepositoryTest {
    @Autowired
    ConceptRepository conceptRepository;

    @Test
    public void springIsConfigured() throws Exception {
        assertNotNull(conceptRepository);
    }

    @Test
    public void canStoreConceptAndGetConcept() throws Exception {
        final String name = "TEST1 " + currentTimeMillis();
        final Concept concept = conceptRepository.save(new Concept(name));
        assertNotNull(concept);
        assertEquals(name, concept.getName());
    }

    @Test
    public void canFindConceptByName() throws Exception {
        String name = "TEST2 " + currentTimeMillis();
        conceptRepository.save(new Concept(name));
        Concept concept = conceptRepository.getByName(name);
        assertNotNull(concept);
        assertEquals(name, concept.getName());
    }

    @Test
    public void canStoreParentAndGetChilds() throws Exception {
        final String childName = "Child " + currentTimeMillis();
        final String parentName = "Parent " + currentTimeMillis();

        Concept child = new Concept(childName);
        Concept parent = new Concept(parentName, child);

        conceptRepository.save(parent);

        Concept foundChild = conceptRepository.getByName(childName);
        assertNotNull(foundChild);
        assertEquals(childName, foundChild.getName());
    }
}
