import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> { // object overhead 16 Byte
    // at most 48n + 192 bytes of memory
    private int size; // 4 Byte

    private Node first; // 8 Byte ref
    private Node last; // 8 Byte ref


    private class Node{ // 16 Byte overhead
        Item item; // 8 Byte ref
        Node pre; // 8 Byte ref
        Node next; // 8 Byte ref
    }


    // construct an empty deque
    public Deque(){
        size = 0;
        first = null;
        last = null;
    }


    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size(){return size;}

    // add the item to the front
    public void addFirst(Item item){
        if(item==null){
            throw new IllegalArgumentException();
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.pre = null;

        if(isEmpty()){
            last = first;
        }else{
            oldFirst.pre = first;
        }
        size++;
    }


    // add the item to the back
    public void addLast(Item item){
        if(item==null){
            throw new IllegalArgumentException();
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.pre = oldLast;
        last.next = null;

        if(isEmpty()){
            first = last;
        }else{
            oldLast.next = last;
        }
        size++;
    }


    // remove and return the item from the front
    public Item removeFirst(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        size--; // must decrease first

        if(isEmpty()){
            last = null;
        }else{
            first.pre = null;
        }
        return item;
    }


    // remove and return the item from the back
    public Item removeLast(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        Item item = last.item;
        last = last.pre;
        size--;

        if(isEmpty()){
            first = null;
        }else{
            last.next = null;
        }
        return item;
    }


    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            /* not supported */
            throw new UnsupportedOperationException();
        }
    }


    // unit testing (required)
    public static void main(String[] args){
        Deque<Integer> dq = new Deque<Integer>();
        for(int i=0;i<5;i++){
            dq.addFirst(i);
        }
        for(int i=5;i<10;i++){
            dq.addLast(i);
        }
        StdOut.println(dq.size());
        StdOut.println(dq.removeFirst());
        StdOut.println(dq.size());
        StdOut.println(dq.removeLast());
        StdOut.println(dq.size());
        Iterator<Integer> it = dq.iterator();
        while(it.hasNext()){
            StdOut.println(it.next());
        }
    }
}
