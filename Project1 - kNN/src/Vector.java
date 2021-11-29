import java.util.Arrays;

public class Vector {
    private final double[] values;
    
    public Vector(int size) {
        this.values = new double[size];
    }
    
    public Vector(double ... values) {
        this.values = Arrays.copyOf(values, values.length);
    }
    
    public int size() {
        return values.length;
    }
    
    public double value(int n) {
        return values[n];
    }
    
    
    public double distance(Vector v) {
        if (this.size() != v.size())
            throw new UnequalSizesException("sizes: " + size() + " and " + v.size());
        
        double sum = 0;
        for (int i = 0; i < size(); ++i)
            sum += Math.pow(value(i) - v.value(i), 2);
        
        return Math.sqrt(sum);
    }
    
    @Override
    public String toString() {
        return Arrays.toString(values);
    }
    
    
    private static class UnequalSizesException extends RuntimeException {
        public UnequalSizesException() {
        }
    
        public UnequalSizesException(String message) {
            super(message);
        }
    
        public UnequalSizesException(String message, Throwable cause) {
            super(message, cause);
        }
    
        public UnequalSizesException(Throwable cause) {
            super(cause);
        }
    
        public UnequalSizesException(String message, Throwable cause, boolean enableSuppression,
                                     boolean writableStackTrace)
        {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
