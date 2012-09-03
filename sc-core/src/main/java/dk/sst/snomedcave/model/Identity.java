package dk.sst.snomedcave.model;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import java.util.Set;

@NodeEntity
public class Identity extends NodeObject {
    @Indexed
    private String cpr;

    private String name;

    @Fetch
    private Set<CaveRegistration> caveRegistrations;

    public Identity() {
    }

    public Identity(String cpr, String name, Set<CaveRegistration> caveRegistrations) {
        this.cpr = cpr;
        this.name = name;
        this.caveRegistrations = caveRegistrations;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CaveRegistration> getCaveRegistrations() {
        return caveRegistrations;
    }

    public void setCaveRegistrations(Set<CaveRegistration> caveRegistrations) {
        this.caveRegistrations = caveRegistrations;
    }
}
