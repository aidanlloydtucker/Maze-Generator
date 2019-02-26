import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class RandomList<E> {

    // Make this better
    // MORE EFFICIENT
    private ArrayList<E> internalList;
    private Random random = new Random();

    public RandomList() {
        internalList = new ArrayList<E>();
    }

    public RandomList(Collection<? extends E> c) {
        internalList = new ArrayList<E>(c);
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
        return new RandomListIterator<E>(internalList);
    }
}
