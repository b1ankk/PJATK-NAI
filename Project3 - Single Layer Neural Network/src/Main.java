import java.util.*;


// Chrząszcz brzmi w trzcinie w Szczebrzeszynie.
// To be, or not to be, that is the question
// Meine Frau heißt Mercedes

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        
        final String trainDirectory = "data/lang/";
        final String testDirectory = "data/lang.test";
        
        List<Observation> trainObservations = DataLoader.loadLanguageData(trainDirectory);
        Map<String, Integer> classMap = mapObservationsToIds(trainObservations);
        
        int inputSize = 'z' - 'a' + 1;
        Network network = new Network(inputSize, classMap.size(), 0.005, classMap);
        network.train(trainObservations);
        
        List<Observation> testObservations = DataLoader.loadLanguageData(testDirectory);
    
        System.out.println(classMap);
        for (Observation obs : testObservations) {
            String classification = network.classify(obs);
            System.out.println(obs.getClassification() + " classified as: " + classification);
        }
        
        classifyFromSystemInLoop(network);
    }
    
    private static void classifyFromSystemInLoop(Network network) {
        Scanner scanner = new Scanner(System.in);
        String text;
        
        while (true) {
            text = scanner.nextLine();
            Observation obs = new Observation(DataLoader.getStatisticsVector(text), "input");
            String classification = network.classify(obs);
            System.out.println(obs.getClassification() + " classified as: " + classification);
        }
    }
    
    private static Map<String, Integer> mapObservationsToIds(List<Observation> observations) {
        Map<String, Integer> classMap = new HashMap<>();
        observations.stream()
                         .map(Observation::getClassification)
                         .distinct()
                         .forEach(cls -> classMap.put(cls, classMap.size()));
        return classMap;
    }
    
}
