import java.util.Random;

public class ModularHash implements HashFactory<Integer> {
    private Random rand;
    private HashingUtils utils;

    public ModularHash() {
        this.rand = new Random();
        this.utils = new HashingUtils();
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor(int k){
            this.m = (int) Math.pow(2,k);
            long lower = 2147483648L; // Integer.MAX_VALUE + 1
            long higher = 2147583648L; // lowerBound + 100,000
            this.p = utils.genPrime(lower, higher);
            this.a = rand.nextInt(Integer.MAX_VALUE) + 1;
            this.b = rand.nextInt(Integer.MAX_VALUE);
        }

        @Override
        public int hash(Integer key) {
            long mult = (long) a * key + b;
            return (int) (HashingUtils.mod(HashingUtils.mod(mult, p), m));
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
