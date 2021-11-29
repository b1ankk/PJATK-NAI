import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Map<Character, Integer> charCounts = getFromInputStream();
        
//        charCounts.put('A', 5);
//        charCounts.put('B', 9);
//        charCounts.put('C', 12);
//        charCounts.put('D', 13);
//        charCounts.put('E', 16);
//        charCounts.put('F', 45);

//        charCounts.put('A', 4);
//        charCounts.put('B', 5);
//        charCounts.put('C',7);
//        charCounts.put('D', 8);
//        charCounts.put('E', 10);
//        charCounts.put('F', 12);
//        charCounts.put('G', 20);

//        charCounts.put('A', 3);
//        charCounts.put('B', 4);
//        charCounts.put('C', 6);
//        charCounts.put('D', 6);
//        charCounts.put('E', 7);
//        charCounts.put('F', 8);
        
//        charCounts.put('A', 6);
//        charCounts.put('B', 1);
//        charCounts.put('C', 3);
//        charCounts.put('D', 3);
//        charCounts.put('E', 6);
//        charCounts.put('F', 2);
        
        
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>();
        charCounts.forEach(
            (character, value) -> nodes.add(
                new HuffmanLeafNode(
                    value, null, null, character
                )
            )
        );
        
        while (nodes.size() > 1) {
            HuffmanNode node1 = nodes.poll();
            HuffmanNode node2 = nodes.poll();
            
            HuffmanNode minNode = HuffmanNode.min(node1, node2);
            HuffmanNode maxNode = HuffmanNode.max(node1, node2);
            
            HuffmanNode newNode = new HuffmanNode(
                node1.getValue() + node2.getValue(),
                minNode, maxNode
            );
            
            nodes.add(newNode);
        }
    
        SortedMap<Character, String> results = new TreeMap<>();
        collectHuffman(nodes.peek(), "", results);
        
        results.forEach(
            (character, code) -> System.out.println(character + " - " + code)
        );
        
    }
    
    private static void collectHuffman(HuffmanNode node, String prefix, SortedMap<Character, String> results) {
        if (node instanceof HuffmanLeafNode leafNode) {
            results.put(leafNode.getCharacter(), prefix);
            return;
        }
        
        collectHuffman(node.getLeft(), prefix + "0", results);
        collectHuffman(node.getRight(), prefix + "1", results);
    }
    
    private static Map<Character, Integer> getFromInputStream() {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        return text.trim()
                   .chars()
                   .boxed()
                   .collect(Collectors.groupingBy(
                       c -> (char) c.intValue(), Collectors.reducing(
                           0, x -> 1, Integer::sum)));
    }
}
