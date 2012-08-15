package dk.sst.snomedcave.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/concepts/")
public class ConceptController {

    @RequestMapping("test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>(
                "{\"name\":\"TEST: " + System.currentTimeMillis() + "\", \"childs\":[" +
                    "{\"name\":\"Concept A\", \"childs\":[" +
                        "{\"name\":\"Concept A c\"}" +
                    "]}," +
                    "{\"name\":\"Concept B\", \"childs\":[]}\n" +
                "]}",
                getHeaders(),
                HttpStatus.OK);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
}
