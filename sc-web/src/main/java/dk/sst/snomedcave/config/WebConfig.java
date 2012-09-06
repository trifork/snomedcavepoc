package dk.sst.snomedcave.config;

import dk.sst.snomedcave.controllers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/*").addResourceLocations("/img/");
        registry.addResourceHandler("/*").addResourceLocations("/");
    }

    //@Bean
    public StaticController staticController() {
        return new StaticController();
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
