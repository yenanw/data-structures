package map;

import java.util.NoSuchElementException;

import interfaces.OrderedMap;
import list.DynamicArrayList;

public class RedBlackTreeMap<K extends Comparable<K>, V>
    implements OrderedMap<K, V>
{
    private Node root;

    private static boolean RED = true;
    private static boolean BLACK = false;

    private class Node {
        K key;
        V value;
        Node left, right;
        int size; 
        boolean color; 

        Node(K key, V value, int size, boolean color) {
            this.key = key;
            this.value = value;
            this.size = size;
            this.color = color;
        }
    }

    public RedBlackTreeMap() {
        root = null;
    }

    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
        // the root has to always be black
        root.color = BLACK;
    }

    private Node put(K key, V value, Node node) {
        if (node == null)
            // standard insert, always a red node by default
            return new Node(key, value, 1, RED);
        
        // same procedure as BST so far
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            // if key < node.key
            node.left = put(key, value, node.left);
        else if (cmp > 0)
            // if key > node.key
            node.right = put(key, value, node.right);
        else
            // else update the current value
            node.value = value;

        // here we fixed everything that breaks the invariant
        if (isRed(node.right) && !isRed(node.left))
            // red node at right side, black node at left side, breaks the
            // invariant, rotate left to fix it
            node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
            // two consecutive red children on the left, breaks the invariant,
            // rotate right to fix it
            node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
            // both children are red, flip their colors to fix it
            flipColors(node);

        // update the size
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    private boolean isRed(Node node) {
        if (node == null)
            return false;
        // i know, it's just for readability
        return node.color == RED;
    }

    // tbh it lost me the moment it started talking about deletion, so for more 
    // details refer to Algorithm by Robert Sedgewick
    private Node rotateLeft(Node node) {
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        right.color = node.color;
        right.size = node.size;
        node.color = RED;
        node.size = 1 + size(node.left) + size(node.right);
        return right;
    }

    private Node rotateRight(Node node) {
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        left.color = node.color;
        left.size = node.size;
        node.color = RED;
        node.size = 1 + size(node.left) + size(node.right);
        return left;
    }

    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    private Node balance(Node node) {
        if (isRed(node.right) && !isRed(node.left))
            // red node at right side, black node at left side, breaks the
            // invariant, rotate left to fix it
            node = rotateLeft(node);
        if (isRed(node.left) && isRed(node.left.left))
            // two consecutive red children on the left, breaks the invariant,
            // rotate right to fix it
            node = rotateRight(node);
        if (isRed(node.left) && isRed(node.right))
            // both children are red, flip their colors to fix it
            flipColors(node);

        // update the size
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    public void removeMin() {
        if (isEmpty())
            throw new NoSuchElementException();
        
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        
        root = removeMin(root);
        if (!isEmpty())
            root.color = BLACK;
    }

    private Node removeMin(Node node) {
        if (node.left == null)
            return null;

        if (!isRed(node.left) && !isRed(node.left.left))
            node = moveRedLeft(node);

        node.left = removeMin(node.left);
        return balance(node);
    }

    public void removeMax() {
        if (isEmpty())
            throw new NoSuchElementException();

        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = removeMax(root);
        if (!isEmpty())
            root.color = BLACK;
    }

    private Node removeMax(Node node) { 
        if (isRed(node.left))
            node = rotateRight(node);

        if (node.right == null)
            return null;

        if (!isRed(node.right) && !isRed(node.right.left))
            node = moveRedRight(node);

        node.right = removeMax(node.right);
        return balance(node);
    }

    @Override
    public void remove(K key) {
        if (!containsKey(key))
            return;

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = remove(key, root);
        if (!isEmpty())
            root.color = BLACK;
    }

    private Node remove(K key, Node node) {
        if (key.compareTo(node.key) < 0)  {
            if (!isRed(node.left) && !isRed(node.left.left))
                node = moveRedLeft(node);
            node.left = remove(key, node.left);
        } else {
            if (isRed(node.left))
                node = rotateRight(node);
            if (key.compareTo(node.key) == 0 && (node.right == null))
                return null;
            if (!isRed(node.right) && !isRed(node.right.left))
                node = moveRedRight(node);
            if (key.compareTo(node.key) == 0) {
                Node right = minKey(node.right);
                node.key = right.key;
                node.value = right.value;
                node.right = removeMin(node.right);
            } else {
                node.right = remove(key, node.right);
            }
        }
        return balance(node);
    }

    // -------------------DANGER ZONE, might hurt your eyes-------------------
    // Below are the operations identical to binary search trees' which are
    // "reused" here
    // -----------------------------------------------------------------------

    @Override
    public V get(K key) {
        Node result = searchKey(key, root);
        if (result == null)
            return null;

        return result.value;
    }

    @Override
    public boolean containsKey(K key) {
        return searchKey(key, root) != null;
    }

    @Override
    public K minKey() {
        if (isEmpty())
            throw new NoSuchElementException();

        return minKey(root).key;
    }

    private Node minKey(Node node) {
        // keep recursively traversing the left node until it's null,
        // then the lastest non-null node is the minimum key
        if (node.left != null)
            return minKey(node.left);

        return node;
    }

    @Override
    public K maxKey() {
        if (isEmpty())
            throw new NoSuchElementException();

        return maxKey(root).key;
    }

    private Node maxKey(Node node) {
        // keep recursively traversing the right node until it's null,
        // then the lastest non-null node is the maximum key
        if (node.right != null)
            return maxKey(node.right);

        return node;
    }

    @Override
    public K floorKey(K key) {
        Node result = floorKey(key, root);
        if (result == null)
            return null;
        return result.key;
    }

    private Node floorKey(K key, Node node) {
        if (node == null)
            return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp == 0)
            // if key == node.key, we have found the floor
            return node;
        else if (cmp < 0)
            // if key < node.key, keep looking in the left subtree, the floor
            // cannot be in the right tree
            return floorKey(key, node.left);
        
        // else key > node.key, the floor might be in the right subtree
        Node right = floorKey(key, node.right);
        if (right != null)
            // found the floor in the right subtree
            return right;
        // else the floor is not in the right subtree, then current node is
        // the floor
        return node;
    }

    @Override
    public K ceilingKey(K key) {
        Node result = ceilingKey(key, root);
        if (result == null)
            return null;
        return result.key;
    }

    private Node ceilingKey(K key, Node node) {
        // almost identical to floorKey(key,node) but with left and right,
        // < and > interchanged
        if (node == null)
            return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp == 0)
            // if key == node.key, we have found the ceiling
            return node;
        else if (cmp > 0)
            // if key > node.key, keep looking in the right subtree, the ceiling
            // cannot be in the left tree
            return ceilingKey(key, node.right);
        
        // else key < node.key, the floor might be in the left subtree
        Node left = ceilingKey(key, node.left);
        if (left != null)
            // found the floor in the right subtree
            return left;
        // else the floor is not in the right subtree, then current node is
        // the floor
        return node;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null)
            return 0;
        
        return node.size;
    }

    @Override
    public Iterable<K> keys() {
        DynamicArrayList<K> list = new DynamicArrayList<>();
        inorder(root, list);
        return list;
    }

    private Node searchKey(K key, Node node) {
        if (node == null)
            return null;
        
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            // if key < node.key
            return searchKey(key, node.left);
        else if (cmp > 0)
            // if key > node.key
            return searchKey(key, node.right);
        else
            return node;
    }

    private void inorder(Node node, DynamicArrayList<K> list) {
        if (node == null)
            return;
        // recursively traverse from the node in an sorted order from the left
        // child to the parent to the right child over all nodes
        inorder(node.left, list);
        list.add(node.key);
        inorder(node.right, list);
    }
}
