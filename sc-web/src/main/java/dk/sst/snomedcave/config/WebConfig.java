package dk.sst.snomedcave.config;

import dk.sst.snomedcave.controllers.ConceptController;
import dk.sst.snomedcave.controllers.DrugController;
import dk.sst.snomedcave.controllers.WebUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*").addResourceLocations("/");
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
    public WebUtils webUtils() {
        return new WebUtils();
    }
}
