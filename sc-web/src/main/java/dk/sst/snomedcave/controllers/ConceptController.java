package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.apache.commons.lang3.StringUtils.join;

@Controller
@RequestMapping("/concepts/")
public class ConceptController {
    @Autowired
    ConceptRepository conceptRepository;

    @RequestMapping("search")
    public ResponseEntity<String> search(@RequestParam("query") String query) {
        Concept concept = conceptRepository.getByFullyspecifiedName(query);
        if (concept == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(toJson(concept), getHeaders(), HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private String toJson(Concept concept) {
        return String.format(
                "{\"fullyspecifiedName\": \"%s\", \"childs\": [%s]}",
                concept.getFullyspecifiedName(),
                join(CollectionUtils.collect(concept.getSubConcepts(), new Transformer<Concept, Object>() {
                    @Override
                    public Object transform(Concept concept) {
                        return toJson(conceptRepository.findOne(concept.getNodeId()));
                    }
                }), ","));
    }
}
