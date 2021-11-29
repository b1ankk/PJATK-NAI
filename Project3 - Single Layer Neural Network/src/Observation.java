public class Observation {
    private Vector vector;
    private String classification;
    
    public Observation(Vector vector, String classification) {
        this.vector = vector;
        this.classification = classification;
    }
    
    public Vector getVector() {
        return vector;
    }
    
    public String getClassification() {
        return classification;
    }
    
    @Override
    public String toString() {
        return classification + ": " + vector.toString();
    }
}
