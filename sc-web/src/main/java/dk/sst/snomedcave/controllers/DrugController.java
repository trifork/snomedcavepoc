package dk.sst.snomedcave.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.dao.DrugRepository;
import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import dk.sst.snomedcave.model.Drug;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
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

    private static Concept causativeAgentAttribute;

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
        Concept refersTo = drugs.get(0).getRefersTo();
        Concept concept = null;
        for (ConceptRelation relation : refersTo.getChilds()) {
            Concept type = conceptRepository.findOne(relation.getType().getNodeId());
            if (type.getNodeId() == causativeAgentId()) {
                concept = conceptRepository.findOne(relation.getChild().getNodeId());
                break;
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.put("location", singletonList("/concepts/tree?id=" + concept.getConceptId()));
        return new ResponseEntity<String>(headers, HttpStatus.MOVED_TEMPORARILY);
    }

    private long causativeAgentId() {
        if (causativeAgentAttribute == null) {
            causativeAgentAttribute = conceptRepository.getByConceptId("246075003");
        }
        return causativeAgentAttribute.getNodeId();
    }
}
