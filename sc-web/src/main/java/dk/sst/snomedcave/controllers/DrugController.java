package dk.sst.snomedcave.controllers;

import com.google.gson.*;
import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.dao.DrugRepository;
import dk.sst.snomedcave.model.Drug;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

@Controller
@RequestMapping("/drugs/")
public class DrugController {
    private final static Logger logger = Logger.getLogger(DrugController.class);
    @Inject
    DrugRepository drugRepository;

    @Inject
    ConceptRepository conceptRepository;

    Gson gson = new GsonBuilder().create();

    @RequestMapping(value = "search", produces = "application/json")
    public ResponseEntity<String> search(@RequestParam("q") String drugQuery) {
        List<Drug> drugs = drugRepository.findByDrugNameLike(String.format("*%s*", drugQuery));
        if (drugs.isEmpty()) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        JsonArray response = new JsonArray();
        for (Drug drug : drugs) {
            response.add(new JsonPrimitive(String.format("%s", drug.getDrugName())));
        }
        return new ResponseEntity<String>(gson.toJson(response), HttpStatus.OK);
    }

    @RequestMapping(value = "concepttree", produces = "application/json")
    public ResponseEntity<String> tree(@RequestParam("name") String drugName) {
        final List<Drug> drugs = drugRepository.findByDrugNameLike(drugName);
        if (drugs.size() > 1) {
            logger.warn("Found more than one drug for drugName=" + drugName);
        }
        return new ResponseEntity<String>(new HttpHeaders() {{
            put("location", singletonList("/concepts/tree?id=" + drugs.get(0).getRefersTo().getConceptId()));
        }}, HttpStatus.MOVED_TEMPORARILY);
    }
}
