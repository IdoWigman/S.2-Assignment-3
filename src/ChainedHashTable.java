import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private List<Element<K,V>>[] table;

    private int size = 0;

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new List[this.capacity];

    }

    public V search(K key) {
        int hash = hashFunc.hash(key);
        List<Element<K, V>> bucket = table[hash];
        if (bucket == null) {
            return null;
        }
        for (Element<K,V> element : bucket) {
            if (element.key().equals(key)) {
                return element.satelliteData();
            }
        }
        return null;
    }

    public void insert(K key, V value) {
        if (search(key) != null) {
            return;
        }
        if ((double) size / capacity >= maxLoadFactor)
            rehash();
        int hash = hashFunc.hash(key);
        List<Element<K, V>> bucket = table[hash];
        bucket.add(new Element<>(key, value));
        size++;
    }

    private void rehash() {
        List<Element<K,V>>[] oldTable = this.table;
        capacity = capacity << 1;
        this.table = new List[this.capacity];
        for (int i = 0; i < capacity; i++) {
            this.table[i] = new LinkedList<>();
        }

        int k = 0;
        int tempCapacity = capacity;
        while (tempCapacity > 1) {
            tempCapacity = tempCapacity >> 1;
            k++;
        }
        this.hashFunc = hashFactory.pickHash(k);

        for (List<Element<K, V>> elements : oldTable) {
            if (elements != null) {
                for (Element<K, V> element : elements) {
                    int currentHash = hashFunc.hash(element.key());
                    table[currentHash].addFirst(element);
                }
            }
        }

    }

    public boolean delete(K key) {
        int hash = hashFunc.hash(key);
        List<Element<K, V>> bucket = table[hash];
        if (bucket == null) {
            return false;
        }
        for (Element<K, V> element : bucket) {
            if (element.key().equals(key)) {
                bucket.remove(element);
                size--;
                return true;
            }
        }
        return false;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
}
