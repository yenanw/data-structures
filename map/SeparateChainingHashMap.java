package map;

import interfaces.Map;

@SuppressWarnings("unchecked")
public class SeparateChainingHashMap<K,V> implements Map<K,V> {
    // the "buckets" for the nodes in the table, here we use a linked list,
    // but in reality, you can use any searchable collection, also this is
    // Object because fuck java and its stupid class casting
    private Object[] table;
    private int size;

    private static final int INIT_SIZE = 64;
    private static final int EXPAND_THRESHOLD = 8;
    private static final int SHRINK_THRESHOLD = 2;

    private class Node {
        K key;
        V value;
        Node next;
        // simple linked list
        public Node(K key, V value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public SeparateChainingHashMap() {
        table = new Object[INIT_SIZE];
        size = 0;
    }

    @Override
    public void put(K key, V value) {
        if (size() >= EXPAND_THRESHOLD * table.length)
            resize(2 * table.length);

        int index = hash(key);
        Node node = node(table[index]);
        while(node != null) {
            // check if the key already exists
            if (node.key.equals(key)) {
                // update the value
                node.value = value;
                return;
            }
            node = node.next;
        }
        // else it doesn't exist
        size++;
        table[index] = new Node(key, value, node(table[index]));
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        Node node = node(table[index]);
        while (node != null) {
            // search through the bucket that might contain the key
            if (node.key.equals(key))
                return node.value;
            node = node.next;
        }
        return null;
    }

    @Override
    public void remove(K key) {
        if (size() <= SHRINK_THRESHOLD * table.length)
            resize(table.length / 2);

        int index = hash(key);
        Node node = node(table[index]);
        // check the first key in the bucket
        if (node != null && node.key.equals(key)) {
            size--;
            table[index] = node.next;
            return;
        }
        // check the rest of the bucket
        while(node != null) {
            if (node.next.key.equals(key)) {
                size--;
                node.next = node.next.next;
                return;
            }
            node = node.next;
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index = hash(key);
        Node node = node(table[index]);
        while (node != null) {
            // search through the bucket that might contain the key
            if (node.key.equals(key))
                return true;
            node = node.next;
        }
        return false;
    }

    private int hash(K key) {
        // important hash function which takes a key and uses its hashCode()
        // to calculate an array index from it
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    private Node node(Object obj) {
        if (obj == null)
            return null;

        return (Node) obj;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size;
    }
    
    @Override
    public Iterable<K> keys() {
        // too much pain in the ass trying to figure out an efficient way
        // to make an iterable for this
        throw new UnsupportedOperationException();
    }

    // awful resize function
    private void resize(int newSize) {
        Object[] oldTable = table;
        table = new Object[newSize];
        // add everything from old table to the new table
        for (Object obj : oldTable) {
            Node node = node(obj);
            while(node != null) {
                this.put(node.key, node.value);
                node = node.next;
            }
        }
    }
}
