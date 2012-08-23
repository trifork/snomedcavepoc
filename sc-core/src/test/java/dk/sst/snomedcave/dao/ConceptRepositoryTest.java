package dk.sst.snomedcave.dao;


import dk.sst.snomedcave.model.Concept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:Neo4jConfig.xml")
@Transactional
public class ConceptRepositoryTest {
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
        final Concept concept = conceptRepository.save(new Concept("1", name));
        assertNotNull(concept);
        assertEquals(name, concept.getFullyspecifiedName());
    }

    @Test
    public void canFindConceptByName() throws Exception {
        String name = "TEST2 " + currentTimeMillis();
        conceptRepository.save(new Concept("1", name));
        Concept concept = conceptRepository.getByFullyspecifiedName(name);
        assertNotNull(concept);
        assertEquals(name, concept.getFullyspecifiedName());
    }

    @Test
    public void canStoreParentAndGetChilds() throws Exception {
        final String childName = "Child " + currentTimeMillis();
        final String parentName = "Parent " + currentTimeMillis();

        Concept child = new Concept("1", childName);
        Concept parent = new Concept("1", parentName, child);

        conceptRepository.save(parent);

        Concept foundChild = conceptRepository.getByFullyspecifiedName(childName);
        assertNotNull(foundChild);
        assertEquals(childName, foundChild.getFullyspecifiedName());
    }

    @Test
    public void canFindByConceptId() throws Exception {
        final String conceptId = "100";
        final String name = "Test" + currentTimeMillis();
        conceptRepository.save(new Concept(conceptId, name));

        Concept concept = conceptRepository.getByConceptId(conceptId);
        assertNotNull(concept);
        assertEquals(conceptId, concept.getConceptId());
        assertEquals(name, concept.getFullyspecifiedName());
    }

    @Test
    public void canFindConceptInAGoogleFashionedManner() throws Exception {
        conceptRepository.save(new Concept("1001", "Allergier over for lægemidler"));
        conceptRepository.save(new Concept("1002", "Allergier over for panodil"));

        List<Concept> concepts = conceptRepository.findByFullyspecifiedNameLike("læge");
        assertEquals(1, concepts.size());
        assertEquals("1001", concepts.get(0).getConceptId());
    }
}
