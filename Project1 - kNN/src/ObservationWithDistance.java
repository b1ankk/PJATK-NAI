public class ObservationWithDistance {
    private Observation observation;
    private double distance;
    
    public ObservationWithDistance(Observation observation, double distance) {
        this.observation = observation;
        this.distance = distance;
    }
    
    public Observation getObservation() {
        return observation;
    }
    
    public double getDistance() {
        return distance;
    }
}
