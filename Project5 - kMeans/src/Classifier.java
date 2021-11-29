import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Classifier {
    private final List<Observation> data;
    private final int groups;
    
    public Classifier(List<Observation> data, int groups) {
        this.data = data;
        this.groups = groups;
    }
    
    public void classify() {
        List<Vector> centroids = generateRandomCentroids();
        printCentroids(centroids);
        
        for (int i = 0; i < 15; ++i) {
            centroids = recalculateCentroids(centroids);
            printCentroids(centroids);
        }
        
        
    }
    
    private List<Vector> generateRandomCentroids() {
        List<Integer> indexes = IntStream.range(0, data.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);
        
        List<Vector> centroids = new ArrayList<>();
        for (int group = 0; group < groups; group++) {
            Vector groupSum = new Vector(data.get(0).getVector().size());
            int groupStart = data.size() / groups * group;
            int groupEnd = data.size() / groups * (group+1);
        
            for (int i = groupStart; i < groupEnd; i++) {
                Vector vector = data.get(indexes.get(i)).getVector();
                groupSum = groupSum.add(vector);
            }
        
            Vector centroid = groupSum.divide(groupEnd - groupStart);
            centroids.add(centroid);
        }
        
        return centroids;
    }
    
    private List<Vector> recalculateCentroids(List<Vector> centroids) {
        Map<Vector, List<Vector>> centroidsAndClosestVectors = new HashMap<>();
        for (Vector centroid : centroids)
            centroidsAndClosestVectors.put(centroid, new ArrayList<>());
        
        
        for (Vector vector : data.stream().map(Observation::getVector).collect(Collectors.toList())) {
            Vector closestCentroid = getNearestCentroid(vector, centroids);
            centroidsAndClosestVectors.get(closestCentroid).add(vector);
        }
        
        List<Vector> newCentroids =
            centroids.stream()
                     .map(centroid -> meanOfVectors(centroidsAndClosestVectors.get(centroid)))
                     .collect(Collectors.toList());
        return newCentroids;
    }
    
    private Vector getNearestCentroid(Vector vector, List<Vector> centroids) {
        return centroids.stream()
                        .min(Comparator.comparingDouble(
                            centroid -> centroid.squaredDistance(vector)))
                        .get();
    }
    
    
    private Vector meanOfVectors(List<Vector> vectors) {
        int vectorSize = data.get(0).getVector().size();
        Vector sum = vectors.stream()
                            .reduce(new Vector(vectorSize), Vector::add);
        
        return sum.divide(vectors.size());
    }
    
    private void printCentroids(Collection<Vector> centroids) {
        StringBuilder sb = new StringBuilder();
        for (Vector centroid : centroids) {
            sb.append("[ ");
            for (int i = 0; i < centroid.size(); ++i) {
                sb.append(
                    String.format("%.2f%s", centroid.value(i), i < centroid.size()-1 ? ", " : "")
                );
            }
            sb.append(" ] \t");
        }
        System.out.println(sb);
    }
}
