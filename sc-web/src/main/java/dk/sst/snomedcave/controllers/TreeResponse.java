package dk.sst.snomedcave.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TreeResponse extends ResponseEntity<String> {
    ConceptNode root;
    static Gson GSON = new GsonBuilder().create();


    public TreeResponse(ConceptNode node, HttpStatus statusCode) {
        super(GSON.toJson(node), statusCode);
        root = node;
    }

    public ConceptNode getRoot() {
        return root;
    }
}
