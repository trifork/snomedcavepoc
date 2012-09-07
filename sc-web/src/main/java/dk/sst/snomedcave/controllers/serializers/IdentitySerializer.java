package dk.sst.snomedcave.controllers.serializers;

import com.google.gson.*;
import dk.sst.snomedcave.model.CaveRegistration;
import dk.sst.snomedcave.model.Identity;
import org.apache.commons.collections15.Transformer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashSet;

import static org.apache.commons.collections15.CollectionUtils.collect;

@Component
public class IdentitySerializer implements WebSerializer<Identity> {
    @Override
    public Class<Identity> getType() {
        return Identity.class;
    }

    @Override
    public Identity deserialize(JsonElement element, Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject json = element.getAsJsonObject();
        Identity identity = new Identity();
        identity.setNodeId(json.get("nodeId").getAsLong());
        identity.setCpr(json.get("cpr").getAsString());
        identity.setName(json.get("name").getAsString());
        identity.setCaveRegistrations(new HashSet<CaveRegistration>(collect(json.get("caveRegistrations").getAsJsonArray().iterator(), new Transformer<JsonElement, CaveRegistration>() {
            @Override
            public CaveRegistration transform(JsonElement jsonElement) {
                return context.deserialize(jsonElement, CaveRegistration.class);
            }
        })));

        return identity;
    }

    @Override
    public JsonElement serialize(Identity identity, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject response = new JsonObject();
        response.addProperty("nodeId", identity.getNodeId());
        response.addProperty("cpr", identity.getCpr());
        response.addProperty("name", identity.getName());
        response.add("caveRegistrations", context.serialize(identity.getCaveRegistrations()));
        return response;
    }
}
