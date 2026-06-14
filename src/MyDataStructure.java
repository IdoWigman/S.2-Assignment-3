import java.util.List;
import java.util.ArrayList;

public class MyDataStructure {

    IndexableSkipList skipList;
    ChainedHashTable<Long, AbstractSkipList.SkipListNode> hashTable;

    /***
     * This function is the Init function described in Part 4.
     *
     * @param N The maximal number of items that may reside in the DS.
     */
    public MyDataStructure(int N) {
        this.skipList = new IndexableSkipList(N);
        HashFactory<Long> hashFactory = new MultiplicativeShiftingHash();
        int k = (int) Math.ceil(Math.log(N) / Math.log(2));
        this.hashTable = new ChainedHashTable<>(hashFactory, k, 2.0);
    }

    /*
     * In the following functions,
     * you should REMOVE the place-holder return statements.
     */
    public boolean insert(int value) {
        return false;
    }

    public boolean delete(int value) {
        return false;
    }

    public boolean contains(int value) {
        return false;
    }

    public int rank(int value) {
        return -1;
    }

    public int select(int index) {
        return Integer.MIN_VALUE;
    }

    public List<Integer> range(int low, int high) {
        return null;
    }
}
