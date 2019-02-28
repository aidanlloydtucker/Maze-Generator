/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RandomListIterator<E> implements Iterator<E> {

    private final static Random random = new Random();
    private int index;
    private final ArrayList<E> list;
    private boolean hasNext = true;
    private final int firstIndex;


    public RandomListIterator(ArrayList<E> l) {
        this.list = l;
        this.index = random.nextInt(list.size());
        this.firstIndex = this.index;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public E next() {
        E e = list.get(index);
        index = (index + 1) % this.list.size();
        if (index == firstIndex) {
            hasNext = false;
        }
        return e;
    }
}
