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
        if (contains(value)) {
            return false;
        }
        AbstractSkipList.SkipListNode newNode = skipList.insert(value);
        hashTable.insert((long) value, newNode);
        return true;
    }

    public boolean delete(int value) {
        AbstractSkipList.SkipListNode nodeToDelete = hashTable.search((long) value);
        if (nodeToDelete == null) {
            return false;
        }
        skipList.delete(nodeToDelete);
        hashTable.delete((long) value);
        return true;
    }

    public boolean contains(int value) {
        return hashTable.search((long) value) != null;
    }

    public int rank(int value) {
        return skipList.rank(value);
    }

    public int select(int index) {
        return skipList.select(index);
    }

    public List<Integer> range(int low, int high) {
        AbstractSkipList.SkipListNode curr = hashTable.search((long) low);

        if (curr == null) {
            return null;
        }

        List<Integer> result = new ArrayList<>();

        while (curr != null && (int) curr.key() <= high) {
            result.add((int) curr.key());
            curr = curr.getNext(0);
        }

        return result;
    }
}
