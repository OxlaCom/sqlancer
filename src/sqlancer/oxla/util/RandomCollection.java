package sqlancer.oxla.util;

import sqlancer.Randomly;

import java.util.NavigableMap;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Randomly randomly;
    private double total = 0;

    public RandomCollection(Randomly randomly) {
        this.randomly = randomly;
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) {
            return this;
        }
        total += weight;
        map.put(total, result);
        return this;
    }

    public E getRandom() {
        double value = randomly.getFiniteDouble() * total;
        return map.higherEntry(value).getValue();
    }
}

