import java.util.Random;
import java.util.function.Function;

public class Perceptron {
    private double alpha;
    private double beta;
    
    private Vector weights;
    private double theta = 0;
//    private final Function<Double, Integer> thresholdFunction = net -> net >= 0 ? 1 : 0;
    private final Function<Double, Double> thresholdFunction = net -> 1 / (1 + Math.exp(-net));
    
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
    
    
    public double classify(Vector inputVector) {
        double net = weights.dotProduct(inputVector) - theta;
        return thresholdFunction.apply(net);
    }
    
    
    public double trainObservation(BinaryObservation observation) {
        Vector x = observation.getVector();
        int d = observation.getClassificationValue();
        double y = classify(x);
        double errorSignal = (d - y) * y * (1 - y);
        
        weights = weights.add(x.multiply(alpha * errorSignal));
        theta = theta - beta * errorSignal;
    
        double error =  Math.pow(d - y, 2);
        return error;
    }
    
}
