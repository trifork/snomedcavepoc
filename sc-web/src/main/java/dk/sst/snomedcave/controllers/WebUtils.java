package dk.sst.snomedcave.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.sst.snomedcave.model.Concept;

public class WebUtils {
    private static Gson gson = new GsonBuilder().create();

    public String toJson(Concept concept) {
        return gson.toJson(concept);
    }

    public Concept toConcept(String json) {
        return gson.fromJson(json, Concept.class);
    }
}
