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
    
    
    public double length() {
        double sum = Arrays.stream(values)
                           .map(v -> v * v)
                           .sum();
        return Math.sqrt(sum);
    }
    
    public Vector negative() {
        double[] newValues = Arrays.stream(values)
                                   .map(v -> -v)
                                   .toArray();
        return new Vector(newValues);
    }
    
    public Vector normalized() {
        double length = length();
        double[] newValues = Arrays.stream(values)
                                   .map(v -> v / length)
                                   .toArray();
        return new Vector(newValues);
    }
    
    public Vector add(Vector v) {
        ensureMatchingSizes(v);
        
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); ++i)
            newValues[i] = value(i) + v.value(i);
            
        return new Vector(newValues);
    }
    
    public Vector subtract(Vector v) {
        ensureMatchingSizes(v);
    
        double[] newValues = new double[size()];
        for (int i = 0; i < size(); ++i)
            newValues[i] = value(i) - v.value(i);
    
        return new Vector(newValues);
    }
    
    public Vector multiply(double multiplier) {
        double[] newValues = Arrays.stream(values)
                                   .map(v -> v * multiplier)
                                   .toArray();
        return new Vector(newValues);
    }
    
    public Vector divide(double divider) {
        double[] newValues = Arrays.stream(values)
                                   .map(v -> v / divider)
                                   .toArray();
        return new Vector(newValues);
    }
    
    public double squaredDistance(Vector v) {
        ensureMatchingSizes(v);
    
        double sum = 0;
        for (int i = 0; i < size(); ++i)
            sum += Math.pow(value(i) - v.value(i), 2);
        
        return sum;
    }
    
    public double distance(Vector v) {
        ensureMatchingSizes(v);
        
        return Math.sqrt(squaredDistance(v));
    }
    
    public double dotProduct(Vector v) {
        ensureMatchingSizes(v);
        
        double sum = 0;
        for (int i = 0; i < size(); ++i)
            sum += value(i) * v.value(i);
        
        return sum;
    }
    
    private void ensureMatchingSizes(Vector v) {
        if (this.size() != v.size())
            throw new NotMatchingSizesException("sizes: " + size() + " and " + v.size());
    }
    
    @Override
    public String toString() {
        return Arrays.toString(values);
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        
        Vector vector = (Vector) o;
    
        return Arrays.equals(values, vector.values);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }
    
    private static class NotMatchingSizesException extends RuntimeException {
        public NotMatchingSizesException() {
        }
    
        public NotMatchingSizesException(String message) {
            super(message);
        }
    
        public NotMatchingSizesException(String message, Throwable cause) {
            super(message, cause);
        }
    
        public NotMatchingSizesException(Throwable cause) {
            super(cause);
        }
    
        public NotMatchingSizesException(String message, Throwable cause, boolean enableSuppression,
                                         boolean writableStackTrace)
        {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
