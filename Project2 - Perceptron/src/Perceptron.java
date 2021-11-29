import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Perceptron {
    private double maxError = 0.01;
    private int maxIterations = 10_000;
    
    private double alpha;
    private double beta;
    
    private Vector weights;
    private double theta = 0;
    private final Function<Double, Integer> thresholdFunction = net -> net >= 0 ? 1 : 0;
    
    public Perceptron(double alpha, double beta, int inputCount) {
        this.alpha = alpha;
        this.beta = beta;
        this.weights = getRandomWeights(inputCount);
    }
    
    private static Vector getRandomWeights(int size) {
        Random random = new Random();
        double[] values = random.doubles(size).toArray();
        return new Vector(values);
    }
    
    
    public int classify(Vector inputVector) {
        double net = weights.dotProduct(inputVector) - theta;
        return thresholdFunction.apply(net);
    }
    
    public double train(List<BinaryObservation> observations) {
        double iterationError = 1;
        
        for (int i = 0; iterationError > maxError && i < maxIterations; ++i)
            iterationError = performIteration(observations);
        
        return iterationError;
    }
    
    public double performIteration(List<BinaryObservation> observations) {
        double errorSum = 0;
        for (BinaryObservation observation : observations)
            errorSum += trainObservation(observation);
        
        double averageError = errorSum / observations.size();
        return averageError;
    }
    
    private double trainObservation(BinaryObservation observation) {
        Vector x = observation.getVector();
        int d = observation.getClassificationValue();
        int y = classify(x);
        
        weights = weights.add(x.multiply(alpha * (d - y)));
        theta = theta - beta * (d - y);
    
        double error = Math.pow(d - y, 2);
        return error;
    }
    
}
