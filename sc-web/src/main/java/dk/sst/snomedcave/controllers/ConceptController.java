package dk.sst.snomedcave.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.sst.snomedcave.dao.ConceptRelationRepository;
import dk.sst.snomedcave.dao.ConceptRepository;
import dk.sst.snomedcave.model.Concept;
import dk.sst.snomedcave.model.ConceptRelation;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.reverse;
import static java.util.Collections.sort;
import static org.apache.commons.collections15.CollectionUtils.*;
import static org.neo4j.graphdb.DynamicRelationshipType.withName;
import static org.neo4j.graphdb.traversal.Evaluators.returnWhereEndNodeIs;

@Controller
@RequestMapping("/concepts/")
public class ConceptController {
    private static Logger logger = Logger.getLogger(ConceptController.class);
    private static final Comparator<ConceptNode> CONCEPTNAME_COMPARATOR = new Comparator<ConceptNode>() {
        @Override
        public int compare(ConceptNode o1, ConceptNode o2) {
            return o1.name.compareTo(o2.name);
        }
    };
    @Inject
    ConceptRepository conceptRepository;

    @Inject
    ConceptRelationRepository conceptRelationRepository;

    @Inject
    Neo4jTemplate neo4jTemplate;

    @Inject
    WebUtils webUtils;

    private Concept isAType;

    private Gson gson = new GsonBuilder().create();

    final TraversalDescription td = Traversal.description()
            .breadthFirst()
            .relationships(withName("childs"), Direction.OUTGOING)
            .relationships(withName("child"), Direction.OUTGOING);

    private Concept getIsA() {
        if (isAType == null) {
            isAType = conceptRepository.getByConceptId("116680003");
        }
        return isAType;
    }

    private ConceptNode toConceptNode(Concept concept) {
        get(concept);
        boolean hasChilds = exists(concept.getChilds(), new Predicate<ConceptRelation>() {
            @Override
            public boolean evaluate(ConceptRelation relation) {
                return shouldInclude(relation);
            }
        });
        return new ConceptNode(
                concept.getConceptId(),
                concept.getTerm(),
                hasChilds);
    }

    private ConceptNode toConceptNodeWithChilds(Concept concept) {
        return toConceptNodeWithChilds(concept, new ConceptNode("", null, false));
    }

    private ConceptNode toConceptNodeWithChilds(Concept concept, final ConceptNode included) {
        get(concept);
        final Collection<ConceptRelation> childs = CollectionUtils.select(concept.getChilds(), new Predicate<ConceptRelation>() {
            @Override
            public boolean evaluate(ConceptRelation relation) {
                return shouldInclude(relation);
            }
        });
        return toConceptNodeWithChilds(
                concept,
                collect(childs, new Transformer<ConceptRelation, ConceptNode>() {
                    @Override
                    public ConceptNode transform(ConceptRelation relation) {
                        final Concept child = get(relation).getChild();
                        return get(child).getConceptId().equals(included.conceptId) ? included : toConceptNode(child);
                    }
                })
        );
    }

    private ConceptNode toConceptNodeWithChilds(Concept concept, Collection<ConceptNode> childs) {
        get(concept);
        return new ConceptNode(
                concept.getConceptId(),
                concept.getTerm(),
                childs
        );

    }

    @RequestMapping(value = "search", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> search(@RequestParam("query") String query) {
        Concept concept = conceptRepository.getByFullyspecifiedName(query);

        logger.info("query=\"" + query + "\" concept=" + concept);
        if (concept == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(webUtils.toJson(concept), HttpStatus.OK);
    }

    @RequestMapping(value = "node", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> nodeJson(@RequestParam("id") String conceptId) {
        return new ResponseEntity<String>(gson.toJson(
                toConceptNodeWithChilds(conceptRepository.getByConceptId(conceptId))),
                HttpStatus.OK);
    }

    @RequestMapping(value = "tree", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> treeJson(@RequestParam("id") String conceptId) {
        return new ResponseEntity<String>(gson.toJson(conceptTree(conceptId).getRoot()), HttpStatus.OK);
    }

    public TreeResponse conceptTree(final String conceptId) {
        Concept target = conceptRepository.getByConceptId(conceptId);
        if (target == null) {
            logger.debug("Did not find any concepts with conceptId=" + conceptId);
            return new TreeResponse(null);
        }
        List<Concept> levels = getThreeLevelsUpFrom(target);
        reverse(levels);
        ConceptNode root = toConceptNodeWithChilds(target);

        for (int i = 1; i <= 3; i++) {
            if (i >= levels.size()) {
                break;
            }
            Concept concept = levels.get(i);
            if (i == 1) {
                root = toConceptNodeWithChilds(concept, root);
            }
            else {
                root = toConceptNodeWithChilds(concept, asList(root));
            }
        }

        return new TreeResponse(root);
    }

    private boolean shouldInclude(ConceptRelation relation) {
        final Concept concept = get(get(relation).getChild());
        final Concept type = get(relation.getType());
        return concept.getStatus() == 0 && type.getNodeId().equals(getIsA().getNodeId());
    }

    private List<Concept> getThreeLevelsUpFrom(Concept target) {
        Node targetNode = neo4jTemplate.getNode(target.getNodeId());
        Concept drugAllergy = conceptRepository.getByConceptId("416098002");
        Node drugAllergyNode = neo4jTemplate.getNode(drugAllergy.getNodeId());

        final Traverser paths = td.evaluator(returnWhereEndNodeIs(targetNode)).traverse(drugAllergyNode);

        final Iterator<Path> iterator = paths.iterator();
        List<Concept> result = new ArrayList<Concept>();
        if (iterator.hasNext()) {
            final Path path = iterator.next();
            for (PropertyContainer container : path) {
                if (container instanceof Node) {
                    Node node = (Node) container;
                    if (node.getProperty("__type__").equals(Concept.class.getCanonicalName())) {
                        result.add(conceptRepository.findOne(node.getId()));
                    }
                }
            }
        }
        return result;
    }

    private ConceptRelation get(ConceptRelation relation) {
        if (relation.getRelationId() == null) {
            ConceptRelation dbRelation = conceptRelationRepository.findOne(relation.getNodeId());
            relation.setRelationId(dbRelation.getRelationId());
            relation.setChild(dbRelation.getChild());
            relation.setType(dbRelation.getType());
        }
        return relation;
    }

    private Concept get(Concept concept) {
        if (concept.getConceptId() == null) {
            Concept dbConcept = conceptRepository.findOne(concept.getNodeId());
            concept.setConceptId(dbConcept.getConceptId());
            concept.setSnomedId(dbConcept.getSnomedId());
            concept.setCtv3Id(dbConcept.getCtv3Id());
            concept.setFullyspecifiedName(dbConcept.getFullyspecifiedName());
            concept.setPrimitive(dbConcept.isPrimitive());
            concept.setStatus(dbConcept.getStatus());
            concept.setTerm(dbConcept.getTerm());
            concept.setChilds(dbConcept.getChilds());
        }
        return concept;
    }
}
