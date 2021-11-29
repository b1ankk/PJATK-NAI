import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<Observation> loadDataFromFile(String file) throws IOException {
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
