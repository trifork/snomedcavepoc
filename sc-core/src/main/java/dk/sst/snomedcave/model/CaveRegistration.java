package dk.sst.snomedcave.model;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class CaveRegistration extends NodeObject {
    private Concept allergy;

    private String reaction;

    private String grade;

    private String reactionFrequency;

    private String area;

    private boolean warning;

    public CaveRegistration() {
    }

    public CaveRegistration(Concept allergy, String reaction, String grade, String reactionFrequency, String area, boolean warning) {
        this.allergy = allergy;
        this.reaction = reaction;
        this.grade = grade;
        this.reactionFrequency = reactionFrequency;
        this.area = area;
        this.warning = warning;
    }

    public Concept getAllergy() {
        return allergy;
    }

    public void setAllergy(Concept allergy) {
        this.allergy = allergy;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getReactionFrequency() {
        return reactionFrequency;
    }

    public void setReactionFrequency(String reactionFrequency) {
        this.reactionFrequency = reactionFrequency;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }
}
