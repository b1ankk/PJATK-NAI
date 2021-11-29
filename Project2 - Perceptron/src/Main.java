import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String trainSet = args[0];
        String testSet = args[1];
        double alpha = Double.parseDouble(args[2]);
        
        // load train data
        List<Observation> observations = DataLoader.loadDataFromFile(trainSet);
        Map<String, Integer> classToBinaryMap = getClassToBinaryMap(observations);
        List<BinaryObservation> binaryObservations = getBinaryObservations(observations, classToBinaryMap);
        
        // train
        int inputSize = observations.get(0).getVector().size();
        Perceptron perceptron = new Perceptron(alpha, alpha, inputSize);
        double trainError = perceptron.train(binaryObservations);
        System.out.println("Train error: " + trainError);
        
        // load test data
        List<Observation> testObs = DataLoader.loadDataFromFile(testSet);
        List<BinaryObservation> testBinaryObservations = getBinaryObservations(testObs, classToBinaryMap);
        
        // test
        int correctCases = 0;
        System.out.println(classToBinaryMap);
        for (BinaryObservation observation : testBinaryObservations) {
            int result = perceptron.classify(observation.getVector());
            System.out.print(result + "\t");
            System.out.println(observation);
            
            if (result == observation.getClassificationValue())
                correctCases++;
        }
        System.out.println("Test accuracy: " + (correctCases * 100. / testBinaryObservations.size()) + "%");
        
        classifyFromSystemIn(perceptron);
    }
    
    private static void classifyFromSystemIn(Perceptron perceptron) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                System.out.println("Type in vector to classify: ");
                String input = scanner.nextLine().trim();
                double[] values = Arrays.stream(input.split("\\s*,\\s*"))
                                        .mapToDouble(Double::parseDouble)
                                        .toArray();
                Vector v = new Vector(values);
                int value = perceptron.classify(v);
                System.out.println(v + " classified as: " + value);
            }
            catch (Exception e) {
                System.out.println("Error occured: " + e);
            }
        }
    }
    
    private static List<BinaryObservation> getBinaryObservations(
        List<Observation> observations, Map<String, Integer> classBinaryMap)
    {
        return observations
            .stream()
            .map(observation ->
                     new BinaryObservation(
                         observation, classBinaryMap.get(observation.getClassification())))
            .collect(Collectors.toList());
    }
    
    private static Map<String, Integer> getClassToBinaryMap(List<Observation> observations) {
        List<String> classes = observations.stream()
                                           .map(Observation::getClassification)
                                           .distinct()
                                           .collect(Collectors.toList());
        if (classes.size() != 2)
            throw new RuntimeException("Input data incorrect: wrong class count");
        
        Map<String, Integer> classValue = new HashMap<>();
        classValue.put(classes.get(0), 0);
        classValue.put(classes.get(1), 1);
        return classValue;
    }
}
