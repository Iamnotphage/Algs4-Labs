import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randomizedQueue;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue(){
        n = 0;
        randomizedQueue = (Item[]) new Object[1];
    }

    private void resize(int newSize){
//        if(newSize <= 0){
//            throw new IllegalArgumentException();
//        }
        Item[] copy = (Item[]) new Object[newSize];
        for(int i = 0; i < n; i++){
            copy[i] = randomizedQueue[i];
        }
        randomizedQueue = copy;
    }
    // is the randomized queue empty?
    public boolean isEmpty(){return n == 0;}

    // return the number of items on the randomized queue
    public int size(){return n;}

    // add the item
    public void enqueue(Item item){
        if(item == null){
            throw new IllegalArgumentException();
        }
        if(n == randomizedQueue.length){
            // grow array: twice the size
            resize(2 * randomizedQueue.length);
        }
        randomizedQueue[n++] = item;
    }

    // remove and return a random item
    public Item dequeue(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        // random
        int randNum = StdRandom.uniformInt(n); // [0,n)
        Item item = randomizedQueue[randNum];

        // 每次出队列都是等概率(uniformly random)
        // 就不需要线性移动randNum之后的所有元素
        if(randNum != n - 1){
            randomizedQueue[randNum] = randomizedQueue[n - 1];
        }
        randomizedQueue[n - 1] = null;
        n--;

        if(n > 0 && n == randomizedQueue.length / 4){
            resize(randomizedQueue.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return randomizedQueue[StdRandom.uniformInt(n)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int index;
        private Item[] items;

        public RandomizedQueueIterator(){
            index = 0;
            items = (Item[]) new Object[n];
            for(int i=0;i<n;i++){
                items[i] = randomizedQueue[i];
            }
            StdRandom.shuffle(items); // in random order
        }

        public boolean hasNext() {
            return index < n;
        }

        public Item next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            return items[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        StdOut.println(rq.size());
        for(int i=0 ;i < 10; i++){
            rq.enqueue(i);
        }
        StdOut.println(rq.size());
        StdOut.println(rq.dequeue());
        StdOut.println("new size: " + rq.size());
        StdOut.println(rq.sample());
        Iterator<Integer> it = rq.iterator();
        while(it.hasNext()){
            StdOut.println(it.next());
        }
    }
}
