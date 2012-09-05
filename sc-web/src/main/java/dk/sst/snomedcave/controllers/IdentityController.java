package dk.sst.snomedcave.controllers;

import com.google.gson.*;
import dk.sst.snomedcave.dao.IdentityRepository;
import dk.sst.snomedcave.model.CaveRegistration;
import dk.sst.snomedcave.model.Identity;
import org.apache.commons.collections15.Transformer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

import static org.apache.commons.collections15.CollectionUtils.collect;

@Controller
@RequestMapping("/identities/")
public class IdentityController {
    @Inject
    IdentityRepository identityRepository;

    private final Gson gson = new GsonBuilder().create();

    @RequestMapping(value = "{cpr}", produces = "application/json")
    public ResponseEntity<String> get(@PathVariable("cpr") String cpr) {
        Identity identity = identityRepository.getByCpr(cpr);
        if (identity == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(gson.toJson(getResponse(identity)), HttpStatus.OK);
    }

    private JsonElement getResponse(final Identity identity) {
        final JsonObject response = new JsonObject();
        response.addProperty("nodeId", identity.getNodeId());
        response.addProperty("cpr", identity.getCpr());
        response.addProperty("name", identity.getName());
        final JsonArray registrations = new JsonArray();
        for (CaveRegistration registration : identity.getCaveRegistrations()) {
            final JsonObject element = new JsonObject();
            element.addProperty("allergyId", registration.getAllergy().getConceptId());
            element.addProperty("allergyTerm", registration.getAllergy().getTerm());
            element.addProperty("nodeId", registration.getNodeId());
            element.addProperty("area", registration.getArea());
            element.addProperty("grade", registration.getGrade());
            element.addProperty("reaction", registration.getReaction());
            element.addProperty("reactionFrequency", registration.getReactionFrequency());
            element.addProperty("warning", registration.isWarning());
            registrations.add(element);
        }
        response.add("caveRegistrations", registrations);
        return response;
    }
}
