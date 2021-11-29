import java.util.concurrent.ExecutionException;
import java.util.stream.LongStream;

public class Main {
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

//        final int size = 7;
//        int capacity = 15;
//        int weights[] = {2, 3, 5, 7, 1, 4, 1};
//        int values[] = {10, 5, 15, 7, 6, 18, 3};
//
        final int size = 32;
        int capacity = 75;
        int weights[] = {
            3, 1, 6, 10, 1, 4, 9, 1, 7, 2, 6, 1, 6, 2, 2, 4, 8, 1, 7, 3, 6, 2, 9, 5, 3, 3, 4, 7, 3, 5, 30, 50
        };
        int values[] = {
            7, 4, 9, 18, 9, 15, 4, 2, 6, 13, 18, 12, 12, 16, 19, 19, 10, 16, 14, 3, 14, 4, 15, 7, 5, 10, 10, 13, 19, 9,
            8, 5
        };


//        final int size = 24 ;
//        int capacity = 6404180;
//        int weights[] = Arrays.stream(("382745\n" +
//                                       "799601\n" +
//                                       "909247\n" +
//                                       "729069\n" +
//                                       "467902\n" +
//                                       " 44328\n" +
//                                       " 34610\n" +
//                                       "698150\n" +
//                                       "823460\n" +
//                                       "903959\n" +
//                                       "853665\n" +
//                                       "551830\n" +
//                                       "610856\n" +
//                                       "670702\n" +
//                                       "488960\n" +
//                                       "951111\n" +
//                                       "323046\n" +
//                                       "446298\n" +
//                                       "931161\n" +
//                                       " 31385\n" +
//                                       "496951\n" +
//                                       "264724\n" +
//                                       "224916\n" +
//                                       "169684")
//                                          .split("\\s+"))
//                              .mapToInt(Integer::parseInt)
//                              .toArray();
//        int values[] =
//            Arrays.stream(("825594\n" +
//                           "1677009\n" +
//                           "1676628\n" +
//                           "1523970\n" +
//                           " 943972\n" +
//                           "  97426\n" +
//                           "  69666\n" +
//                           "1296457\n" +
//                           "1679693\n" +
//                           "1902996\n" +
//                           "1844992\n" +
//                           "1049289\n" +
//                           "1252836\n" +
//                           "1319836\n" +
//                           " 953277\n" +
//                           "2067538\n" +
//                           " 675367\n" +
//                           " 853655\n" +
//                           "1826027\n" +
//                           "  65731\n" +
//                           " 901489\n" +
//                           " 577243\n" +
//                           " 466257\n" +
//                           " 369261")
//                              .split("\\s+"))
//                  .mapToInt(Integer::parseInt)
//                  .toArray();
//
//        final int size = 10;
//        int capacity = 165;
//        int weights[] = {23,
//                         31,
//                         29,
//                         44,
//                         53,
//                         38,
//                         63,
//                         85,
//                         89,
//                         82};
//        int values[] = {92,
//                        57,
//                        49,
//                        68,
//                        60,
//                        43,
//                        67,
//                        84,
//                        87,
//                        72};
        
        //var result = bestFromRange(0, (long) Math.pow(2, size), size, capacity, weights, values);
        
        
        var result = LongStream
            .range(0, (long) Math.pow(2, size))
            .parallel()
            .mapToObj(
                n -> {
                    int weight = 0;
                    int value = 0;
                    for (int j = 0; j < size; ++j) {
                        long positionUsed = ((n >>> j) & 1);
                        if (positionUsed == 0)
                            continue;
                        weight += weights[j];
                        value += values[j];
                    }
                    if (weight > capacity)
                        return null;
                    else return new Tuple<>(n, value);
                })
            .reduce(
                (left, right) -> {
                    if (left == null)
                        return right;
                    else if (right == null)
                        return left;
                    
                    return left.getSecond() >= right.getSecond() ? left : right;
                })
            .get();
        
        long best = result.getFirst();
        int bestValue = result.getSecond();


//        long start = 0;
//        long end = (long) Math.pow(2, size);
//
//        final int asyncCalls = 16;
//        ExecutorService executorService = Executors.newFixedThreadPool(16);
//        List<Future<Tuple<Long, Integer>>> futures = new ArrayList<>();
//
//        for (int i = 0; i < asyncCalls; ++i) {
//            long s = end * i / asyncCalls;
//            long e = end * (i+1) / asyncCalls;
//
//            futures.add(
//                executorService.submit(() -> bestFromRange(s, e, size, capacity, weights, values))
//            );
//        }
//
//        long best = 0;
//        int bestValue = 0;


//
//        for (var future : futures) {
//            var results = future.get();
//
//            if (results.getSecond() > bestValue) {
//                best = results.getFirst();
//                bestValue = results.getSecond();
//            }
//        }
//        executorService.shutdown();
        
        System.out.println("best permutation: " + Long.toBinaryString(best));
        System.out.println("best value: " + bestValue);
        
        System.out.println("millis: " + (System.currentTimeMillis() - startTime));
    }
    
    
    static Tuple<Long, Integer> bestFromRange(
        long start, long end, int size, int capacity, int weights[], int values[]
    )
    {
        long best = 0;
        int bestValue = 0;
        
        for (long i = start, max = end; i < max; ++i) {
            int weight = 0;
            int value = 0;
            for (int j = 0; j < size; ++j) {
                long positionUsed = ((i >>> j) & 1);
                if (positionUsed == 0)
                    continue;
                weight += weights[j];
                value += values[j];
            }
            if (weight <= capacity && value > bestValue) {
                best = i;
                bestValue = value;
            }
        }
        
        return new Tuple<>(best, bestValue);
    }
    
}
