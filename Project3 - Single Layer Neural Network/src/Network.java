import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Network {
    private final double MAX_ERROR = 0.01;
    private final int MAX_ITERATIONS = 50_000;
    
    private final Map<String, Integer> observationIds;
    private final Perceptron[] perceptrons;
    
    
    public Network(int inputSize, int classificationCount, double alpha, Map<String, Integer> observationIds) {
        this.observationIds = observationIds;
        perceptrons = new Perceptron[classificationCount];
        for (int i = 0; i < classificationCount; ++i)
            perceptrons[i] = new Perceptron(alpha, alpha, inputSize);
    }
    
    
    public String classify(Observation observation) {
        double[] result = new double[perceptrons.length];
        for (int i = 0; i < perceptrons.length; i++)
            result[i] = perceptrons[i].classify(observation.getVector());
        
        int maxId = maxId(result);
        
        String classification =
            observationIds.entrySet()
                          .stream()
                          .filter(e -> e.getValue() == maxId)
                          .findFirst()
                          .get()
                          .getKey();
        
        return classification + " " + Arrays.stream(result)
                                            .boxed()
                                            .map(str -> String.format("%.2f", str))
                                            .collect(Collectors.joining(", ", "[", "]"));
    }
    
    private int maxId(double[] arr) {
        int maxId = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[maxId] < arr[i])
                maxId = i;
        }
        return maxId;
    }
    
    
    public void train(List<Observation> observations) {
        double error = 1;
        
        int i = 0;
        for (; i < MAX_ITERATIONS && error > MAX_ERROR; i++)
            error = performIteration(observations);
    
        System.out.println(error);
        System.out.println(i);
    }
    
    private double performIteration(List<Observation> observations) {
        double sum = 0;
        
        for (Observation observation : observations)
            sum += trainObservation(observation);
        
        double error = sum / observations.size();
        return error;
    }
    
    private double trainObservation(Observation observation) {
        double sum = 0;
        
        for (int i = 0; i < perceptrons.length; ++i) {
            BinaryObservation bObservation = new BinaryObservation(
                observation,
                observationIds.get(observation.getClassification()) == i ? 1 : 0
            );
            sum += perceptrons[i].trainObservation(bObservation);
        }
        
        double error = sum / perceptrons.length;
        return error;
    }
    
}
