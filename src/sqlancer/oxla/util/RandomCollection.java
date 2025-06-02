package sqlancer.oxla.util;

import sqlancer.Randomly;

import java.util.NavigableMap;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Integer, E> map = new TreeMap<>();
    private int total = 0;

    public RandomCollection<E> add(int weight, E result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public E getRandom() {
        final long value = Randomly.getNotCachedInteger(0, total + 1);
        return map.higherEntry((int)value).getValue();
    }
}

