public class BinaryObservation extends Observation {
    private int value;
    
    public BinaryObservation(Vector vector, String classification, int value) {
        super(vector, classification);
        this.value = value;
    }
    
    public BinaryObservation(Observation observation, int value) {
        super(observation.getVector(), observation.getClassification());
        this.value = value;
    }
    
    public int getClassificationValue() {
        return value;
    }
}
