package map;

import interfaces.Map;

@SuppressWarnings("unchecked")
public class LinearProbingHashMap<K, V> implements Map<K, V> {
    private Object[] table;
    // the size of all non-deleted nodes
    private int size;
    // the size of all deleted nodes
    private int deleted;
    // to represent a deleted node
    private final Node DELETED;

    private static final int INIT_SIZE = 64;
    // according to some very complicated math calculations which i don't
    // understand the optimal load factor should be below 3/4 
    private static final float MAX_LOAD_FACTOR = 0.75f;
    // even though the smaller the load factor is, the faster the hashmap is,
    // it's still not really worth the memory trade-off
    private static final float MIN_LOAD_FACTOR = 0.25f;

    private class Node {
        K key;
        V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public LinearProbingHashMap() {
        table = new Object[INIT_SIZE];
        size = 0;
        deleted = 0;
        DELETED = new Node(null, null);
    }

    @Override
    public void put(K key, V value) {
        if (loadFactor() > MAX_LOAD_FACTOR)
            resize(2 * table.length);

        int index = hash(key);
        Node node = node(table[index]);
        while (node != null && !isDeleted(node)) {
            if (node.key.equals(key)) {
                // if the key already exists, update the value
                node.value = value;
                return;
            }
            // else keep going through the table to look for an empty spot
            index = (index + 1) % table.length;
            node = node(table[index]);
        }
        // if the node to be replaced is a deleted node then adjust the
        // deleted size
        if (isDeleted(node))
            deleted--;
        // the key doesn't exist in the table, so create a new node
        size++;
        table[index] = new Node(key, value);
    }

    @Override
    public V get(K key) {
        int index = hash(key);
        Node node = node(table[index]);
        while (node != null) {
            if (!isDeleted(node) && node.key.equals(key))
                // we have found our key
                return node.value;
            // keep looking until we found either an empty cell or the key
            // we are looking for
            index = (index + 1) % table.length;
            node = node(table[index]);
        }
        return null;
    }

    @Override
    public void remove(K key) {
        int index = hash(key);
        Node node = node(table[index]);
        while (node != null) {
            if (!isDeleted(node) && node.key.equals(key)) {
                // don't delete the key directly, just set the node as deleted
                table[index] = DELETED;
                deleted++;
                size--;
                // check to make sure the table is not too empty
                if (loadFactor() < MIN_LOAD_FACTOR)
                    resize(table.length / 2);
                return;
            }
            // keep looking until we found either an empty cell or the key
            // we are looking for
            index = (index + 1) % table.length;
            node = node(table[index]);
        }
    }

    @Override
    public boolean containsKey(K key) {
        int index = hash(key);
        Node node = node(table[index]);
        while (node != null) {
            if (!isDeleted(node) && node.key.equals(key))
                // the key exists
                return true;
            // keep looking until we found either an empty cell or the key
            // we are looking for
            index = (index + 1) % table.length;
            node = node(table[index]);
        }
        return false;
    }

    private double loadFactor() {
        return (double) (size + deleted) / table.length;
    }

    private int hash(K key) {
        // first, remove the signed bit in the hashcode so that it's guaranteed
        // a positive number as negative modulo in java is scuffed and then
        // we simply compress the result into a range of 0 to table.length-1
        // and we get the index translated from the key's hashcode
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    private Node node(Object obj) {
        if (obj == null)
            return null;

        return (Node) obj;
    }

    private boolean isDeleted(Node node) {
        return node == DELETED;
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

    // awful resize function, it suffers the same problem as
    // SeparateChainingHashMap, and as always, it's a fix for another day
    private void resize(int newSize) {
        Object[] oldTable = table;
        table = new Object[newSize];
        // reset the sizes
        size = 0;
        deleted = 0;
        // add everything from old table to the new table
        for (Object obj : oldTable) {
            Node node = node(obj);
            if (node != null && !isDeleted(node)) {
                this.put(node.key, node.value);
            }
        }
    }
}
