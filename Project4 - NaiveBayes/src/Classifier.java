import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Classifier {
    private final List<Observation> observations;
    private final List<String> classifications;
    
    public Classifier(List<Observation> observations) {
        this.observations = observations;
        
        this.classifications = observations.stream()
            .map(Observation::getClassification)
            .distinct()
            .collect(Collectors.toUnmodifiableList());
    }
    
    public String classify(Observation observation) {
        double[] chances = classifications
            .stream()
            .mapToDouble(
                c ->
                    getChance(new Observation(
                        c, observation.getAttributes().toArray(String[]::new))
                    ))
            .toArray();
        
        int maxIndex = 0;
        for (int i = 0; i < classifications.size(); i++)
            if (chances[i] > chances[maxIndex])
                maxIndex = i;
        
        String classification = classifications.get(maxIndex);
        return classification;
    }
    
    public double getChance(Observation observation) {
        List<Observation> matchingObservations =
            getObservationsMatchingClassification(observation.getClassification());
        double matchingCount = matchingObservations.size();
        
        List<String> attributes = observation.getAttributes();
        List<Double> atrChances = computeAttributeChances(matchingObservations, attributes);
    
        smoothIfNeeded(matchingCount, atrChances);
    
        double result = matchingCount / observations.size();
        for (double chance : atrChances)
            result *= chance;
        
        return result;
    }
    
    private ArrayList<Observation> getObservationsMatchingClassification(String classification) {
        return observations
            .parallelStream()
            .filter(obs -> obs.getClassification().equals(classification))
            .collect(Collectors.toCollection(ArrayList::new));
    }
    
    private List<Double> computeAttributeChances(List<Observation> wellClassified, List<String> attributes) {
        List<Double> atrChances = new ArrayList<>(attributes.size());
        double classCount = wellClassified.size();
        
        for (int i = 0; i < attributes.size(); i++) {
            String atr = attributes.get(i);
            int ii = i;
            double count = wellClassified.parallelStream()
                                         .filter(obs -> obs.getAttributes().get(ii).equals(atr))
                                         .count();
            atrChances.add(count / classCount);
        }
        return atrChances;
    }
    
    private void smoothIfNeeded(double classCount, List<Double> atrChances) {
        for (int i = 0; i < atrChances.size(); i++) {
            if (atrChances.get(i) == 0) {
                int ii = i;
                long valueCount = observations
                    .parallelStream()
                    .map(obs -> obs.getAttributes().get(ii))
                    .filter(atr -> !atr.equals("?"))
                    .distinct()
                    .count();
                
                double smoothed = 1. / (classCount + valueCount);
                atrChances.set(i, smoothed);
            }
        }
    }
    
    
}
