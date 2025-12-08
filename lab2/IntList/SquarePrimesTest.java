package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest
{

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    // 一个质数
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    // 无质数
    @Test
    public void testSquarePrimesSimple1()
    {
        IntList lst = IntList.of(14, 15, 16, 20, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 20 -> 18", lst.toString());
        assertFalse(changed);
    }

    //
    @Test
    public void testSquarePrimesSimple2()
    {
        IntList lst = IntList.of(1, 2, 3, 4, 5, 6, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("1 -> 4 -> 9 -> 4 -> 25 -> 6 -> 49", lst.toString());
        assertTrue(changed);
    }
}
