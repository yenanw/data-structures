package pqueue;

import java.util.Iterator;
import java.util.Random;

import interfaces.PriorityQueue;
import list.DynamicArrayList;

public class MeldableHeapPriorityQueue<T extends Comparable<T>>
    implements PriorityQueue<T>
{
    private Node root;
    private int size;

    private class Node {
        T key;
        Node left, right;

        Node(T key) {
            this.key = key;
        }
    }

    public MeldableHeapPriorityQueue() {
        root = null;
        size = 0;
    }

    @Override
    public void add(T item) {
        root = meld(root, new Node(item));
        size++;
    }

    @Override
    public T poll() {
        T min = root.key;
        root = meld(root.left, root.right);
        size--;
        return min;
    }

    @Override
    public T peek() {
        return root.key;
    }

    public void meld(MeldableHeapPriorityQueue<T> other) {
        root = meld(root, other.root);
        size += other.size();
        // the other root's content has been modified and is therefore unusable
        other.root = null;
        other.size = 0;
    }
    
    // the entire meldable heap is dependent on this meld method,
    // note that this specific implementation is destructive, so
    // the actual contents of the roots are altered
    private Node meld(Node root1, Node root2) {
        // the case when either one of the roots are empty
        if (root1 == null) return root2;
        if (root2 == null) return root1;
        
        // if the root2 has the smallest minimum then we need to swap
        // these two roots
        if (root2.key.compareTo(root1.key) < 0) {
            Node tmp = root1;
            root1 = root2;
            root2 = tmp;
        }
        // once everything is ready, randomly descend into one of its child
        Random rnd = new Random();
        if (rnd.nextBoolean())
            root1.left = meld(root1.left, root2);
        else
            root1.right = meld(root1.right, root2);

        // return the root
        return root1;
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
    public Iterator<T> iterator() {
        DynamicArrayList<T> list = new DynamicArrayList<>();
        inorder(root, list);
        return list.iterator();
    }

    private void inorder(Node node, DynamicArrayList<T> list) {
        if (node == null)
            return;
        // recursively traverse from the node in an sorted order from the left
        // child to the parent to the right child over all nodes, except heaps
        // are not sorted
        inorder(node.left, list);
        list.add(node.key);
        inorder(node.right, list);
    }
}
