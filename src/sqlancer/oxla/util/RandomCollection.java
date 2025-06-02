package sqlancer.oxla.util;

import sqlancer.Randomly;

import java.util.NavigableMap;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Integer, E> map = new TreeMap<>();
    private final Randomly randomly;
    private int total = 0;

    public RandomCollection(Randomly randomly) {
        this.randomly = randomly;
    }

    public RandomCollection<E> add(int weight, E result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public E getRandom() {
        final int value = randomly.getInteger(0, total + 1);
        return map.higherEntry(value).getValue();
    }
}

