package dk.sst.snomedcave.dbgenerate;

import org.apache.log4j.Logger;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.neo4j.unsafe.batchinsert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.currentTimeMillis;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class SnomedParser {
    private static Logger logger = Logger.getLogger(SnomedParser.class);
    StreamFactory factory = StreamFactory.newInstance();

    Map<String, Long> conceptIds = new HashMap<String, Long>();

    Map<String, String> configConcept = new HashMap<String, String>() {{
        put("type", "exact");
    }};
    Map<String, String> configFull = new HashMap<String, String>() {{
        put("type", "fulltext");
        put("to_lower_case", "true");
    }};

    BatchInserter inserter = BatchInserters.inserter("target/data-insert.db");
    BatchInserterIndexProvider indexProvider = new LuceneBatchInserterIndexProvider(inserter);

    BatchInserterIndex nodeTypeIndex = indexProvider.nodeIndex("__types__", configConcept);
    //BatchInserterIndex relTypeIndex = indexProvider.nodeIndex("__rel-types__", configConcept);

    BatchInserterIndex conceptIndex = indexProvider.nodeIndex("Concept", configConcept);
    //BatchInserterIndex conceptFullIndex = indexProvider.nodeIndex("conceptFull", configFull);

    //BatchInserterIndex relationshipIndex = indexProvider.nodeIndex("ConceptRelation", configConcept);

    public void finish() {
        nodeTypeIndex.flush();
        //relTypeIndex.flush();
        conceptIndex.flush();
        //conceptFullIndex.flush();
        //relationshipIndex.flush();

        indexProvider.shutdown();
        inserter.shutdown();
    }


    private BeanReader getBeanReader(String file, String config) {
        File conceptsFile = new File("/Users/mwl/IdeaProjects/SnomedCave/data" + file);
        factory.load("/Users/mwl/IdeaProjects/SnomedCave/snomedcavepoc/sc-dbgenerate/src/main/resources/beanio-" + config + ".xml");
        return factory.createReader(config, conceptsFile);
    }

    public void importConcept() {
        long startTime = currentTimeMillis();
        BeanReader in = getBeanReader("/0/sct_concepts_20120813T134009.txt", "concepts");

        Object record;
        int count = 0;
        while ((record = in.read()) != null) {
            if ("header".equals(in.getRecordName())) {
                //Map<String, Object> header = (Map<String, Object>) record;
                logger.debug("Parsing header: ignoring");
            }
            else if ("concept".equals(in.getRecordName())) {
                count++;
                final Map<String, Object> concept = (Map<String, Object>) record;

                saveConcept(concept);

                if (count % 1000 == 0) {
                    logger.debug(String.format("time=%d, count=%d", currentTimeMillis() - startTime, count));
                }
            }
            else {
                logger.warn("unable to parse \"" + record + "\"");
            }
        }
        in.close();
        logger.info("Parsed " + count + " records in " + (currentTimeMillis() - startTime) + " ms");
    }

    private long saveConcept(final Map<String, Object> concept) {
        concept.put("__type__", "dk.sst.snomedcave.model.Concept");
        final long nodeId = inserter.createNode(concept);
        final String conceptId = (String) concept.get("conceptId");
        conceptIds.put(conceptId, nodeId);
        conceptIndex.add(nodeId, new HashMap<String, Object>() {{
            put("__type__", "dk.sst.snomedcave.model.Concept");
            put("conceptId", conceptId);
            put("fullyspecifiedName", concept.get("fullyspecifiedName"));
        }});
        //conceptFullIndex.add(nodeId, new HashMap<String, Object>() {{
        //}});

        nodeTypeIndex.add(nodeId, new HashMap<String, Object>() {{
            put("className", "dk.sst.snomedcave.model.Concept");
        }});

        return nodeId;
    }

    public void importRelationships() {
        BeanReader in = getBeanReader("/0/sct_relationships_20120813T134009.txt", "relationships");

        Object record;
        long startTime = currentTimeMillis();
        int count = 0;
        while ((record = in.read()) != null) {
            if ("header".equals(in.getRecordName())) {
                //Map<String, Object> header = (Map<String, Object>) record;
                logger.debug("Parsing header: ignoring");
            } else if ("relationship".equals(in.getRecordName())) {
                count++;

                final Map<String, Object> relation = (Map<String, Object>) record;

                relation.put("__type__", "dk.sst.snomedcave.model.ConceptRelation");
                final long concept1NodeId = getNodeId(relation.get("conceptId1"));
                final long concept2NodeId = getNodeId(relation.get("conceptId2"));
                final long conceptTypeNodeId = getNodeId(relation.get("relationshipType"));
                relation.remove("conceptId1");
                relation.remove("conceptId2");
                relation.remove("relationshipType");

                final long nodeId = inserter.createNode(relation);

                inserter.createRelationship(concept2NodeId, nodeId, withName("childs"), null);
                inserter.createRelationship(nodeId, concept1NodeId, withName("child"), null);
                inserter.createRelationship(nodeId, conceptTypeNodeId, withName("type"), null);

                if (count % 1000 == 0) {
                    logger.debug(String.format("time=%d, count=%d", currentTimeMillis() - startTime, count));
                }
            }
            else {
                logger.warn("unable to parse \"" + record + "\"");
            }
        }
        in.close();
        logger.info("Parsed " + count + " records in " + (currentTimeMillis() - startTime) + " ms");

    }

    private long getNodeId(final Object conceptId) {
        Long nodeId = conceptIds.get(conceptId);
        if (nodeId == null) {
            logger.warn("Could not find nodeId for conceptId=" + conceptId);
            nodeId = saveConcept(new HashMap<String, Object>() {{
                put("conceptId", conceptId);
                put("status", 0);
                put("fullyspecifiedName", "Dummy concept, " + conceptId);
                put("ctv3Id", "");
                put("snomedId", "dummy");
                put("primitive", false);
            }});
        }
        return nodeId;
    }
}
