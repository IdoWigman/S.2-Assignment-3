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

    /*
    Time complexity: Theta(N) worst case.
    Instantiating a skip list and a hash table in the size of N each has a runtime of Theta(N).
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

    /*
    Time complexity: Theta(log n) expected.
    The method contains has a runtime of Theta(1).
    A skip list has an expected runtime of Theta(log n) to insert a value.
    The reference has Theta(1) runtime to insert it to the hash table.
     */
    public boolean insert(int value) {
        if (contains(value)) {
            return false;
        }
        AbstractSkipList.SkipListNode newNode = skipList.insert(value);
        hashTable.insert((long) value, newNode);
        return true;
    }
    /*
    Time complexity: Theta(log n) expected if the value exists, Theta(1) if doesn't.
    Search on a hashtable has an expected runtime of Theta(1) (stops here if the value doesn't exist).
    Using the reference, deleting it from a skip list an expected runtime of Theta(log n).
    Deleting the value from the hash table has an expected runtime of Theta(1).
     */

    public boolean delete(int value) {
        AbstractSkipList.SkipListNode nodeToDelete = hashTable.search((long) value);
        if (nodeToDelete == null) {
            return false;
        }
        skipList.delete(nodeToDelete);
        hashTable.delete((long) value);
        return true;
    }

    /*
    Time complexity: Theta(1) expected.
    A hash table has an expected runtime of Theta(1) to search for a value.
     */

    public boolean contains(int value) {
        return hashTable.search((long) value) != null;
    }

    /*
    Time complexity: Theta(log n) expected.
    The Indexable Skip List as an expected runtime of Theta(log n) for the rank method.
     */

    public int rank(int value) {
        return skipList.rank(value);
    }

    /*
    Time complexity: Theta(log n) expected.
    The Indexable Skip List as an expected runtime of Theta(log n) for the select method.
     */

    public int select(int index) {
        return skipList.select(index);
    }

    /*
     Time Complexity: Theta(|L|) Expected
     Theta(1) lookup to find the 'low' node instantly via Hash Table.
     Traverses |L| steps forward using Level 0 linked list pointers.
     */

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
