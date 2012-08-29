package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConceptControllerTest {
    @Mock
    ConceptRepository conceptRepository;
    @Mock
    WebUtils webUtils;
    Map<String, Concept> conceptMap = new HashMap<String, Concept>();

    @InjectMocks
    ConceptController controller = new ConceptController();
    Concept rootConcept = create("1",
            create("1-1",
                    create("1-1-1",
                            create("1-1-1-1",
                                    create("1-1-1-1-1",
                                            create("1-1-1-1-1-1"),
                                            create("1-1-1-1-1-2", create("1-1-1-1-1-2-1")),
                                            create("1-1-1-1-1-3")
                                    ),
                                    create("1-1-1-1-2")
                            ),
                            create("1-1-1-2", create("1-1-1-2-1"))
                    ),
                    create("1-1-2")
            ),
            create("1-2")
    );


    @Test
    public void responseOwnConcept() throws Exception {
        when(conceptRepository.getByConceptId("1")).thenReturn(conceptMap.get("1"));
        TreeResponse entity = controller.conceptTree("1");
        assertEquals(rootConcept.getConceptId(), entity.getRoot().conceptId);
    }

    @Test
    public void responseHasChilds() throws Exception {
        String id = "1-1-1-1-1";

        when(conceptRepository.getByConceptId(id)).thenReturn(conceptMap.get(id));

        TreeResponse entity = controller.conceptTree(id);

        assertEquals(id + "-1", entity.getRoot().childs.get(0).conceptId);
        assertEquals(id + "-2", entity.getRoot().childs.get(1).conceptId);
        assertTrue(entity.getRoot().childs.get(1).hasChilds);
        assertEquals(id + "-3", entity.getRoot().childs.get(2).conceptId);
        assertFalse(entity.getRoot().childs.get(2).hasChilds);
    }

    @Test @Ignore
    public void responseHasThreeLevelsOfParents() throws Exception {
        String id = "1-1-1-1-1";
        when(conceptRepository.getByConceptId(id)).thenReturn(conceptMap.get(id));
        when(conceptRepository.findOne(4l)).thenReturn(conceptMap.get("1-1-1-1"));
        when(conceptRepository.findOne(3l)).thenReturn(conceptMap.get("1-1-1"));
        when(conceptRepository.findOne(2l)).thenReturn(conceptMap.get("1-1"));

        TreeResponse entity = controller.conceptTree(id);

        assertEquals("1-1", entity.getRoot().conceptId);
        assertEquals("1-1-1", entity.getRoot().childs.get(0).conceptId);
        assertEquals("1-1-1-1", entity.getRoot().childs.get(0).childs.get(0).conceptId);
        assertEquals(id, entity.getRoot().childs.get(0).childs.get(0).childs.get(0).conceptId);
    }

    private Concept create(String conceptId, Concept... childs) {
        Concept concept = new Concept(conceptId, "Concept " + conceptId, CollectionUtils.collect(asList(childs), new Transformer<Concept, ConceptRelation>() {
            @Override
            public ConceptRelation transform(Concept concept) {
                return new ConceptRelation(null, concept);
            }
        }));
        conceptMap.put(conceptId, concept);
        return concept;
    }
}
