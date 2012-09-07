package dk.sst.snomedcave.controllers;

import com.google.gson.*;
import dk.sst.snomedcave.dao.CaveRegistrationRepository;
import dk.sst.snomedcave.dao.IdentityRepository;
import dk.sst.snomedcave.model.CaveRegistration;
import dk.sst.snomedcave.model.Identity;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

import java.lang.reflect.Type;

import static org.apache.commons.collections15.CollectionUtils.collect;

@Controller
@RequestMapping("/identities/")
public class IdentityController {
    private static Logger logger = Logger.getLogger(IdentityController.class);
    @Inject
    IdentityRepository identityRepository;
    @Inject
    CaveRegistrationRepository caveRegistrationRepository;

    @Inject
    Gson gson;

    @RequestMapping(value = "{cpr}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<String> get(@PathVariable("cpr") String cpr) {
        Identity identity = identityRepository.getByCpr(cpr);
        if (identity == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(gson.toJson(identity), HttpStatus.OK);
    }

    @RequestMapping(value = "{cpr}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<String> save(@PathVariable("cpr") String cpr, @RequestBody String requestBody) {
        Identity identity = gson.fromJson(requestBody, Identity.class);

        identityRepository.save(identity);
        caveRegistrationRepository.save(identity.getCaveRegistrations());

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
