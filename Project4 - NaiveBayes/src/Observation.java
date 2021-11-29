import java.util.List;

public class Observation {
    private String classification;
    private List<String> attributes;
    
    public Observation(String classification, String ... attributes) {
        this.classification = classification;
        this.attributes = List.of(attributes);
    }
    
    public String getClassification() {
        return classification;
    }
    
    public List<String> getAttributes() {
        return attributes;
    }
}
