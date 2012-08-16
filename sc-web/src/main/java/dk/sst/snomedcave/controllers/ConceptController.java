package dk.sst.snomedcave.controllers;

import dk.sst.snomedcave.model.Concept;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/concepts/")
public class ConceptController {
    private Map<String, Concept> concepts = new HashMap<String, Concept>();

    public ConceptController() {
        int i = 1;
        concepts.put("B", new Concept(i++, "concept B"));
        concepts.put("C", new Concept(i++, "concept C"));
        concepts.put("A", new Concept(i++, "concept A", concepts.get("C")));
        concepts.put("root", new Concept(i++, "root", concepts.get("A"), concepts.get("B")));
    }

    @RequestMapping("search")
    public ResponseEntity<String> search(@RequestParam("query") String query) {
        return new ResponseEntity<String>(concepts.get(query).toJson(), getHeaders(), HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
