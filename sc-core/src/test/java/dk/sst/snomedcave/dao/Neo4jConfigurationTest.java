package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.Concept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:Neo4jConfig.xml")
public class Neo4jConfigurationTest {
    @Autowired
    Neo4jTemplate neo4jTemplate;

    @Test
    public void springIsConfigured() throws Exception {
        assertNotNull(neo4jTemplate);
    }

    @Test
    @Transactional
    public void canPutSomethingInAndGetItOut() throws Exception {
        final String name = "TestConcept " + currentTimeMillis();
        Concept concept = neo4jTemplate.save(new Concept("1", name));
        assertNotNull(concept);
        assertEquals(name, concept.getFullyspecifiedName());
    }
}
