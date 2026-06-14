import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;
    private Element<K,V>[] table;


    private int size = 0;

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        this.table = new Element[capacity];

    }
	
	public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public V search(K key) {
        int hash = hashFunc.hash(key);
        int index = hash;
        Element<K,V> element = table[index];
        while ((index < hash + capacity) && (element != null)) {
            if (element.key().equals(key) && !element.getIsDeleted()) {
                return element.satelliteData();
            }
            index++;
            element = table[index % capacity];
        }
        return null;
    }

    public void insert(K key, V value) {
        if (search(key) != null) {
            return;
        }
        int hash = hashFunc.hash(key);
        int index = hash;
        Element<K,V> element = table[index];
        while ((index < hash + capacity) && (element != null) && (!element.getIsDeleted())) {
            index++;
            element = table[index % capacity];
        }
        if ((element == null) || (element.getIsDeleted())) {
            if (element != null) {
                element.setIsDeleted(false);
                element.setKey(key);
                element.setSatData(value);
            }
            else  {
                table[index % capacity] =  new Element<>(key, value);
            }
            size++;
            if ((float) size / capacity >= maxLoadFactor) {
                rehash();
            }
        }
    }

    private void rehash() {
        Element<K,V>[] oldTable = this.table;
        capacity = capacity << 1;
        this.table = new Element[capacity];

        int k = 0;
        int tempCapacity = capacity;
        while (tempCapacity > 1) {
            tempCapacity = tempCapacity >> 1;
            k++;
        }
        this.hashFunc = hashFactory.pickHash(k);

        for (Element<K, V> element : oldTable) {
            if ((element != null) && (!element.getIsDeleted())) {
                int index = hashFunc.hash(element.key());
                while (this.table[index % capacity] != null) {
                    index++;
                }
                this.table[index % capacity] = element;
            }
        }
    }

    public boolean delete(K key) {
        if (search(key) == null) {
            return false;
        }
        int index = hashFunc.hash(key);
        Element<K,V> element = table[index];
        while ((!element.key().equals(key)) || (element.getIsDeleted())) {
            index++;
            element = table[index % capacity];
        }
        element.setIsDeleted(true);
        size--;
        return true;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }
}
