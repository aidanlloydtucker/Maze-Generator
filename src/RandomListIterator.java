import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class RandomListIterator<E> implements Iterator<E> {

    private ArrayList<E> list;
    private int index;
    private int firstIndex;
    private boolean hasNext = true;
    private static Random random = new Random();


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
