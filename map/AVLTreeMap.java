package map;

import java.util.NoSuchElementException;

import interfaces.OrderedMap;
import list.DynamicArrayList;

public class AVLTreeMap<K extends Comparable<K>,V>
    implements OrderedMap<K,V>
{
    private Node root;

    private class Node {
        K key;
        V value;
        Node left, right;
        int size; 
        int height; 

        Node(K key, V value, int size, int height) {
            this.key = key;
            this.value = value;
            this.size = size;
            this.height = height;
        }
    }

    @Override
    public void put(K key, V value) {
        root = put(key, value, root);
    }

    private Node put(K key, V value, Node node) {
        if (node == null)
            return new Node(key, value, 1, 1);
        // first we run the basic BST insertion algorithm
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
        // balance the node to keep the invariant
        return balance(node);
    }

    @Override
    public void remove(K key) {
        if (!containsKey(key))
            return;

        if (!isEmpty())
            root = remove(key, root);
    }

    private Node remove(K key, Node node) {
        if (node == null)
            return null;
        // first we run the basic BST deletion algorithm
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = remove(key, node.left);
        else if (cmp > 0)
            node.right = remove(key, node.right);
        else {
            // found the key to be removed
            if (node.right == null)
                // the case when the node to be removed only has one or no
                // children, so we just assign the other sibling
                return node.left;
            else if (node.left == null)
                return node.right;
            // else the node to be removed has two children then we removed it
            // by replace it with its successor, first we save the node
            Node t = node;
            // find its successor, a.k.a. the biggest key in the left subtree,
            // or the smallest key in the right subtree, and replace it with
            // the node to be removed
            node = maxKey(node.left);
            // remove the maxKey from the old left tree
            node.left = removeMax(t.left);
            // simply assign the old right tree to the new node
            node.right = t.right;
        }
        // balance the node to keep the invariant
        return balance(node);
    }

    public void removeMax() {
        if (isEmpty())
            throw new NoSuchElementException();

        root = removeMax(root);
    }

    private Node removeMax(Node node) {
        // same as the BST removeMax(node), except it also updates the height
        if (node.right == null)
            // removing the maximum by assigning the left node as the right
            // node of the parent of the node
            return node.left;
        // assuming this is the last step, also when node.right.right == null,
        // then we just assign node.right.left to node.right, irrelevant of
        // node.right.left is null or not, the link to node.right.right is
        // automatically served
        node.right = removeMax(node.right);
        // update the size/height
        updateHeight(node);
        updateSize(node);
        return node;
    }

    public void removeMin() {
        if (isEmpty())
            throw new NoSuchElementException();

        root = removeMin(root);
    }

    private Node removeMin(Node node) {
        // same as the BST removeMin(node), except it also updates the height
        if (node.left == null)
            return node.right;
        
        node.left = removeMin(node.left);
        updateHeight(node);
        updateSize(node);
        return node;
    }

    private Node rotateLeft(Node node) {
        // right child of root becomes the root, old root becomes left child
        Node right = node.right;
        node.right = right.left;
        right.left = node;
        // update the changed sizes/heights
        updateHeight(right.left);
        updateSize(right.left);
        updateHeight(right);
        updateSize(right);
        return right;
    }

    private Node rotateRight(Node node) {
        // symmetric function of rotateLeft(node)
        // left child of root becomes the root, old root becomes right child
        Node left = node.left;
        node.left = left.right;
        left.right = node;
        // update the changed sizes/heights
        updateHeight(left.right);
        updateSize(left.right);
        updateHeight(left);
        updateSize(left);
        return left;
    }

    private Node balance(Node node) {
        if (!isBalanced(node)) {
            // all four possible cases of invariant breaks
            if (height(node.left) > height(node.right)) {
                if (height(node.left.left) < height(node.left.right))
                    // the left-right case
                    node.left = rotateLeft(node.left);
                // both the left-left and left-right cases requires the tree
                // to be rotated right
                node = rotateRight(node);
            } else {
                if (height(node.right.right) < height(node.right.left))
                    // the right-left case
                    node.right = rotateRight(node.right);
                // both the right-right and the right-left cases requires the
                // tree to be rotated left
                node = rotateLeft(node);
            }
        } else {
            // the rotation automatically updates height and size so there is
            // no need to do it again
            updateHeight(node);
            updateSize(node);
        }
        return node;
    }

    private boolean isBalanced(Node node) {
        // if the heights of the children differ by more than 1
        // then the invariant is broken and the tree is therefore unbalanced
        return Math.abs(height(node.left)-height(node.right)) <= 1;
    }

    private void updateSize(Node node) {
        node.size = 1 + size(node.left) + size(node.right);
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int height(Node node) {
        if (node == null)
            return 0;
        
        return node.height;
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
