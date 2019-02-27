/*
 * Copyright (c) 2019 Aidan Lloyd-Tucker.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class RandomList<E> {
    // I use a list here just for ease of use and simplicity, and because it was the best idea I thought of
    private final ArrayList<E> internalList;
    private final Random random = new Random();

    public RandomList() {
        internalList = new ArrayList<>();
    }

    public RandomList(Collection<? extends E> c) {
        internalList = new ArrayList<>(c);
    }

    public void add(E e) {
        internalList.add(e);
    }

    // Get and remove from the random list
    public E popRandom() {
        int randIdx = random.nextInt(internalList.size());
        E e = internalList.get(randIdx);
        internalList.remove(randIdx);
        return e;
    }

    public int size() {
        return internalList.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Iterator<E> iterator() {
        return new RandomListIterator<>(internalList);
    }
}
