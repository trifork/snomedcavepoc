package dk.sst.snomedcave.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DrugControllerTest.TestConfig.class)
@Transactional
public class DrugControllerTest {
    Gson gson = new GsonBuilder().create();

    @Configuration
    @ImportResource("classpath:/Neo4jConfig.xml")
    public static class TestConfig {
        @Bean
        public DrugController drugController() {
            return new DrugController();
        }
    }

    @Inject
    DrugController drugController;

    @Test
    public void willAlwaysReturnNonCombinedDrug() throws Exception {
        for (int i = 0; i < 100; i++) {
            String allergyId = drugController.tree("Paracetamol").getBody();
            assertNotNull(allergyId);
            assertEquals(i + " of 10", "293584003", allergyId);
        }
    }
}
