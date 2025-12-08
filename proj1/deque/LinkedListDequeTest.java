package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;

/* 为 LinkedListDeque 和 ArrayDeque 编写更全面的测试。*/
// 复制并粘贴那些针对 SList 和 AList 的测试
// 并将它们适配到这些数据结构中
// 如果你的随机测试非常庞大，你应该对你的实现充满信心

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest()
    {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();

    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());

    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);

    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest()
    {

        System.out.println("Make sure to uncomment the lines below (and delete this print statement).");

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    public void printTest()
    {
        Deque<Integer> L = new LinkedListDeque<>();
        Deque<Integer> A = new ArrayDeque<>();
        int N = 5000;
        for (int i = 0; i < N; i++)
        {
            int operationNumber = StdRandom.uniform(0, 100);
            L.addFirst(operationNumber);
            A.addFirst(operationNumber);
        }
        A.printDeque();
        L.printDeque();


    }

    @Test
    public void equalTest()
    {
        Deque<Integer> L = new LinkedListDeque<>();
        Deque<Integer> A = new ArrayDeque<>();
        Deque<Integer> L1 = new LinkedListDeque<>();
        Deque<Integer> A1 = new ArrayDeque<>();
        int N = 50;
        for (int i = 0; i < N; i++)
        {
            int operationNumber = StdRandom.uniform(0, 100);
            L.addFirst(operationNumber);
            A.addFirst(operationNumber);
        }
        for (int i = 0; i < L.size(); i++)
        {
            L1.addFirst(L.get(i));
            A1.addFirst(A.get(i));
        }
        assertEquals(L.equals(L1), A.equals(A1));
        if (L.equals(L1))
        {
            System.out.println("L == L1");
        }
        if (A.equals(A1))
        {
            System.out.println("A == A1");
        }
    }

    @Test
    public void randomizedTest()
    {
        Deque<Integer> L = new LinkedListDeque<>();
        Deque<Integer> A = new ArrayDeque<>();

        int N = 10000;
        for (int i = 0; i < N; i += 1)
        {
            int operationNumber = StdRandom.uniform(0, 7); //测试完改成7

            if (operationNumber == 0)
            {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                A.addLast(randVal);
                assertEquals(L.size(), A.size());
            }
            else if (operationNumber == 1)
            {
                // size
                int L_size = L.size();
                int A_size = A.size();
                assertEquals(A_size, L_size);
            }
            else if (operationNumber == 2)
            {
                // addfirst
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                A.addFirst(randVal);
                assertEquals(L.size(), A.size());
            }
            if (operationNumber == 3)
            {
                // get

                if (L.size() <= 1)
                {
                    continue;
                }
                int randVal = StdRandom.uniform(0, L.size());
                int L_get = L.get(randVal);
                int A_get = A.get(randVal);
                assertEquals(A_get, L_get);
            }
            else if (operationNumber == 4)
            {
                // removelast
                if (L.size() <= 0)
                {
                    continue;
                }
                int L_remove = L.removeLast();
                int A_remove = A.removeLast();
                assertEquals(L_remove, A_remove);
            }
            else if (operationNumber == 5)
            {
                // removefirst
                if (L.size() <= 0)
                {
                    continue;
                }
                int L_remove = L.removeFirst();
                int A_remove = A.removeFirst();
                assertEquals(L_remove, A_remove);
            }
            else if (operationNumber == 6)
            {
                //isempty
                assertEquals(L.isEmpty(), A.isEmpty());
            }
        }
    }
}
