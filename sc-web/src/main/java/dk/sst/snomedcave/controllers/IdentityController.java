package dk.sst.snomedcave.controllers;

import com.google.gson.Gson;
import dk.sst.snomedcave.dao.CaveRegistrationRepository;
import dk.sst.snomedcave.dao.IdentityRepository;
import dk.sst.snomedcave.model.Identity;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;

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

    @RequestMapping(value = "echo", produces = "application/json")
    @ResponseBody
    public String echo(@RequestParam(value = "e", required = false, defaultValue = "æøåÆØÅ") String e) throws UnsupportedEncodingException {
        return e;
    }

    @RequestMapping(value = "{cpr}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<byte[]> get(@PathVariable("cpr") String cpr) {
        Identity identity = identityRepository.getByCpr(cpr);
        if (identity == null) {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<byte[]>(gson.toJson(identity).getBytes(), HttpStatus.OK);
    }

    @RequestMapping(value = "{cpr}", method = RequestMethod.PUT)
    @Transactional
    public ResponseEntity<String> save(@PathVariable("cpr") String cpr, @RequestBody byte[] requestBody) {
        Identity identity = gson.fromJson(new String(requestBody), Identity.class);

        identityRepository.save(identity);
        caveRegistrationRepository.save(identity.getCaveRegistrations());

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}
