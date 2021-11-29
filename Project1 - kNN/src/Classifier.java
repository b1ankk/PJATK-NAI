import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Classifier {
    private final int k;
    private final List<Observation> trainData;
    
    public Classifier(int k, List<Observation> trainData) {
        this.k = k;
        this.trainData = trainData;
    }
    
    public String classify(Observation observation)
    {
        List<ObservationWithDistance> distances = calculateDistances(observation);
        
        Map<String, Long> classificationsCounted =
            groupKNearestClassificationsByCount(distances);
        
        return getBestClassification(classificationsCounted);
    }
    
    
    private List<ObservationWithDistance> calculateDistances(Observation testedObservation) {
        return trainData.stream()
                        .map(neighborObservation -> new ObservationWithDistance(
                            neighborObservation,
                            neighborObservation.getVector().distance(testedObservation.getVector())
                        ))
                        .collect(Collectors.toList());
    }
    
    private Map<String, Long> groupKNearestClassificationsByCount(
        List<ObservationWithDistance> distances)
    {
        return distances.stream()
                        .sorted(Comparator.comparingDouble(ObservationWithDistance::getDistance))
                        .limit(k)
                        .collect(Collectors.groupingBy(
                            observationWithDistance -> observationWithDistance.getObservation().getClassification(),
                            Collectors.counting()
                        ));
    }
    
    private static String getBestClassification(Map<String, Long> classificationsCounted) {
        return classificationsCounted
            .entrySet()
            .stream()
            .max(Comparator.comparingLong(Map.Entry::getValue))
            .orElseThrow()
            .getKey();
    }
    
    
}
