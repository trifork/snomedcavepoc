package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.*;

import static org.apache.commons.collections15.CollectionUtils.collect;

@Controller
@RequestMapping("/concepts/")
public class ConceptController {
    private static Logger logger = Logger.getLogger(ConceptController.class);
    @Inject
    ConceptRepository conceptRepository;

    @Inject
    WebUtils webUtils;

    @RequestMapping(value = "search", produces = "application/json")
    public ResponseEntity<String> search(@RequestParam("query") String query) {
        Concept concept = conceptRepository.getByFullyspecifiedName(query);

        logger.info("query=\"" + query + "\" concept=" + concept);
        if (concept == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(webUtils.toJson(concept), HttpStatus.OK);
    }

    @RequestMapping(value = "tree", produces = "application/json")
    public ResponseEntity<TreeResponse> conceptTree(@RequestParam("id") String conceptId) {
        TreeResponse response = new TreeResponse();
        Concept concept = conceptRepository.getByConceptId(conceptId);
        List<ConceptNode> childs = new ArrayList<ConceptNode>(collect(concept.getChilds(), new Transformer<ConceptRelation, ConceptNode>() {
            @Override
            public ConceptNode transform(ConceptRelation conceptRelation) {
                return new ConceptNode(conceptRelation.getChild());
            }
        }));
        Collections.sort(childs, new Comparator<ConceptNode>() {
            @Override
            public int compare(ConceptNode conceptNode, ConceptNode conceptNode1) {
                return conceptNode.conceptId.compareTo(conceptNode1.conceptId);
            }
        });
        response.root = new ConceptNode(
                concept.getConceptId(),
                concept.getFullyspecifiedName(),
                childs);
        return new ResponseEntity<TreeResponse>(response, HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
