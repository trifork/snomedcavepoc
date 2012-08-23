package dk.sst.snomedcave.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.data.neo4j.annotation.GraphId;

public class NodeObject {
    @GraphId
    private Long nodeId;

    //<editor-fold desc="GettersAndSetters">

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getNodeId() {
        return nodeId;
    }
    //</editor-fold>

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
}
