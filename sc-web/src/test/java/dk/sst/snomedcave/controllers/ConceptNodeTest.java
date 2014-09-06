package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
public class ConceptNodeTest {

    @Test
    public void canCreateConceptNodeFromConcept() throws Exception {
        Concept child = new Concept("ID2", "CHILD2");
        Concept concept = new Concept("ID1", "NAME1", new ConceptRelation(null, child));

        ConceptNode node = new ConceptNode(concept.getConceptId(), concept.getFullyspecifiedName());
        assertEquals("ID1", node.conceptId);
        assertEquals("NAME1", node.name);

        assertEquals("ID2", node.childs.get(0).conceptId);
        assertEquals("CHILD2", node.childs.get(0).name);
    }
}
