import java.util.Random;

public class MultiplicativeShiftingHash implements HashFactory<Long> {
    private HashingUtils utils;
    private Random rand;

    public MultiplicativeShiftingHash() {
        utils = new HashingUtils();
        this.rand = new Random();
    }

    @Override
    public HashFunctor<Long> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Long> {
        final public static long WORD_SIZE = 64;
        final private long a;
        final private long k;

        public Functor(int k) {
            this.k = k;
            a = rand.nextLong(2L, Long.MAX_VALUE);
        }
        @Override
        public int hash(Long key) {
            long mult = a * key;
            long hash = mult >>> (WORD_SIZE - k);
            return (int) hash;
        }

        public long a() {
            return a;
        }

        public long k() {
            return k;
        }
    }
}
