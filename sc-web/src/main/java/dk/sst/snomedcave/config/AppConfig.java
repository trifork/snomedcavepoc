package dk.sst.snomedcave.config;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ImportResource("classpath:/Neo4jConfig.xml")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
@ComponentScan(basePackages = {"dk.sst.snomedcave.dao"})
public class AppConfig {

}
