import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String POSITIVE = "p";
    
    
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        
        List<Observation> trainData = loadData("agaricus-lepiota.data");
        List<Observation> testData = loadData("agaricus-lepiota.test.data");
        
        Classifier classifier = new Classifier(trainData);
        
        double truePositiveCount = 0;
        double falsePositiveCount = 0;
        double trueNegativeCount = 0;
        double falseNegativeCount = 0;
        
        for (Observation testObs : testData) {
            String realClassification = testObs.getClassification();
            String classification = classifier.classify(testObs);
    
            System.out.print(classification.equals(realClassification) ? "\u001B[0m" : "\u001B[31m");
            System.out.println(testObs.getClassification() + " classified as: " + classification);
            
            if (classification.equals(POSITIVE)) {
                if (classification.equals(realClassification))
                    truePositiveCount++;
                else falsePositiveCount++;
            }
            else {
                if (classification.equals(realClassification))
                    trueNegativeCount++;
                else falseNegativeCount++;
            }
        }
        
        double accuracy = (truePositiveCount + trueNegativeCount) / testData.size();
        double precision = truePositiveCount / (truePositiveCount + falsePositiveCount);
        double recall = truePositiveCount / (truePositiveCount + falseNegativeCount);
        double f = 2. * precision * recall / (precision + recall);
        
        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall);
        System.out.println("F: " + f);
        
        
        System.out.println((System.currentTimeMillis() - start) / 1000. + " seconds");
    }
    
    public static List<Observation> loadData(String file) throws IOException {
        return Files.readAllLines(Path.of(file))
                    .stream()
                    .map(
                        line ->
                        {
                            line = line.trim();
                            String[] parts = line.split("\\s*,\\s*");
                            String classification = parts[0];
                            String[] attributes = Arrays.copyOfRange(parts, 1, parts.length);
                            return new Observation(classification, attributes);
                        }
                    )
                    .collect(Collectors.toCollection(ArrayList::new));
    }
}
