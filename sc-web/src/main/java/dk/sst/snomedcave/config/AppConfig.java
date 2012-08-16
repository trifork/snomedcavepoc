package dk.sst.snomedcave.config;

import dk.sst.snomedcave.service.SnomedParser;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ImportResource("classpath:/Neo4jConfig.xml")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@ComponentScan(basePackages = {"dk.sst.snomedcave.dao", "dk.sst.snomedcave.service"})
@EnableScheduling
public class AppConfig {
}
