package dk.sst.snomedcave.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.sst.snomedcave.dao.IdentityRepository;
import dk.sst.snomedcave.model.Identity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

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
        return new ResponseEntity<String>(gson.toJson(identity), HttpStatus.OK);
    }
}
