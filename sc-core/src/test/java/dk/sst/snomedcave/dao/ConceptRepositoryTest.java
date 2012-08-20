package dk.sst.snomedcave.dao;


import dk.sst.snomedcave.model.Concept;
import org.apache.log4j.Logger;
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
@ContextConfiguration(locations = "classpath:Neo4jConfig.xml")
@Transactional
public class ConceptRepositoryTest {
    private static Logger logger = Logger.getLogger(ConceptRepositoryTest.class);
    @SuppressWarnings("SpringJavaAutowiringInspection Provided by <neo4j:config .../>")
    @Autowired
    ConceptRepository conceptRepository;

    @Test
    public void springIsConfigured() throws Exception {
        assertNotNull(conceptRepository);
    }

    @Test
    public void canStoreConceptAndGetConcept() throws Exception {
        final String name = "TEST1 " + currentTimeMillis();
        final Concept concept = conceptRepository.save(new Concept(1L, name));
        assertNotNull(concept);
        assertEquals(name, concept.getFullyspecifiedName());
    }

    @Test
    public void canFindConceptByName() throws Exception {
        String name = "TEST2 " + currentTimeMillis();
        conceptRepository.save(new Concept(1L, name));
        Concept concept = conceptRepository.getByFullyspecifiedName(name);
        assertNotNull(concept);
        assertEquals(name, concept.getFullyspecifiedName());
    }

    @Test
    public void canStoreParentAndGetChilds() throws Exception {
        final String childName = "Child " + currentTimeMillis();
        final String parentName = "Parent " + currentTimeMillis();

        Concept child = new Concept(1L, childName);
        Concept parent = new Concept(1L, parentName, child);

        conceptRepository.save(parent);

        Concept foundChild = conceptRepository.getByFullyspecifiedName(childName);
        assertNotNull(foundChild);
        assertEquals(childName, foundChild.getFullyspecifiedName());
    }

    @Test
    public void canFindByConceptId() throws Exception {
        final long conceptId = 100l;
        final String name = "Test" + currentTimeMillis();
        conceptRepository.save(new Concept(conceptId, name));

        Concept concept = conceptRepository.getByConceptId(conceptId);
        assertNotNull(concept);
        assertEquals(conceptId, concept.getConceptId());
        assertEquals(name, concept.getFullyspecifiedName());
    }

    @Test
    public void canFindSomeConcept() throws Exception {
        logger.debug("TEST TEST TEST");
        final Concept foundConcept = conceptRepository.getByConceptId(100005l);
        //final Concept foundConcept = conceptRepository.getByFullyspecifiedName("SNOMED RT Concept (special concept)");
        assertNotNull(foundConcept);
        assertEquals(100005l, foundConcept.getConceptId());
        assertEquals("SNOMED RT Concept (special concept)", foundConcept.getFullyspecifiedName());
    }
}
