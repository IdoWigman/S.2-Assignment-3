import java.util.Collections; // can be useful
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class HashingExperimentUtils {
    final private static int k = 16;
    private static final int NUM_EXPERIMENTS = 30;
    private static final double[] PROBING_ALPHAS = {0.5, 0.75, 0.875, 0.9375};
    private static final double[] CHAINING_ALPHAS = {0.5, 0.75, 1.0, 1.5, 2.0};
    private static final Random RANDOM = new Random();

    public static double[] measureInsertionsProbing() {
        double[] results = new double[PROBING_ALPHAS.length];

        HashFactory hashFactory = new MultiplicativeShiftingHash();
        for (int i = 0; i < PROBING_ALPHAS.length; i++) {
            double currentAlpha = PROBING_ALPHAS[i];
            int numElementsToInsert = (int) (Math.pow(2, 16) * currentAlpha);
            double totalAlphaTime = 0;

            for (int exp =  0; exp < NUM_EXPERIMENTS; exp++) {
                long totalExpTime = 0;
                ProbingHashTable hashTable = new ProbingHashTable(hashFactory, k, 1);
                while (hashTable.getSize() < numElementsToInsert) {
                    int randomKey = RANDOM.nextInt();
                    int randomValue = RANDOM.nextInt();
                    int sizeBefore = hashTable.getSize();
                    long start = System.nanoTime();
                    hashTable.insert(randomKey, randomValue);
                    long finish = System.nanoTime();
                    if (hashTable.getSize() > sizeBefore) {
                        totalExpTime += (finish - start);
                    }
                }
                totalAlphaTime += ((double) totalExpTime/ numElementsToInsert);
            }
            results[i] = totalAlphaTime /NUM_EXPERIMENTS;
        }
        return results;
    }

    public static double[] measureSearchesProbing() {
        double[] results = new double[PROBING_ALPHAS.length];

        HashFactory hashFactory = new MultiplicativeShiftingHash();
        for (int i = 0; i < PROBING_ALPHAS.length; i++) {
            double currentAlpha = PROBING_ALPHAS[i];
            int numElementsToInsert = (int) (Math.pow(2, 16) * currentAlpha);
            double totalAlphaTime = 0;
            for (int exp = 0; exp < NUM_EXPERIMENTS; exp++) {
                long totalExpTime = 0;
                ProbingHashTable<Integer, Integer> hashTable = new ProbingHashTable<>(hashFactory, k, 1);
                List<Integer> insertedKeys = new ArrayList<>();

                while (hashTable.getSize() < numElementsToInsert) {
                    int randomKey = RANDOM.nextInt();
                    int randomValue = RANDOM.nextInt();

                    int sizeBefore = hashTable.getSize();
                    hashTable.insert(randomKey, randomValue);

                    if (hashTable.getSize() > sizeBefore) {
                        insertedKeys.add(randomKey);
                    }
                }

                int totalSearches = numElementsToInsert;
                int[] searchQueries = new int[totalSearches];
                for (int j = 0; j < totalSearches / 2; j++) {
                    int randomIndex = RANDOM.nextInt(insertedKeys.size());
                    searchQueries[j] = insertedKeys.get(randomIndex);
                }

                for (int j = totalSearches / 2; j < totalSearches; j++) {
                    int badKey = RANDOM.nextInt();
                    while (hashTable.search(badKey) != null) {
                        badKey = RANDOM.nextInt();
                    }
                    searchQueries[j] = badKey;
                }

                long start = System.nanoTime();
                for (int j = 0; j < totalSearches; j++) {
                    hashTable.search(searchQueries[j]);
                }
                long finish = System.nanoTime();
                totalExpTime += (finish - start);
                totalAlphaTime += ((double) totalExpTime / totalSearches);
            }
            results[i] = totalAlphaTime /NUM_EXPERIMENTS;
        }
        return results;
    }

    public static double[] measureInsertionsChaining() {
        double[] results = new double[CHAINING_ALPHAS.length];

        HashFactory hashFactory = new MultiplicativeShiftingHash();
        for (int i = 0; i < CHAINING_ALPHAS.length; i++) {
            double currentAlpha = CHAINING_ALPHAS[i];
            int numElementsToInsert = (int) (Math.pow(2, 16) * currentAlpha);
            double totalAlphaTime = 0;

            for (int exp =  0; exp < NUM_EXPERIMENTS; exp++) {
                long totalExpTime = 0;
                ChainedHashTable hashTable = new ChainedHashTable(hashFactory, k, 2.1);
                while (hashTable.getSize() < numElementsToInsert) {
                    int randomKey = RANDOM.nextInt();
                    int randomValue = RANDOM.nextInt();
                    int sizeBefore = hashTable.getSize();
                    long start = System.nanoTime();
                    hashTable.insert(randomKey, randomValue);
                    long finish = System.nanoTime();
                    if (hashTable.getSize() > sizeBefore) {
                        totalExpTime += (finish - start);
                    }
                }
                totalAlphaTime += ((double) totalExpTime/ numElementsToInsert);
            }
            results[i] = totalAlphaTime /NUM_EXPERIMENTS;
        }
        return results;
    }

    public static double[] measureSearchesChaining() {
        throw new UnsupportedOperationException("Delete this line and replace it with your implementation");
    }
}
