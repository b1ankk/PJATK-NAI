import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        final int k = Integer.parseInt(args[0]);
        final String trainSetFile = args[1];
        final String testSetFile = args[2];
        
        List<Observation> trainData = loadDataFromFile(trainSetFile);
        List<Observation> testData = loadDataFromFile(testSetFile);

        Classifier classifier = new Classifier(k, trainData);
        int correctCounter = 0;
        
        for (Observation testedObservation : testData) {
            String classification = classifier.classify(testedObservation);
    
            if (classification.equals(testedObservation.getClassification())) {
                System.out.println(testedObservation + " correctly classified as " + classification);
                correctCounter++;
            }
            else
                System.out.println(testedObservation + " incorrectly classified as " + classification);
        }
    
        Locale.setDefault(Locale.US);
        System.out.printf("Accuracy: %.2f%%%n", ((double)correctCounter / testData.size() * 100.));
        
        classifyFromSystemIn(classifier);
    }
    
    
    private static void classifyFromSystemIn(Classifier classifier) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                System.out.println("Type in vector to classify: ");
                String input = scanner.nextLine().trim();
                double[] values = Arrays.stream(input.split("\\s*,\\s*"))
                                        .mapToDouble(Double::parseDouble)
                                        .toArray();
                Vector v = new Vector(values);
                String classification = classifier.classify(new Observation(v, ""));
                System.out.println(v + " classified as: " + classification);
            }
            catch (Exception e) {
                System.out.println("Error occured: " + e);
            }
        }
    }
    
    
    private static List<Observation> loadDataFromFile(String file) throws IOException {
        List<Observation> data = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.lines().forEach(
                line -> {
                    String[] parts = line.split("\\s*,\\s*");
                    
                    double[] values = new double[parts.length-1];
                    for (int i = 0; i < values.length; i++)
                        values[i] = Double.parseDouble(parts[i].trim());
                    
                    Vector v = new Vector(values);
                    String classification = parts[parts.length-1];
                    
                    data.add(new Observation(v, classification));
                }
            );
        }
        return data;
    }
}
