package dk.sst.snomedcave.controllers.serializers;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public interface WebSerializer<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    Class<T> getType();
}
