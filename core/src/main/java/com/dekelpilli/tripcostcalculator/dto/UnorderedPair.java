package com.dekelpilli.tripcostcalculator.dto;

import java.util.List;

public class UnorderedPair<T> {
    private final T value1;
    private final T value2;

    public UnorderedPair(T stop1, T stop2) {
        this.value1 = stop1;
        this.value2 = stop2;

        if (stop1.equals(stop2)) {
            throw new IllegalArgumentException("Provided values must be different");
        }
    }

    public static <T> UnorderedPair<T> fromList(List<T> valueList) {
        if (valueList.size() != 2) {
            throw new IllegalArgumentException("List must have exactly 2 items");
        }
        return new UnorderedPair<>(valueList.get(0), valueList.get(1));
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof UnorderedPair)) {
            return false;
        }
        return equalsIgnoreOrder((UnorderedPair) other);
    }

    @Override
    public int hashCode() {
        return value1.hashCode() ^ value2.hashCode();
    }

    private boolean equalsIgnoreOrder(UnorderedPair other) {
        return this.value1.equals(other.value1) && this.value2.equals(other.value2)
                || this.value1.equals(other.value2) && this.value2.equals(other.value1);
    }
}
