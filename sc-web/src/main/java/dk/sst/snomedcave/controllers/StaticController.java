package dk.sst.snomedcave.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.lang3.StringUtils.join;

@Controller
public class StaticController {
    private static Logger logger = Logger.getLogger(StaticController.class);
    private File webapp = new File("/Users/mwl/IdeaProjects/SnomedCave/snomedcavepoc/sc-web/src/main/webapp/");

    @RequestMapping(value = {"/index"}, produces = "text/html")
    public ResponseEntity<String> index() throws IOException {
        return serve("index.html");
    }

    @RequestMapping(value="/css/{file}", produces = "text/css")
    public ResponseEntity<String> css(@PathVariable String file) {
        return serve("css", file + ".css");
    }

    @RequestMapping(value="/js/{file}.js", produces = "text/javascript")
    public ResponseEntity<String> js(@PathVariable String file) {
        return serve("js", file + ".js");
    }

    private ResponseEntity<String> serve(String... names) {
        final File file = new File(webapp, join(names, "/"));
        try {
            return new ResponseEntity<String>(readFileToString(file), HttpStatus.OK);
        } catch (IOException e) {
            logger.warn("Problem accessing file=" + file.getAbsolutePath(), e);
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }
}
