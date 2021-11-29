import java.util.Comparator;
import java.util.function.BiFunction;

public class HuffmanNode implements Comparable<HuffmanNode> {
    private static final Comparator<HuffmanNode> comparator =
        Comparator.comparingInt(HuffmanNode::getValue)
                  .thenComparingInt(HuffmanNode::getMinChar);
    
    private final int value;
    private final HuffmanNode left;
    private final HuffmanNode right;
    
    
    public HuffmanNode(int value, HuffmanNode left, HuffmanNode right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }
    
    public int getValue() {
        return value;
    }
    
    public HuffmanNode getLeft() {
        return left;
    }
    
    public HuffmanNode getRight() {
        return right;
    }
    
    
    private char recursiveCharReduce(BiFunction<Character, Character, Character> reduceFunction) {
        if (this instanceof HuffmanLeafNode leafNode) {
            return leafNode.getCharacter();
        }
    
        try {
            char leftChar = this.getLeft().getMinChar();
            char rightChar = this.getRight().getMinChar();
        
            return reduceFunction.apply(leftChar, rightChar);
        }
        catch (NullPointerException e) {
            throw new RuntimeException("Leaf node is not a " + HuffmanLeafNode.class.getSimpleName() + " instance");
        }
    }
    
    public char getMinChar() {
        return recursiveCharReduce(
            (leftChar, rightChar) -> (char) Math.min(leftChar, rightChar)
        );
    }
    
    public char getMaxChar() {
        return recursiveCharReduce(
            (leftChar, rightChar) -> (char) Math.max(leftChar, rightChar)
        );
    }
    
    
    @Override
    public int compareTo(HuffmanNode other) {
        return comparator.compare(this, other);
    }
    
    @Override
    public String toString() {
        return "HuffmanNode{" +
               "value=" + value +
               ", left=" + left +
               ", right=" + right +
               '}';
    }
    
    public static HuffmanNode min(HuffmanNode hn1, HuffmanNode hn2) {
        if (hn1.compareTo(hn2) <= 0)
            return hn1;
        return hn2;
    }
    
    public static HuffmanNode max(HuffmanNode hn1, HuffmanNode hn2) {
        if (min(hn1, hn2) == hn1)
            return hn2;
        return hn1;
    }
    
    
    
    
    
}
