import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DataLoader {
    
    static List<Observation> loadLanguageData(String trainDirectory) {
        try {
            return Files.walk(Path.of(trainDirectory), 1)
                        .skip(1)
                        .filter(Files::isDirectory)
                        .map(DataLoader::loadWordFiles)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    private static List<Observation> loadWordFiles(Path path) {
        String language = path.getFileName().toString();
        
        try {
            return Files.walk(path, 1)
                        .filter(Files::isRegularFile)
                        .map(DataLoader::loadStatisticVector)
                        .map(vector -> new Observation(vector, language))
                        .collect(Collectors.toList());
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        
        
    }
    
    private static Vector loadStatisticVector(Path path) {
        try {
            String fileContents = Files.readString(path);
            
            return getStatisticsVector(fileContents);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    public static Vector getStatisticsVector(String text) {
        double[] counts = new double['z' - 'a' + 1];
    
        for (char c : text.toLowerCase().toCharArray()) {
            if (c >= 'a' && c <= 'z')
                ++counts[c - 'a'];
        }
    
        Vector v = new Vector(counts);
        return v.normalized();
    }
}
