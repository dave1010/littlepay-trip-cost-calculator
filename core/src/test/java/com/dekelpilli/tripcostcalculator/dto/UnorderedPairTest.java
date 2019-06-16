package com.dekelpilli.tripcostcalculator.dto;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UnorderedPairTest {

    @Test
    void givenListOfSizeOtherThan2__ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UnorderedPair.fromList(Collections.emptyList()));
        assertThrows(IllegalArgumentException.class,
                () -> UnorderedPair.fromList(List.of(1, 2, 3)));
    }

    @Test
    void givenListOfIdenticalItems__ThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> UnorderedPair.fromList(List.of("equalsString", "equalsString")));
    }

    @Test
    void equalsCheckAgainstNonUnorderedPairObject__ReturnsFalse() {
        assertNotEquals(UnorderedPair.fromList(List.of(1, 2)), new Object());
    }

    @Test
    void equalsCheckAgainstOppositeOrderUnorderedPairObject__ReturnsTrue() {
        UnorderedPair<Integer> normalOrder = new UnorderedPair<>(1, 2);
        UnorderedPair<Integer> oppositeOrder = new UnorderedPair<>(2, 1);
        assertEquals(normalOrder, oppositeOrder);
        assertEquals(normalOrder.hashCode(), oppositeOrder.hashCode());
    }

    @Test
    void equalsCheckAgainstNonIdenticalUnorderedPairObject__ReturnsTrue() {
        UnorderedPair<Integer> unorderedPair1 = new UnorderedPair<>(1, 2);
        UnorderedPair<Integer> unorderedPair2 = new UnorderedPair<>(2, 3);
        UnorderedPair<Integer> unorderedPair3 = new UnorderedPair<>(4, 3);
        assertUnorderedPairsNotEqual(unorderedPair1, unorderedPair2);
        assertUnorderedPairsNotEqual(unorderedPair1, unorderedPair3);
        assertUnorderedPairsNotEqual(unorderedPair2, unorderedPair3);
    }

    private static <T> void assertUnorderedPairsNotEqual(UnorderedPair<T> unorderedPair1,
                                                         UnorderedPair<T> unorderedPair2) {
        assertNotEquals(unorderedPair1, unorderedPair2);
        assertNotEquals(unorderedPair1.hashCode(), unorderedPair2.hashCode());
    }
}
