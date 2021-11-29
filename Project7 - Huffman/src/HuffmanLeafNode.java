public class HuffmanLeafNode extends HuffmanNode {
    private final char character;
    
    public HuffmanLeafNode(int value, HuffmanNode left, HuffmanNode right, char character) {
        super(value, left, right);
        this.character = character;
    }
    
    public char getCharacter() {
        return character;
    }
    
    @Override
    public String toString() {
        return "HuffmanLeafNode{" +
               "value=" + getValue() +
               ", character=" + character +
               '}';
    }
}
