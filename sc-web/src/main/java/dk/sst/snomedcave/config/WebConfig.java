package dk.sst.snomedcave.config;

import com.google.gson.*;
import dk.sst.snomedcave.controllers.*;
import dk.sst.snomedcave.controllers.serializers.WebSerializer;
import dk.sst.snomedcave.model.Identity;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.reflect.Type;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "dk.sst.snomedcave.controllers.serializers")
public class WebConfig extends WebMvcConfigurerAdapter {
    private static Logger logger = Logger.getLogger(WebConfig.class);
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/");
    }

    @Bean
    public Gson gson(WebSerializer<?>[] serializers) {
        GsonBuilder builder = new GsonBuilder();
        for (WebSerializer<?> serializer : serializers) {
            builder.registerTypeAdapter(serializer.getType(), serializer);
        }
        return builder.create();
    }

    @Bean
    public JsonDeserializer<Identity> identityJsonDeserializer() {
        return new JsonDeserializer<Identity>() {
            @Override
            public Identity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return null;
            }
        };
    }

    @Bean
    public ConceptController snomedController() {
        return new ConceptController();
    }

    @Bean
    public DrugController drugController() {
        return new DrugController();
    }

    @Bean
    public IdentityController identityController() {
        return new IdentityController();
    }

    @Bean
    public WebUtils webUtils() {
        return new WebUtils();
    }
}
