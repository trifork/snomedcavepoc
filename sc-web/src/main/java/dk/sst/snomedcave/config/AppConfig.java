package dk.sst.snomedcave.config;

import dk.sst.snomedcave.service.ContextChecker;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;

@Configuration
@ImportResource("classpath:/Neo4jConfig.xml")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@ComponentScan(basePackages = {"dk.sst.snomedcave.dao", "dk.sst.snomedcave.service"})
@PropertySource("classpath:/app.properties")
public class AppConfig {
    private static Logger logger = Logger.getLogger(AppConfig.class);
    @Inject
    Environment env;

    @Bean
    public ContextChecker contextChecker() {
        logger.debug("env.getProperty(\"neo4j.data.path\")=" + env.getProperty("neo4j.data.path"));
        return new ContextChecker();
    }
}
