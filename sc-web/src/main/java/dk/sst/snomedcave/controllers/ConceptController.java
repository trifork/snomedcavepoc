package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/concepts/")
public class ConceptController {
    private static Logger logger = Logger.getLogger(ConceptController.class);
    @Autowired
    ConceptRepository conceptRepository;

    @Autowired
    WebUtils webUtils;

    //TODO: @RequestMapping(value = "search", produces = "application/json") and remove getHeaders()
    @RequestMapping("search")
    public ResponseEntity<String> search(@RequestParam("query") String query) {
        Concept concept = conceptRepository.getByFullyspecifiedName(query);

        logger.info("query=\"" + query + "\" concept=" + concept);
        if (concept == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(webUtils.toJson(concept), getHeaders(), HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
