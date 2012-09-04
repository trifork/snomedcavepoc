package dk.sst.snomedcave.dao;

import dk.sst.snomedcave.model.Drug;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:Neo4jConfig.xml")
@Transactional
public class DrugRepositoryTest {

    @SuppressWarnings("SpringJavaAutowiringInspection Provided by <neo4j:config .../> auto discovery")
    @Inject
    DrugRepository drugRepository;

    @Test
    public void canFindFlixonase() throws Exception {
        Drug drug = drugRepository.getByDrugId("28101368089");
        System.out.println("drug.getNodeId() = " + drug.getNodeId());
        assertNotNull(drug);
        assertEquals("Flixonase (FLUTICASONPROPIONAT)", drug.getName());
        assertNotNull(drug.getRefersTo());
    }

    @Test
    public void canFindDrugFromNamePart() throws Exception {
        List<Drug> drugs = drugRepository.findByNameLike("*xona*");
        assertFalse(drugs.isEmpty());
        assertThat(drugs, hasItem(equalTo(drugRepository.getByDrugId("28101368089"))));
    }
}
