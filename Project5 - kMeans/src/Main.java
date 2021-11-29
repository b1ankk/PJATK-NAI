import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        
        final int groups = 3;
        List<Observation> data = DataLoader.loadDataFromFile("iris.data");
    
        Classifier classifier = new Classifier(data, 3);
        classifier.classify();
        
    }
}
